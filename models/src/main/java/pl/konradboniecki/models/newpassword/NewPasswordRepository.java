package pl.konradboniecki.models.newpassword;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewPasswordRepository extends CrudRepository<NewPassword,Long>{
    Optional<NewPassword> findById(Long aLong);
    NewPassword save(NewPassword entity);
    long count();
    void deleteById(Long aLong);
    boolean existsById(Long id);
}
