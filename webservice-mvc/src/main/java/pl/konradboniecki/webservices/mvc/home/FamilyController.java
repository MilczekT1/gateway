package pl.konradboniecki.webservices.mvc.home;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.ServiceManager;
import pl.konradboniecki.models.account.Account;
import pl.konradboniecki.models.budget.Budget;
import pl.konradboniecki.models.budget.BudgetRepository;
import pl.konradboniecki.models.family.Family;
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
@Slf4j
@Controller
@RequestMapping(value = "home/family")
public class FamilyController {

    @Autowired private BudgetRepository budgetRepository;
    @Autowired private FamilyInvitationRepository familyInvitationRepository;
    @Autowired private ServiceManager serviceManager;

    @GetMapping
    public ModelAndView showFamily(ModelMap modelMap) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = serviceManager.findAccountByEmail(email);
        if (!acc.isPresent()){
            return new ModelAndView(ERROR_PAGE, "errorType", PROCESSING_EXCEPTION);
        }

        HashMap<String, Object> modelAttributes = new HashMap<>();
        if (acc.get().hasFamily()){
            // when user has family then get his family, get all invitations to this family
            Family family = serviceManager.findFamilyById(acc.get().getFamilyId()).get();
            modelAttributes.put("familyObject", family);

            List<FamilyInvitation> invitations = familyInvitationRepository.findAllByFamilyId(acc.get().getFamilyId());
            modelMap.putIfAbsent("invitationsList", invitations);
            modelMap.addAttribute("familyObject", family);
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
                    family = serviceManager.findFamilyById(familyId);
                    Optional<Account> account = serviceManager.findAccountById(family.get().getOwnerId());
                    familyOwners.add(account.get());
                }
                modelAttributes.put("familyOwnersList", familyOwners);
            }
            return new ModelAndView(FAMILY_CREATION_PAGE, modelAttributes);
        }
    }

    @PostMapping("/create")
    public ModelAndView createFamilyFromForm(@ModelAttribute("newFamilyCreationForm")
                                             @Valid FamilyCreationForm familyCreationForm,
                                         BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ModelAndView(FAMILY_CREATION_PAGE);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Account> acc = serviceManager.findAccountByEmail(email);

        Family family = new Family(familyCreationForm, acc.get().getId());
        family = serviceManager.saveFamily(family);

        serviceManager.setFamilyIdInAccountWithId(family.getId(), acc.get().getId());

        Budget budget = new Budget();
        budget.setFamilyId(family.getId());
        budget = budgetRepository.save(budget);
        family.setBudgetId(budget.getId());
        serviceManager.updateFamily(family);

        return new ModelAndView("redirect:/home/family");
    }

    @PostMapping("/remove-family")
    public ModelAndView removeFamily(@RequestParam("familyId") Long id){
        if (serviceManager.findFamilyById(id).isPresent()) {
            serviceManager.deleteFamilyById(id);
        }
        return new ModelAndView("redirect:/home/family");
    }
}