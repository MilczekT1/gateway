package pl.konradboniecki.Budget.services;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.konradboniecki.Budget.models.account.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findById(Long id);
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    Account save(Account entity);
    long count();
    void deleteById(Long aLong);
    
    @Modifying
    @Transactional
    @Query (value="UPDATE account SET enabled = 1 WHERE account_id = ?1", nativeQuery=true)
    void setEnabled(Long id);
    
    @Modifying
    @Transactional
    @Query (value="UPDATE account SET password = ?1 WHERE account_id = ?2", nativeQuery=true)
    void changePassword(String newPassword, Long id);
}
