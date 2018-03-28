package pl.konradboniecki.Budget.services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import pl.konradboniecki.Budget.models.familyinvitation.FamilyInvitation;

import java.util.List;
import java.util.Optional;

@Service
public interface FamilyInvitationRepository extends CrudRepository<FamilyInvitation, Long> {

    Optional<FamilyInvitation> findById(Long id);
    Optional<FamilyInvitation> findByEmail(String email);
    Optional<FamilyInvitation> findByEmailAndFamilyId(String email, Long familyId);
    FamilyInvitation save(FamilyInvitation familyInvitation);
    long count();
    void deleteById(Long aLong);
    List<FamilyInvitation> findAllByEmail(String email);
}