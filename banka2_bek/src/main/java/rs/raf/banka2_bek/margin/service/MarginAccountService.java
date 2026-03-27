package rs.raf.banka2_bek.margin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.banka2_bek.account.repository.AccountRepository;
import rs.raf.banka2_bek.margin.dto.CreateMarginAccountDto;
import rs.raf.banka2_bek.margin.dto.MarginAccountDto;
import rs.raf.banka2_bek.margin.dto.MarginTransactionDto;
import rs.raf.banka2_bek.margin.model.MarginAccount;
import rs.raf.banka2_bek.margin.model.MarginAccountStatus;
import rs.raf.banka2_bek.margin.model.MarginTransaction;
import rs.raf.banka2_bek.margin.model.MarginTransactionType;
import rs.raf.banka2_bek.margin.repository.MarginAccountRepository;
import rs.raf.banka2_bek.margin.repository.MarginTransactionRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servis za upravljanje margin racunima.
 *
 * Specifikacija: Celina 3 - Margin racuni
 *
 * Kljucne formule:
 *   initialMargin     = deposit / (1 - bankParticipation)
 *   loanValue          = initialMargin - deposit
 *   maintenanceMargin  = initialMargin * 0.5  (za akcije)
 *
 * Margin call: ako initialMargin padne ispod maintenanceMargin, racun se blokira.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MarginAccountService {

    private final MarginAccountRepository marginAccountRepository;
    private final MarginTransactionRepository marginTransactionRepository;
    private final AccountRepository accountRepository;

    /** Podrazumevani procenat ucestva banke (50%) */
    private static final BigDecimal DEFAULT_BANK_PARTICIPATION = new BigDecimal("0.50");

    /** Faktor za izracunavanje maintenance margine (50% od initial za akcije) */
    private static final BigDecimal MAINTENANCE_FACTOR = new BigDecimal("0.50");

    /**
     * Kreira novi margin racun za korisnika.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci Account po accountId, baciti exception ako ne postoji
     *   2. Proveriti da li korisnik (userId) ima pristup tom racunu
     *   3. Izracunati:
     *      - bankParticipation = DEFAULT_BANK_PARTICIPATION (0.50)
     *      - initialMargin = initialDeposit / (1 - bankParticipation)
     *        Npr. deposit=5000, bank=50% → initialMargin = 5000 / 0.5 = 10000
     *      - loanValue = initialMargin - initialDeposit
     *        Npr. loanValue = 10000 - 5000 = 5000
     *      - maintenanceMargin = initialMargin * MAINTENANCE_FACTOR
     *        Npr. maintenanceMargin = 10000 * 0.5 = 5000
     *   4. Kreirati MarginAccount entitet sa svim izracunatim vrednostima
     *   5. Sacuvati u bazu
     *   6. Kreirati DEPOSIT MarginTransaction za pocetni depozit
     *   7. Mapirati u MarginAccountDto i vratiti
     *
     * @param userId ID korisnika koji kreira margin racun
     * @param dto DTO sa accountId i initialDeposit
     * @return kreiran MarginAccountDto
     */
    @Transactional
    public MarginAccountDto createForUser(Long userId, CreateMarginAccountDto dto) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("createForUser nije implementiran");
    }

    /**
     * Vraca sve margin racune za autentifikovanog korisnika.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci korisnika po email-u (User ili Employee)
     *   2. Dohvatiti sve margin racune za tog korisnika (findByUserId)
     *   3. Mapirati u listu MarginAccountDto
     *
     * @param email email autentifikovanog korisnika
     * @return lista margin racuna
     */
    public List<MarginAccountDto> getMyMarginAccounts(String email) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("getMyMarginAccounts nije implementiran");
    }

    /**
     * Uplata sredstava na margin racun.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci MarginAccount po ID-ju, baciti exception ako ne postoji
     *   2. Dodati amount na initialMargin
     *   3. Preracunati maintenanceMargin = initialMargin * MAINTENANCE_FACTOR
     *   4. Ako je racun bio BLOCKED i sada initialMargin >= maintenanceMargin:
     *      - Promeniti status na ACTIVE
     *      - Logirati "Margin account {} unblocked after deposit of {}"
     *   5. Sacuvati MarginAccount
     *   6. Kreirati DEPOSIT MarginTransaction
     *   7. Sacuvati MarginTransaction
     *
     * @param marginAccountId ID margin racuna
     * @param amount iznos za uplatu
     */
    @Transactional
    public void deposit(Long marginAccountId, BigDecimal amount) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("deposit nije implementiran");
    }

    /**
     * Isplata sredstava sa margin racuna.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci MarginAccount po ID-ju, baciti exception ako ne postoji
     *   2. Proveriti da je racun ACTIVE (blokirani racuni ne dozvoljavaju isplate)
     *   3. Proveriti: initialMargin - amount >= maintenanceMargin
     *      - Ako nije, baciti exception "Isplata bi smanjila marginu ispod maintenance nivoa"
     *   4. Smanjiti initialMargin za amount
     *   5. Sacuvati MarginAccount
     *   6. Kreirati WITHDRAWAL MarginTransaction
     *   7. Sacuvati MarginTransaction
     *
     * @param marginAccountId ID margin racuna
     * @param amount iznos za isplatu
     */
    @Transactional
    public void withdraw(Long marginAccountId, BigDecimal amount) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("withdraw nije implementiran");
    }

    /**
     * Dnevna provera maintenance margine za sve aktivne margin racune.
     * Pokrece se automatski svaki dan u ponoc.
     *
     * TODO: Implementirati logiku:
     *   1. Dohvatiti sve margin racune sa statusom ACTIVE
     *   2. Za svaki racun proveriti: da li je initialMargin < maintenanceMargin?
     *   3. Ako jeste — margin call:
     *      - Postaviti status na BLOCKED
     *      - Sacuvati racun
     *      - Logirati "MARGIN CALL: Account {} blocked. initialMargin={}, maintenanceMargin={}"
     *   4. Na kraju logirati ukupan broj blokiranih racuna
     *   5. TODO (buducnost): Poslati email notifikaciju korisniku o margin call-u
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkMaintenanceMargin() {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("checkMaintenanceMargin nije implementiran");
    }

    /**
     * Vraca istoriju transakcija za dati margin racun.
     *
     * TODO: Implementirati logiku:
     *   1. Proveriti da margin racun postoji
     *   2. Dohvatiti sve transakcije (findByMarginAccountIdOrderByCreatedAtDesc)
     *   3. Mapirati u listu MarginTransactionDto
     *
     * @param marginAccountId ID margin racuna
     * @return lista transakcija sortirana od najnovije
     */
    public List<MarginTransactionDto> getTransactions(Long marginAccountId) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("getTransactions nije implementiran");
    }

    // ── Helper metode ───────────────────────────────────────────────────────────

    /**
     * TODO: Mapira MarginAccount entitet u MarginAccountDto.
     */
    private MarginAccountDto toDto(MarginAccount marginAccount) {
        // TODO: Implementirati mapiranje
        throw new UnsupportedOperationException("toDto(MarginAccount) nije implementiran");
    }

    /**
     * TODO: Mapira MarginTransaction entitet u MarginTransactionDto.
     */
    private MarginTransactionDto toDto(MarginTransaction transaction) {
        // TODO: Implementirati mapiranje
        throw new UnsupportedOperationException("toDto(MarginTransaction) nije implementiran");
    }
}
