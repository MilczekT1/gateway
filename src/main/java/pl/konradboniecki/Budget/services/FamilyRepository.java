package pl.konradboniecki.Budget.services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pl.konradboniecki.Budget.models.family.Family;

import java.util.Optional;

@Service
public interface FamilyRepository extends CrudRepository<Family, Long> {
   Optional<Family> findById(Long id);
   Family save(Family family);
   long count();
   void deleteById(Long aLong);
}
