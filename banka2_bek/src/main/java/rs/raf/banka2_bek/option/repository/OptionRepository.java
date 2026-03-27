package rs.raf.banka2_bek.option.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.raf.banka2_bek.option.model.Option;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA repozitorijum za Option entitet.
 *
 * TODO: Sve metode ispod su definisane po Spring Data JPA naming konvenciji
 * i automatski ce generisati SQL upite. Jedina izuzetak je deleteBySettlementDateBefore
 * koja zahteva @Modifying anotaciju jer vrsi brisanje.
 *
 * NAPOMENA: Upiti koji vracaju opcije za odredjenu akciju (stockListingId) ce se
 * najcesce koristiti na frontendu za prikaz "option chain" tabele.
 */
@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    /**
     * TODO: Pronalazi sve opcije (CALL i PUT) za odredjenu akciju.
     * Koristi se u OptionService#getOptionsForStock() za prikaz option chain-a.
     *
     * @param listingId ID Listing entiteta (akcije)
     * @return lista svih opcija vezanih za tu akciju
     */
    List<Option> findByStockListingId(Long listingId);

    /**
     * TODO: Pronalazi opcije za odredjenu akciju i konkretan settlement datum.
     * Koristi se za filtriranje option chain-a po datumu isteka.
     *
     * @param listingId ID Listing entiteta (akcije)
     * @param date      settlement datum za filtriranje
     * @return lista opcija za dati listing i datum
     */
    List<Option> findByStockListingIdAndSettlementDate(Long listingId, LocalDate date);

    /**
     * TODO: Pronalazi sve istekle opcije (settlement datum pre zadatog datuma).
     * Koristi se u OptionScheduler-u za identifikaciju opcija za brisanje.
     *
     * @param date referentni datum (obicno LocalDate.now())
     * @return lista isteklih opcija
     */
    List<Option> findBySettlementDateBefore(LocalDate date);

    /**
     * TODO: Brise sve istekle opcije iz baze.
     * Poziva se iz OptionScheduler-a svakodnevno u 03:00.
     *
     * VAZNO: Mora imati @Modifying jer vrsi DELETE operaciju.
     * Mora se koristiti unutar @Transactional metode.
     *
     * @param date referentni datum — sve opcije sa settlementDate < date ce biti obrisane
     */
    @Modifying
    @Query("DELETE FROM Option o WHERE o.settlementDate < :date")
    void deleteBySettlementDateBefore(@Param("date") LocalDate date);

    /**
     * TODO: Proverava da li vec postoje opcije za datu akciju i settlement datum.
     * Koristi se u OptionGeneratorService da se izbegne dupliranje opcija.
     *
     * @param listingId ID Listing entiteta
     * @param date      settlement datum
     * @return true ako vec postoje opcije za taj par (listing, datum)
     */
    boolean existsByStockListingIdAndSettlementDate(Long listingId, LocalDate date);
}
