package rs.raf.banka2_bek.account.util;

import rs.raf.banka2_bek.account.model.AccountSubtype;
import rs.raf.banka2_bek.account.model.AccountType;

import java.util.Random;

public final class AccountNumberUtils {

    private static final String BANK_CODE = "222";
    private static final String BRANCH_CODE = "0001";
    private static final Random RANDOM = new Random();

    private AccountNumberUtils() {}

    public static String generate(AccountType type, AccountSubtype subtype, boolean isBusiness) {
        String typeDigits = determineTypeDigits(type, subtype, isBusiness);
        while (true) {
            String randomPart = String.format("%09d", RANDOM.nextInt(1_000_000_000));
            String candidate = BANK_CODE + BRANCH_CODE + randomPart + typeDigits;
            if (isValidMod11(candidate)) {
                return candidate;
            }
        }
    }

    private static String determineTypeDigits(AccountType type, AccountSubtype subtype, boolean isBusiness) {
        if (type == AccountType.FOREIGN) {
            return isBusiness ? "22" : "21";
        }
        if (type == AccountType.BUSINESS || isBusiness) {
            return "12";
        }
        if (type == AccountType.CHECKING && subtype != null) {
            return switch (subtype) {
                case PERSONAL -> "11";
                case SAVINGS -> "13";
                case PENSION -> "14";
                case YOUTH -> "15";
                case STUDENT -> "16";
                case UNEMPLOYED -> "17";
                default -> "10";
            };
        }
        return "10";
    }

    private static boolean isValidMod11(String accountNumber) {
        int sum = 0;
        for (char c : accountNumber.toCharArray()) {
            sum += Character.getNumericValue(c);
        }
        return sum % 11 == 0;
    }
}
