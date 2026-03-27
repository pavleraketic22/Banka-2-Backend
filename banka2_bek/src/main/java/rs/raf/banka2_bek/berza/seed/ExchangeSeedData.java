package rs.raf.banka2_bek.berza.seed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import rs.raf.banka2_bek.berza.model.Exchange;
import rs.raf.banka2_bek.berza.repository.ExchangeRepository;

import java.time.LocalTime;

/**
 * Seed komponenta koja popunjava tabelu berzi pri pokretanju aplikacije.
 *
 * Pokrece se samo ako je tabela prazna (count == 0).
 * Dodaje 4 berze: NYSE, NASDAQ, LSE, BELEX.
 *
 * TODO: Implementirati logiku:
 *   1. Proveriti da li tabela vec ima podatke (exchangeRepository.count() > 0)
 *   2. Ako ima, samo logirati "Exchange seed data already exists, skipping." i izaci
 *   3. Ako je prazna, kreirati 4 Exchange entiteta sa sledecim podacima:
 *
 *      NYSE:
 *        name = "New York Stock Exchange"
 *        acronym = "NYSE"
 *        micCode = "XNYS"
 *        country = "US"
 *        currency = "USD"
 *        timeZone = "America/New_York"
 *        openTime = 09:30
 *        closeTime = 16:00
 *        preMarketOpenTime = 04:00
 *        postMarketCloseTime = 20:00
 *
 *      NASDAQ:
 *        name = "NASDAQ"
 *        acronym = "NASDAQ"
 *        micCode = "XNAS"
 *        country = "US"
 *        currency = "USD"
 *        timeZone = "America/New_York"
 *        openTime = 09:30
 *        closeTime = 16:00
 *        preMarketOpenTime = 04:00
 *        postMarketCloseTime = 20:00
 *
 *      LSE:
 *        name = "London Stock Exchange"
 *        acronym = "LSE"
 *        micCode = "XLON"
 *        country = "GB"
 *        currency = "GBP"
 *        timeZone = "Europe/London"
 *        openTime = 08:00
 *        closeTime = 16:30
 *        preMarketOpenTime = null
 *        postMarketCloseTime = null
 *
 *      BELEX:
 *        name = "Belgrade Stock Exchange"
 *        acronym = "BELEX"
 *        micCode = "XBEL"
 *        country = "RS"
 *        currency = "RSD"
 *        timeZone = "Europe/Belgrade"
 *        openTime = 09:00
 *        closeTime = 15:00
 *        preMarketOpenTime = null
 *        postMarketCloseTime = null
 *
 *   4. Sacuvati sve sa exchangeRepository.saveAll(list)
 *   5. Logirati "Seeded {} exchanges." sa brojem unetih
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExchangeSeedData implements ApplicationRunner {

    private final ExchangeRepository exchangeRepository;

    @Override
    public void run(ApplicationArguments args) {
        // TODO: Implementirati prema uputstvima iznad
        throw new UnsupportedOperationException("ExchangeSeedData.run() nije implementiran");
    }
}
