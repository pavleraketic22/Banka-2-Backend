package rs.raf.banka2_bek.option.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.banka2_bek.stock.model.Listing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * JPA entitet za opcije (finansijske derivate).
 *
 * Opcija predstavlja ugovor koji daje kupcu pravo (ali ne i obavezu) da kupi (CALL)
 * ili proda (PUT) odredjenu akciju (stockListing) po unapred definisanoj ceni (strikePrice)
 * do odredjenog datuma (settlementDate).
 *
 * Specifikacija: Celina 3 - Opcije i Black-Scholes
 *
 * TODO: Ticker format specifikacija:
 *   Format: {STOCK_TICKER}{YYMMDD}{C/P}{STRIKE*1000 sa 8 cifara, zero-padded}
 *   Primer: MSFT220404C00180000
 *     - MSFT      = ticker osnovne akcije
 *     - 220404    = settlement date (04. april 2022.)
 *     - C         = CALL opcija (P za PUT)
 *     - 00180000  = strike price * 1000 = 180.000 * 1000 = 180000, padovan na 8 cifara
 *
 * TODO: Cena opcije (price) se racuna Black-Scholes modelom u BlackScholesService.
 *   Ne cuva se rucno - uvek se rekalkulise na osnovu trenutne cene akcije,
 *   strike cene, vremena do isteka, risk-free rate-a i implied volatility-ja.
 */
@Entity
@Table(name = "options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * TODO: ManyToOne relacija ka Listing entitetu (osnovna akcija).
     * - Samo listinzi sa tipom STOCK mogu imati opcije.
     * - fetch = FetchType.LAZY za performanse (opcije se cesto ucitavaju u bulk-u).
     * - JoinColumn: stock_listing_id, nullable = false
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_listing_id", nullable = false)
    private Listing stockListing;

    /**
     * Tip opcije: CALL ili PUT.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionType optionType;

    /**
     * TODO: Strike price - cena po kojoj kupac opcije moze da kupi/proda akciju.
     * - Generise se u OptionGeneratorService: 5 iznad i 5 ispod trenutne cene akcije,
     *   svaki +/-5% od tekuce cene.
     * - precision = 18, scale = 4 (isti format kao Listing.price)
     */
    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal strikePrice;

    /**
     * TODO: Implied Volatility (sigma) - procenjena volatilnost akcije.
     * - Koristi se kao ulaz u Black-Scholes formulu.
     * - Vrednost izmedju 0.0 i 1.0+ (npr. 0.25 = 25% godisnja volatilnost).
     * - Moze se generisati nasumicno u rasponu 0.15 - 0.60 za pocetne podatke,
     *   ili izracunati iz istorijskih cena ako su dostupne.
     */
    @Column(nullable = false)
    private double impliedVolatility;

    /**
     * TODO: Open Interest - ukupan broj otvorenih (neizmirenih) ugovora.
     * - Povecava se kada se otvori nova pozicija.
     * - Smanjuje se kada se opcija izvrsi (exercise) ili istekne.
     * - Podrazumevano 0 za novokreirane opcije.
     */
    @Column(name = "open_interest")
    private int openInterest = 0;

    /**
     * TODO: Settlement Date - datum isteka opcije.
     * - Posle ovog datuma opcija vise ne vazi.
     * - Generise se u OptionGeneratorService:
     *   6 datuma sa razmakom od 6 dana + 3 datuma sa razmakom od 30 dana.
     * - Istekle opcije (settlementDate < danas) se brisu u OptionScheduler-u.
     */
    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    /**
     * TODO: Contract Size - broj akcija po jednom ugovoru opcije.
     * - Standardno 100 akcija po ugovoru.
     * - Koristi se pri izracunavanju ukupne cene pozicije:
     *   totalCost = price * contractSize
     */
    @Column(name = "contract_size")
    private int contractSize = 100;

    /**
     * TODO: Price - premija opcije izracunata Black-Scholes modelom.
     * - Za CALL: S * N(d1) - K * e^(-rT) * N(d2)
     * - Za PUT:  K * e^(-rT) * N(-d2) - S * N(-d1)
     * - Rekalkulise se svakodnevno u OptionScheduler-u.
     * - precision = 18, scale = 4
     */
    @Column(precision = 18, scale = 4)
    private BigDecimal price;

    /**
     * TODO: Ask - najniza cena po kojoj prodavac nudi opciju.
     * - Moze se generisati kao: price * 1.05 (5% iznad teorijske cene)
     * - precision = 18, scale = 4
     */
    @Column(precision = 18, scale = 4)
    private BigDecimal ask;

    /**
     * TODO: Bid - najvisa cena po kojoj kupac nudi opciju.
     * - Moze se generisati kao: price * 0.95 (5% ispod teorijske cene)
     * - precision = 18, scale = 4
     */
    @Column(precision = 18, scale = 4)
    private BigDecimal bid;

    /**
     * TODO: Volume - broj ugovora kojima se trgovalo tokom dana.
     * - Za pocetne podatke generisati nasumicno (100-10000).
     */
    private long volume;

    /**
     * TODO: Ticker - jedinstveni identifikator opcije.
     * Format: {STOCK_TICKER}{YYMMDD}{C/P}{STRIKE*1000 zero-padded na 8 cifara}
     * Primer: AAPL260402C00185000
     *   - AAPL     = ticker akcije
     *   - 260402   = 02. april 2026.
     *   - C        = CALL
     *   - 00185000 = strike 185.000 * 1000 = 185000 padovano na 8
     *
     * Generise se u OptionGeneratorService#generateTicker().
     */
    @Column(nullable = false, unique = true)
    private String ticker;

    /**
     * Datum i vreme kreiranja opcije.
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * TODO: Automatski postaviti createdAt pre prvog upisa u bazu.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
