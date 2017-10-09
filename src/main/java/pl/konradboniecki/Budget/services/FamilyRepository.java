package pl.konradboniecki.Budget.services;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FamilyRepository extends CrudRepository<Family, Long> {
   Optional<Family> findById(Long id);
   Family save(Family family);
   long count();
   void deleteById(Long aLong);
}
