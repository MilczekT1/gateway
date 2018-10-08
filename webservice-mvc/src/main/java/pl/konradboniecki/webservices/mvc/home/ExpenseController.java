package pl.konradboniecki.webservices.mvc.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.konradboniecki.models.expense.Expense;
import pl.konradboniecki.models.expense.ExpenseRepository;
import pl.konradboniecki.models.frontendforms.ExpenseCreationForm;
import pl.konradboniecki.models.label.Label;
import pl.konradboniecki.models.label.LabelRepository;

import javax.validation.Valid;
import java.util.Optional;

import static pl.konradboniecki.templates.ViewTemplate.BUDGET_HOME_PAGE;
import static pl.konradboniecki.templates.ViewTemplate.EXPENSE_CREATION_PAGE;

@Controller
@RequestMapping(value = "home/budget/expense")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private LabelRepository labelRepository;


    @GetMapping("/show-form")
    public ModelAndView showExpenseForm(@ModelAttribute("budgetId") Long budgetId, ModelMap modelMap){
        modelMap.addAttribute("newExpenseCreationForm", new ExpenseCreationForm());
        modelMap.addAttribute("budgetId", budgetId);
        return new ModelAndView(EXPENSE_CREATION_PAGE, modelMap);
    }

    @PostMapping("/add")
    public ModelAndView addLabel(
            @ModelAttribute("newExpenseCreationForm") @Valid ExpenseCreationForm form,
            @ModelAttribute("budgetId") Long budgetId, BindingResult bindingResult, ModelMap modelMap){
        if (budgetId == null){
            throw new NullPointerException();
        }
        if (bindingResult.hasErrors()) {
            //TODO: check if budget id is lost or not
            return new ModelAndView(EXPENSE_CREATION_PAGE);
        }
        Expense expense = new Expense(form);
        expense.setBudgetId(budgetId);
        // Find label by name and budgetid
        Optional<Label> labelOptional = labelRepository.findByLabel(form.getLabel());
        Label label;
        if (labelOptional.isPresent()){
            label = labelOptional.get();
            expense.setLabelId(label.getId());
            expenseRepository.save(expense);
        } else {
            expense = expenseRepository.save(expense);

            label = new Label();
            label.setLabel(form.getLabel());
            label.setExpenseId(expense.getId());
            label = labelRepository.save(label);
            Optional<Expense> newExpense = expenseRepository.findById(expense.getId());
            Expense exp1 = newExpense.get();
            exp1.setLabelId(label.getId());
            expenseRepository.save(exp1);
        }

        return new ModelAndView("redirect:/" + BUDGET_HOME_PAGE);
    }
    @PostMapping("/remove")
    public ModelAndView removeExpenseFromBudget(@ModelAttribute("expenseId") Long expenseId){
        expenseRepository.deleteById(expenseId);
        return new ModelAndView("redirect:/" + BUDGET_HOME_PAGE);
    }
}