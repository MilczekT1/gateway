package pl.konradboniecki.models.frontendforms;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ExpenseCreationForm {

    @Size(max = 50, message = "{expenseCreationForm.expenseCommentMaxSize}")
    private String comment;
    @Size(max = 30, message = "{expenseCreationForm.labelMaxSize}")
    private String label;
    private Long amount;
}
