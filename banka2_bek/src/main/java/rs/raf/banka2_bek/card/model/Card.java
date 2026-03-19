package rs.raf.banka2_bek.card.model;

import jakarta.persistence.*;
import lombok.*;
import rs.raf.banka2_bek.account.model.Account;
import rs.raf.banka2_bek.client.model.Client;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String cardNumber;

    @Column(nullable = false, length = 30)
    private String cardName;

    @Column(nullable = false, length = 3)
    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, precision = 19, scale = 4)
    @Builder.Default
    private BigDecimal cardLimit = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private CardStatus status = CardStatus.ACTIVE;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate expirationDate;

    // --- Luhn algorithm ---

    public static boolean isValidLuhn(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }

    public static String generateCardNumber() {
        java.util.Random random = new java.util.Random();
        // Visa-style: starts with 4, bank identifier 22200
        String prefix = "422200";
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = prefix.length(); i < 15; i++) {
            sb.append(random.nextInt(10));
        }
        // Calculate Luhn check digit
        String partial = sb.toString();
        int sum = 0;
        boolean alternate = true;
        for (int i = partial.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(partial.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        sb.append(checkDigit);
        return sb.toString();
    }

    public static String generateCvv() {
        return String.format("%03d", new java.util.Random().nextInt(1000));
    }
}
