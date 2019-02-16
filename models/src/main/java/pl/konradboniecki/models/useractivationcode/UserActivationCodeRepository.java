package pl.konradboniecki.models.useractivationcode;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//TODO: remove this class
@Repository
public interface UserActivationCodeRepository extends CrudRepository<UserActivationCode, Long> {
    Optional<UserActivationCode> findById(Long aLong);
    Optional<UserActivationCode> findByAccountId(Long aLong);
    UserActivationCode save(UserActivationCode entity);
    long count();
    void deleteById(Long aLong);
    boolean existsById(Long id);
}
