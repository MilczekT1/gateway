package pl.konradboniecki.webservices.mvc.home;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Throwables;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.models.family.FamilyRepository;
import pl.konradboniecki.models.familyinvitation.FamilyInvitation;
import pl.konradboniecki.models.familyinvitation.FamilyInvitationRepository;
import pl.konradboniecki.utils.BudgetAdress;
import pl.konradboniecki.utils.TokenGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static pl.konradboniecki.utils.enums.ErrorType.*;
import static pl.konradboniecki.utils.RestCall.performPostWithJSON;
import static pl.konradboniecki.utils.template.ViewTemplate.ERROR_PAGE;
import static pl.konradboniecki.utils.template.ViewTemplate.FAMILY_HOME_PAGE;

@Controller
@RequestMapping(value = "home/family")
public class FamilyInvitationController {
    private static final Logger log = LoggerFactory.getLogger(FamilyInvitationController.class);

    @Autowired private AccountRepository accountRepository;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private FamilyInvitationRepository familyInvitationRepository;

    @PostMapping("/invite-to-family")
    public ModelAndView handleInvitationToFamilyFromApp(@RequestParam("newMemberEmail") String newMemberEmail,
                                                        @ModelAttribute("familyObject") Family family){

        String invitationCode = TokenGenerator.createUUIDToken();
        Map<String, Object> jsonObjects = new LinkedHashMap<>();
        boolean isNewUser = false;

        if(accountRepository.existsByEmail(newMemberEmail)) {
            Account account = accountRepository.findByEmail(newMemberEmail).get();
            Account owner = accountRepository.findById(family.getOwnerId()).get();

            try {
                jsonObjects.put("Account", account);
                jsonObjects.put("Owner", owner);
                jsonObjects.put("Family", family);
                jsonObjects.put("InvitationCode", invitationCode);
                String URL = BudgetAdress.getURI() + ":3002/services/mail/invitation/existing-user";
                performPostWithJSON(URL, jsonObjects);
            } catch (JsonProcessingException | UnirestException  e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new ModelAndView(ERROR_PAGE, "errorType",
                        PROCESSING_EXCEPTION.getModelAttrName());
            }
        } else {
            //Invitation Code is not neccessary
            isNewUser = true;
            String inviterEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Account owner = accountRepository.findByEmail(inviterEmail).get();

            try {
                jsonObjects.put("Owner", owner);
                jsonObjects.put("Family", family);
                jsonObjects.put("NewMemberEmail", newMemberEmail);
                String URL = BudgetAdress.getURI() + ":3002/services/mail/invitation/new-user";
                performPostWithJSON(URL, jsonObjects);
            } catch (JsonProcessingException | UnirestException e) {
                log.error(Throwables.getStackTraceAsString(e));
                return new ModelAndView(ERROR_PAGE, "errorType",
                        PROCESSING_EXCEPTION.getModelAttrName());
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


    @GetMapping("/{familyId}/addMember/{id}/{invitationCode}")
    public ModelAndView addAccountToFamily(@PathVariable("invitationCode") String code,
                                           @PathVariable("id") Long accountId,
                                           @PathVariable("familyId") Long familyId){

        if(familyRepository.existsById(familyId) && accountRepository.existsById(accountId)){
            Account account = accountRepository.findById(accountId).get();
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
                        if(familyRepository.getFreeSlotsFromFamily(familyId) > 0) {
                            accountRepository.setFamilyId(familyId, accountId);
                            familyInvitationRepository.deleteById(familyInvitation.get().getId());
                        } else {
                            log.error("Not enough space in family for new user " + account.toString() +
                                    "in familywith id: " + familyId);
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
        Optional<Account> invitee = accountRepository.findByEmail(inviteeEmail);
        Optional<Account> ownerOpt = accountRepository.findById(ownerId);
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
