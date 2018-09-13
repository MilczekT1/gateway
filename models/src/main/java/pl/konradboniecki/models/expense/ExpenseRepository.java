package pl.konradboniecki.models.expense;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    Optional<Expense> findById(Long id);
    List<Expense> findAllByBudgetId(Long id);
    Optional<Expense> findByBudgetId(Long id);
    Expense save(Expense expense);
    long count();
    void deleteById(Long aLong);
}