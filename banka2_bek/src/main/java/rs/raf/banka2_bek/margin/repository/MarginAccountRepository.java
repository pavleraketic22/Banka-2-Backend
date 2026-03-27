package rs.raf.banka2_bek.margin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.banka2_bek.margin.model.MarginAccount;
import rs.raf.banka2_bek.margin.model.MarginAccountStatus;

import java.util.List;

@Repository
public interface MarginAccountRepository extends JpaRepository<MarginAccount, Long> {

    /** Pronalazi sve margin racune za datog korisnika */
    List<MarginAccount> findByUserId(Long userId);

    /** Pronalazi sve margin racune sa datim statusom */
    List<MarginAccount> findByStatus(MarginAccountStatus status);

    /** Pronalazi margin racun vezan za dati obicni racun */
    List<MarginAccount> findByAccountId(Long accountId);
}
