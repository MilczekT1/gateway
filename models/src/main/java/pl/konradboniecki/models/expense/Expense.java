package pl.konradboniecki.models.expense;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import pl.konradboniecki.models.frontendforms.ExpenseCreationForm;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Data
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "expense_id")
    private Long id;

    @Column(name = "budget_id")
    private Long budgetId;
    @Column(name = "label_id")
    private Long labelId;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "comment")
    private String comment;
    @Column(name = "expense_date")
    private ZonedDateTime expenseDate;

    public Expense(){
        setExpenseDate(ZonedDateTime.now(ZoneId.of("Europe/Warsaw")));
    }

    public Expense(ExpenseCreationForm expenseCreationForm){
        this();
        setAmount(expenseCreationForm.getAmount());
        setComment(expenseCreationForm.getComment());
    }
}
