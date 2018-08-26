package pl.konradboniecki.models.jar;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
}