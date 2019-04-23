package pl.konradboniecki.webservices.mvc.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Throwables;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.ServiceManager;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.models.familyinvitation.FamilyInvitation;
import pl.konradboniecki.models.familyinvitation.FamilyInvitationRepository;
import pl.konradboniecki.utils.BudgetAdress;
import pl.konradboniecki.utils.RestCall;
import pl.konradboniecki.utils.TokenGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static pl.konradboniecki.templates.ViewTemplate.ERROR_PAGE;
import static pl.konradboniecki.templates.ViewTemplate.FAMILY_HOME_PAGE;
import static pl.konradboniecki.utils.enums.ErrorType.*;

@Slf4j
@Controller
@RequestMapping(value = "home/family")
public class FamilyInvitationController {

    @Autowired private AccountRepository accountRepository;
    @Autowired private FamilyInvitationRepository familyInvitationRepository;
    @Autowired private RestCall restCall;
    @Autowired private ServiceManager serviceManager;

    @PostMapping("/invite-to-family")
    public ModelAndView handleInvitationToFamilyFromApp(@RequestParam("newMemberEmail") String newMemberEmail,
                                                        @ModelAttribute("familyObject") Family family){
        String invitationCode = new TokenGenerator().createUUIDToken();
        Map<String, Object> jsonObjects = new LinkedHashMap<>();
        boolean isNewUser = false;

        if(accountRepository.existsByEmail(newMemberEmail)) {
            Account account = serviceManager.findAccountByEmail(newMemberEmail).get();
            Account owner = serviceManager.findAccountById(family.getOwnerId()).get();

            try {
                jsonObjects.put("Account", account);
                jsonObjects.put("Inviter", owner);
                jsonObjects.put("Family", family);
                jsonObjects.put("InvitationCode", invitationCode);
                String url = BudgetAdress.getURI() + ":3002/api/mail/invite-user/existing";
                restCall.performPostWithJSON(url, jsonObjects);
            } catch (JsonProcessingException | UnirestException  e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new ModelAndView(ERROR_PAGE, "errorType",
                        PROCESSING_EXCEPTION.getErrorTypeVarName());
            }
        } else {
            //Invitation Code is not neccessary
            isNewUser = true;
            String inviterEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Account owner = serviceManager.findAccountByEmail(inviterEmail).get();

            try {
                jsonObjects.put("Inviter", owner);
                jsonObjects.put("Family", family);
                jsonObjects.put("NewMemberEmail", newMemberEmail);
                String url = BudgetAdress.getURI() + ":3002/api/mail/invite-user/new";
                restCall.performPostWithJSON(url, jsonObjects);
            } catch (JsonProcessingException | UnirestException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new ModelAndView(ERROR_PAGE, "errorType",
                        PROCESSING_EXCEPTION.getErrorTypeVarName());
            }
        }

        Optional<FamilyInvitation> familyInvitation =
                familyInvitationRepository.findByEmailAndFamilyId(newMemberEmail, family.getId());
        if (familyInvitation.isPresent()){
            familyInvitationRepository.deleteById(familyInvitation.get().getId());
        }
        familyInvitationRepository.save(new FamilyInvitation(newMemberEmail, family.getId(), invitationCode, isNewUser));

        return new ModelAndView("redirect:/" + FAMILY_HOME_PAGE, "familyObject", family);
    }

    @PostMapping("/invite-to-family/resend-invitation")
    public ModelAndView resendInvitationMail(@RequestParam("invitationId") Long invitationId){
        Map<String, Object> jsonObjects = new LinkedHashMap<>();

        Optional<FamilyInvitation> familyInvitation = familyInvitationRepository.findById(invitationId);
        if (familyInvitation.isPresent()){
            String emailDest = familyInvitation.get().getEmail();
            Optional<Account> account = serviceManager.findAccountByEmail(emailDest);
            Family family = serviceManager.findFamilyById(familyInvitation.get().getFamilyId()).get();
            if (account.isPresent()){
                Account owner = serviceManager.findAccountById(family.getOwnerId()).get();
                try {
                    jsonObjects.put("Account", account.get());
                    jsonObjects.put("Inviter", owner);
                    jsonObjects.put("Family", family);
                    jsonObjects.put("InvitationCode", familyInvitation.get().getInvitationCode());
                    String url = BudgetAdress.getURI() + ":3002/api/mail/invite-user/existing";
                    restCall.performPostWithJSON(url, jsonObjects);
                } catch (JsonProcessingException | UnirestException  e) {
                    log.error(Throwables.getStackTraceAsString(e));
                    return new ModelAndView(ERROR_PAGE, "errorType",
                            PROCESSING_EXCEPTION.getErrorTypeVarName());
                }
            } else {
                //Invitation Code is not neccessary
                String inviterEmail = SecurityContextHolder.getContext().getAuthentication().getName();
                Account owner = serviceManager.findAccountByEmail(inviterEmail).get();
                try {
                    jsonObjects.put("Inviter", owner);
                    jsonObjects.put("Family", family);
                    jsonObjects.put("NewMemberEmail", emailDest);
                    String url = BudgetAdress.getURI() + ":3002/api/mail/invite-user/new";
                    restCall.performPostWithJSON(url, jsonObjects);
                } catch (JsonProcessingException | UnirestException e) {
                    log.error(Throwables.getStackTraceAsString(e));
                    return new ModelAndView(ERROR_PAGE, "errorType",
                            PROCESSING_EXCEPTION.getErrorTypeVarName());
                }
            }
        }
        return new ModelAndView("redirect:/" + FAMILY_HOME_PAGE);
    }

    @GetMapping("/{familyId}/addMember/{id}/{invitationCode}")
    public ModelAndView addAccountToFamily(@PathVariable("invitationCode") String code,
                                           @PathVariable("id") Long accountId,
                                           @PathVariable("familyId") Long familyId){

        if(serviceManager.findFamilyById(familyId).isPresent() && accountRepository.existsById(accountId)){
            Account account = serviceManager.findAccountById(accountId).get();
            if (account.hasFamily()){
                return new ModelAndView(ERROR_PAGE, "errorType", ALREADY_IN_FAMILY);
            } else {
                Optional<FamilyInvitation> familyInvitation =
                        familyInvitationRepository.findByEmailAndFamilyId(account.getEmail(),familyId);
                if (familyInvitation.isPresent()){
                    if (!familyInvitation.get().getInvitationCode().equals(code)){
                        log.error("Wrong invitation code: " + familyInvitation.get().toString()
                                + "and given invitation code: " + code);
                        return new ModelAndView(ERROR_PAGE, "errorType", INVALID_INVITATION_LINK);
                    } else {
                        if(serviceManager.countFreeSlotsInFamilyWithId(familyId) > 0) {
                            accountRepository.setFamilyId(familyId, accountId);
                            familyInvitationRepository.deleteById(familyInvitation.get().getId());
                        } else {
                            log.error("Not enough space in family for new user " + account.toString() +
                                    "in family with id: " + familyId);
                            return new ModelAndView(ERROR_PAGE, "errorType", NOT_ENOUGH_SPACE_IN_FAMILY);
                        }
                    }
                }
                else{
                    log.error("No such family invitation with  " + account.getEmail() + " and familyId:" + familyId);
                    return new ModelAndView(ERROR_PAGE, "errorType", INVALID_INVITATION_LINK);
                }
            }
        }
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/accept-invitation-in-family-creation-form")
    public ModelAndView acceptInvitationInFamilyCreationForm(
            @RequestParam(value = "familyOwnerId") Long ownerId) {

        String inviteeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> invitee = serviceManager.findAccountByEmail(inviteeEmail);
        Optional<Account> ownerOpt = serviceManager.findAccountById(ownerId);
        Long familyId = ownerOpt.get().getFamilyId();

        Optional<FamilyInvitation> invitationToDelete = familyInvitationRepository.findByEmailAndFamilyId(inviteeEmail, familyId);
        if (invitationToDelete.isPresent()){
            familyInvitationRepository.deleteById(invitationToDelete.get().getId());
        }

        accountRepository.setFamilyId(ownerOpt.get().getFamilyId(), invitee.get().getId());
        return new ModelAndView("redirect:/home/family");
    }

    @PostMapping("/remove")
    public ModelAndView removeInvitation(@RequestParam("invitationId") Long invitationId){

        Optional<FamilyInvitation> familyInvitation = familyInvitationRepository.findById(invitationId);
        if (familyInvitation.isPresent()){
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            familyInvitationRepository.deleteById(invitationId);
            log.info("Invitation with id: " + invitationId + " to " + familyInvitation.get().getEmail()
                    + " has been deleted by: " + email);
        }
         return new ModelAndView("redirect:/" + FAMILY_HOME_PAGE);
    }
}
