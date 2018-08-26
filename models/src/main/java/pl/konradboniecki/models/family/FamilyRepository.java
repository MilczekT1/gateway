package pl.konradboniecki.models.family;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FamilyRepository extends CrudRepository<Family, Long> {
   Optional<Family> findById(Long id);
   Optional<Family> findByOwnerId(Long id);
   Family save(Family family);
   long count();
   void deleteById(Long aLong);
   
   @Query(value="SELECT (max_members-members_amount) from family_members where family_id =?1 ", nativeQuery=true)
   Long countFreeSlotsInFamilyWithId(Long familyId);

   @Modifying
   @Transactional
   @Query (value="UPDATE family SET budget_id = ?1 WHERE family_id = ?2", nativeQuery=true)
   void setBudgetId(Long budgetId, Long familyId);
}
