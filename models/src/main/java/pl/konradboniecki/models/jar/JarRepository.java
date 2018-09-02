package pl.konradboniecki.models.jar;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface JarRepository extends CrudRepository<Jar, Long> {
    Optional<Jar> findById(Long id);
    Optional<Jar> findByBudgetId(Long id);
    List<Jar> findAllByBudgetId(Long id);
    Jar save(Jar jar);
    long count();
    void deleteById(Long aLong);

    @Modifying
    @Transactional
    @Query(value="UPDATE jar SET current_amount = ?1 WHERE jar_id = ?2", nativeQuery=true)
    void setCurrentAmount(Long newAmount, Long jarId);
}