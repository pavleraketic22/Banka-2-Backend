package rs.raf.banka2_bek.berza.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.banka2_bek.berza.dto.ExchangeDto;
import rs.raf.banka2_bek.berza.model.Exchange;
import rs.raf.banka2_bek.berza.repository.ExchangeRepository;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Servis za upravljanje berzama i proveru radnog vremena.
 *
 * Specifikacija: Celina 3 - Berza
 */
@Service
@RequiredArgsConstructor
public class ExchangeManagementService {

    private final ExchangeRepository exchangeRepository;

    /**
     * Proverava da li je berza trenutno otvorena.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci berzu po acronym-u, baciti exception ako ne postoji
     *   2. Ako je testMode=true, odmah vratiti true
     *   3. Odrediti trenutno vreme u timezone-u berze (ZoneId.of(exchange.getTimeZone()))
     *   4. Proveriti da li je danas radni dan (ponedeljak-petak)
     *   5. Proveriti da li je trenutno vreme izmedju openTime i closeTime
     *   6. TODO (buducnost): Proveriti holiday calendar — za sada ignorisati praznike
     *
     * @param acronym skraceni naziv berze (npr. "NYSE")
     * @return true ako je berza otvorena za trgovanje
     */
    public boolean isExchangeOpen(String acronym) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("isExchangeOpen nije implementiran");
    }

    /**
     * Vraca listu svih aktivnih berzi sa computed poljima (isCurrentlyOpen, currentLocalTime, nextOpenTime).
     *
     * TODO: Implementirati logiku:
     *   1. Dohvatiti sve aktivne berze iz repozitorijuma (findByActiveTrue)
     *   2. Za svaku berzu mapirati u ExchangeDto
     *   3. Izracunati computed polja:
     *      - isCurrentlyOpen: pozivom isExchangeOpen(acronym)
     *      - currentLocalTime: ZonedDateTime.now(ZoneId.of(tz)).toLocalTime().toString()
     *      - nextOpenTime: ako je zatvorena, izracunati sledeci radni dan + openTime
     *
     * @return lista ExchangeDto objekata
     */
    public List<ExchangeDto> getAllExchanges() {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("getAllExchanges nije implementiran");
    }

    /**
     * Vraca detalje jedne berze po skracenici.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci berzu po acronym-u
     *   2. Baciti RuntimeException ako ne postoji
     *   3. Mapirati u ExchangeDto sa computed poljima
     *
     * @param acronym skraceni naziv berze
     * @return ExchangeDto sa svim podacima i statusom
     */
    public ExchangeDto getByAcronym(String acronym) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("getByAcronym nije implementiran");
    }

    /**
     * Ukljucuje/iskljucuje test mode za berzu.
     * Samo admin moze da pozove ovu metodu.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci berzu po acronym-u
     *   2. Postaviti testMode na zadatu vrednost
     *   3. Sacuvati izmene
     *   4. Logirati promenu (slf4j)
     *
     * @param acronym skraceni naziv berze
     * @param enabled true za ukljucivanje, false za iskljucivanje test moda
     */
    @Transactional
    public void setTestMode(String acronym, boolean enabled) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("setTestMode nije implementiran");
    }

    /**
     * Proverava da li je berza u after-hours periodu.
     * After-hours = posle closeTime ali pre postMarketCloseTime.
     *
     * TODO: Implementirati logiku:
     *   1. Pronaci berzu po acronym-u
     *   2. Ako postMarketCloseTime == null, vratiti false (berza nema post-market)
     *   3. Odrediti trenutno vreme u timezone-u berze
     *   4. Proveriti: closeTime < currentTime < postMarketCloseTime
     *
     * @param acronym skraceni naziv berze
     * @return true ako je berza u after-hours periodu
     */
    public boolean isAfterHours(String acronym) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("isAfterHours nije implementiran");
    }

    // ── Helper metode ───────────────────────────────────────────────────────────

    /**
     * TODO: Mapira Exchange entitet u ExchangeDto.
     * Treba da popuni sva polja ukljucujuci computed (isCurrentlyOpen, currentLocalTime, nextOpenTime).
     */
    private ExchangeDto toDto(Exchange exchange) {
        // TODO: Implementirati mapiranje
        throw new UnsupportedOperationException("toDto nije implementiran");
    }
}
