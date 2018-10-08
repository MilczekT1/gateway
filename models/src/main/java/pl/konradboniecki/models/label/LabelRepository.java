package pl.konradboniecki.models.label;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends CrudRepository<Label, Long> {
    Optional<Label> findById(Long id);
    Optional<Label> findByLabel(String label);
    List<Label> findAll();
    Label save(Label label);
    long count();
    void deleteById(Long aLong);
}
