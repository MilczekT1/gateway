package pl.konradboniecki.models.frontendforms;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ExpenseCreationForm {

    @Size(max = 50, message = "{expenseCreationForm.expenseCommentMaxSize}")
    private String comment;

//    @NotNull//(message = "{expenseCreationForm.amountNotNull}")
    private Long amount;
}
