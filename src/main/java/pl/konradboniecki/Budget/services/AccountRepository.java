package pl.konradboniecki.Budget.services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findById(Long id);
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    Account save(Account entity);
    long count();
    void deleteById(Long aLong);
}
