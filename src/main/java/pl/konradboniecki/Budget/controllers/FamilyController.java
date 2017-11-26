package pl.konradboniecki.Budget.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.konradboniecki.Budget.models.family.Family;
import pl.konradboniecki.Budget.models.family.FamilyForm;
import pl.konradboniecki.Budget.services.FamilyRepository;

@Controller
@RequestMapping("/family")
public class FamilyController {
    
    @Autowired
    FamilyRepository familyRepository;
    
    @GetMapping("/create")
    public String showCreatingPanel(Model model){
        model.addAttribute("familyFormObject", new FamilyForm());
        return "createFamily";
    }
    
    @PostMapping("/create")
    public String createFamily(@ModelAttribute(name="familyFormObject") FamilyForm familyForm){
        
        
        //wyciagnac id z uzytkownika
        //wyciagnac role uzytkownika
        Family family = new Family();
        //if admin
        //family.setEmailNotificationsEnabled(true);
        //family.setPhoneNotificationsEnabled(true);
        //family.setMaxJars(50);
        //family.setMaxMembers(10);
    
        
        //family.setMaxMembers(4);
        //family.setMaxJars(6);
        //family.setTitle(familyForm.getTitle());
        
        //jfamilyRepository.save(family);
        return null;
    }
}
