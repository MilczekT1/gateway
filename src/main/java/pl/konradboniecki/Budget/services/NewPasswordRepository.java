package pl.konradboniecki.Budget.services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.konradboniecki.Budget.models.newPassword.NewPassword;

import java.util.Optional;

@Repository
public interface NewPasswordRepository extends CrudRepository<NewPassword,Long>{
    Optional<NewPassword> findById(Long aLong);
    NewPassword save(NewPassword entity);
    long count();
    void deleteById(Long aLong);
    boolean existsById(Long id);
}
