package pl.konradboniecki.webservices.mvc.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.account.AccountRepository;
import pl.konradboniecki.models.family.Family;
import pl.konradboniecki.models.family.FamilyRepository;
import pl.konradboniecki.models.familyinvitation.FamilyInvitation;
import pl.konradboniecki.models.familyinvitation.FamilyInvitationRepository;
import pl.konradboniecki.models.frontendforms.FamilyCreationForm;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static pl.konradboniecki.templates.ViewTemplate.*;
import static pl.konradboniecki.utils.enums.ErrorType.PROCESSING_EXCEPTION;


@Controller
@RequestMapping(value = "home/family")
public class FamilyController {
    
    @Autowired private AccountRepository accountRepository;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private FamilyInvitationRepository familyInvitationRepository;

    @GetMapping
    public ModelAndView showFamily(ModelMap modelMap) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        HashMap<String, Object> modelAttributes = new HashMap<>();

        if (acc.isPresent()){
            if (acc.get().hasFamily()){
                Optional<Family> familyOptional = familyRepository.findById(acc.get().getFamilyId());
                modelAttributes.put("familyObject", familyOptional.get());

                List<FamilyInvitation> invitations = familyInvitationRepository.findAllByFamilyId(acc.get().getFamilyId());
                modelMap.putIfAbsent("invitationsList", invitations);
                modelMap.addAttribute("familyObject", familyOptional.get());
                return new ModelAndView(FAMILY_HOME_PAGE, modelMap);
            } else {
                modelAttributes.put("newFamilyCreationForm", new FamilyCreationForm());
                List<FamilyInvitation> invitations = familyInvitationRepository.findAllByEmail(email);
                List<Long> familyIds = new LinkedList<>();
                for (FamilyInvitation invitation : invitations){
                    familyIds.add(invitation.getFamilyId());
                }
                invitations.clear();

                if (!familyIds.isEmpty()){
                    Optional<Family> family;
                    List<Account> familyOwners = new LinkedList<>();
                    for(Long familyId : familyIds) {
                        family = familyRepository.findById(familyId);
                        Optional<Account> account = accountRepository.findById(family.get().getOwnerId());
                        familyOwners.add(account.get());
                    }
                    modelAttributes.put("familyOwnersList", familyOwners);
                }
                return new ModelAndView(FAMILY_CREATION_PAGE, modelAttributes);
            }
        } else {
            return new ModelAndView(ERROR_PAGE, "errorType", PROCESSING_EXCEPTION);
        }
    }

    @PostMapping("/create")
    public ModelAndView createFamilyForm(@ModelAttribute("newFamilyCreationForm")
                                             @Valid FamilyCreationForm familyCreationForm,
                                         BindingResult bindingResult){
    
        if (bindingResult.hasErrors()) {
            return new ModelAndView(FAMILY_CREATION_PAGE);
        }
        
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = accountRepository.findByEmail(email);
        
        Family family = new Family(familyCreationForm,acc.get().getId());
        family = familyRepository.save(family);
        
        accountRepository.setFamilyId(family.getId(),acc.get().getId());
        
        return new ModelAndView("redirect:/home/family");
    }

    @PostMapping("/remove-family")
    public ModelAndView removeFamily(@RequestParam("familyId") Long id){
        if (familyRepository.findById(id).isPresent()) {
            familyRepository.deleteById(id);
        }
        return new ModelAndView("redirect:/home/family");
    }
}