package pl.konradboniecki.Budget.controllers.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.Budget.core.Utils;
import pl.konradboniecki.Budget.models.account.Account;
import pl.konradboniecki.Budget.models.family.Family;
import pl.konradboniecki.Budget.models.family.FamilyCreationForm;
import pl.konradboniecki.Budget.models.familyinvitation.FamilyInvitation;
import pl.konradboniecki.Budget.services.AccountRepository;
import pl.konradboniecki.Budget.services.FamilyInvitationRepository;
import pl.konradboniecki.Budget.services.FamilyRepository;
import pl.konradboniecki.Budget.services.MailService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "home")
public class FamilyController {
    
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    FamilyRepository familyRepository;
    @Autowired
    MailService mailService;
    @Autowired
    FamilyInvitationRepository familyInvitationRepository;
    
    @GetMapping ("/family")
    public ModelAndView showFamily() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        
        if (acc.isPresent()){
            if (acc.get().hasFamily()){
                //TODO: modify finding family
                Optional<Family> familyOptional = familyRepository.findById(acc.get().getFamilyId());
                return new ModelAndView("home/family", "familyObject",familyOptional.get());
            } else {
                HashMap<String, Object> modelAttributes = new HashMap<>();
                modelAttributes.put("newFamilyCreationForm", new FamilyCreationForm());
                /*
                  TODO:
                  1. Create View
                  2. Obtain data
                  3. put data into model
                */
                 //find all invtitations and put them into model
                
                List<FamilyInvitation> invitations = familyInvitationRepository.findAllByEmail(email);

                List<Long> familyIds = new LinkedList<>();
                for (FamilyInvitation invitation : invitations){
                    familyIds.add(invitation.getFamilyId());
                }
                invitations.clear();

                if (!familyIds.isEmpty()){
                    Optional<Family> family;
                    String firstName;
                    String lastName;
                    String invitationEmail;
                    List<Account> familyOwners = new LinkedList<>();
                    for(Long familyId : familyIds) {
                        family = familyRepository.findById(familyId);
                        Optional<Account> account = accountRepository.findById(family.get().getOwnerId());
                        familyOwners.add(account.get());
//                        firstName = account.get().getFirstName();
//                        lastName = account.get().getLastName();
//                        invitationEmail = account.get().getEmail();
//                        System.out.println(familyId + " " + firstName + " " + lastName + " " + invitationEmail);
                      //  modelAttributes.put
                    }
//                    /*for (FamilyInvitation invitation : invitations){
//                        modelAttributes.put()
//                    }*/
                    modelAttributes.put("familyOwnersList", familyOwners);
                }
                return new ModelAndView("home/familyCreationForm", modelAttributes);
            }
        } else {
            return new ModelAndView("error");
        }
    }

    @PostMapping("/family/processInvitation")
    public ModelAndView processInvitation(@ModelAttribute("familyOwner") Account owner) {

        String inviteeEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long familyId = owner.getFamilyId();

        Optional<Account> invitee = accountRepository.findByEmail(inviteeEmail);
        Optional<FamilyInvitation> invitationToDelete = familyInvitationRepository.findByEmailAndFamilyId(inviteeEmail,familyId);
        if (invitationToDelete.isPresent()){
            familyInvitationRepository.deleteById(invitationToDelete.get().getId());
        }
        accountRepository.setFamilyId(owner.getFamilyId(),invitee.get().getId());
        return new ModelAndView("redirect:/home/family");
    }
    
    @PostMapping("/family/create")
    public ModelAndView createFamilyForm(@ModelAttribute("newFamilyCreationForm")
                                             @Valid FamilyCreationForm familyCreationForm,
                                         BindingResult bindingResult){
    
        if (bindingResult.hasErrors()) {
            return new ModelAndView("home/familyCreationForm");
        }
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        
        Family family = new Family(familyCreationForm,acc.get().getId());
        family = familyRepository.save(family);
        
        accountRepository.setFamilyId(family.getId(),acc.get().getId());
        
        return new ModelAndView("redirect:/home/family");
    }
    
    @GetMapping("/family/{id}/addMember/{accountId}/{invitationCode}")
    public ModelAndView handleAccountAdditionToFamily(@PathVariable("invitationCode") String code,
                                                      @PathVariable("accountId") Long accountId,
                                                      @PathVariable("id") Long familyId){
        
        if(familyRepository.existsById(familyId) && accountRepository.existsById(accountId)){
            Optional<Account> account = accountRepository.findById(accountId);
            if (account.get().hasFamily()){
                return new ModelAndView("redirect:/login");
                // TODO: generate error page with info that account had already been in family.
            } else {
                if(familyRepository.getFreeSlotsFromFamily(familyId) > 0) {
                    accountRepository.setFamilyId(familyId, accountId);
                }
            }
        }
        return new ModelAndView("redirect:/login");
    }
    
    @PostMapping("/family/addMember")
    public ModelAndView handleInvitationToFamily(@RequestParam("newMemberEmail") String newMemberEmail,
                                                 @RequestParam("familyId") Long familyId,
                                                 @ModelAttribute("accountFormObject")
                                                     @Valid Family familyObject,
                                                 BindingResult bindingResult){
        
        if(accountRepository.existsByEmail(newMemberEmail)) {
            String invitationCode = Utils.createInvitationCode(newMemberEmail);
            Optional<Account> account = accountRepository.findByEmail(newMemberEmail);
            Family family = familyRepository.findById(familyId).get();
            mailService.sendFamilyInvitationToUserWithAccount(account.get(), family, invitationCode);
            familyInvitationRepository.save(new FamilyInvitation(newMemberEmail, familyId));
        } else {

            String inviterEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<Account> accountOptional = accountRepository.findByEmail(inviterEmail);
            
            mailService.sendFamilyInvitationToUserWithoutAccount(accountOptional.get(), newMemberEmail);
            // TODO: nie da sie ponownie zaprosic osoby ;(
            familyInvitationRepository.save(new FamilyInvitation(newMemberEmail, familyId));
            
        }
        return new ModelAndView("home/family", "familyObject", familyObject);
    }
}