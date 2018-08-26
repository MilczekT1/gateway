package pl.konradboniecki.models.budget;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, Long> {
    Optional<Budget> findById(Long id);
    Optional<Budget> findByFamilyId(Long id);
    Budget save(Budget budget);
    long count();
    void deleteById(Long aLong);

    @Modifying
    @Transactional
    @Query(value="UPDATE budget SET family_id = ?1 WHERE budget_id = ?2", nativeQuery=true)
    void setFamilyId(Long familyId, Long accountId);
}
