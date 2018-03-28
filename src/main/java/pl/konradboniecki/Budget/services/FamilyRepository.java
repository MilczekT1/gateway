package pl.konradboniecki.Budget.services;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pl.konradboniecki.Budget.models.family.Family;

import java.util.Optional;

@Service
public interface FamilyRepository extends CrudRepository<Family, Long> {
   Optional<Family> findById(Long id);
   Optional<Family> findByOwnerId(Long id);
   Family save(Family family);
   long count();
   void deleteById(Long aLong);
   
   @Query(value="SELECT (max_members-members_amount) from family_members where family_id =?1 ", nativeQuery=true)
   Integer getFreeSlotsFromFamily(Long familyId);
}
