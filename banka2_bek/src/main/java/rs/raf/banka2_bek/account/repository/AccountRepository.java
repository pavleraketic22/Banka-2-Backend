package rs.raf.banka2_bek.account.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.raf.banka2_bek.account.model.Account;
import rs.raf.banka2_bek.account.model.AccountStatus;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClientIdAndStatusOrderByAvailableBalanceDesc(Long clientId, AccountStatus status);

    List<Account> findByClientId(Long clientId);

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a LEFT JOIN a.client c LEFT JOIN a.company co WHERE "
            + "(:ownerName IS NULL OR "
            + "LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :ownerName, '%')) OR "
            + "LOWER(co.name) LIKE LOWER(CONCAT('%', :ownerName, '%')))")
    Page<Account> findAllWithOwnerFilter(@Param("ownerName") String ownerName, Pageable pageable);
}
