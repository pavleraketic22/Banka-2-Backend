package rs.raf.banka2_bek.currency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.banka2_bek.currency.model.Currency;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);
}
