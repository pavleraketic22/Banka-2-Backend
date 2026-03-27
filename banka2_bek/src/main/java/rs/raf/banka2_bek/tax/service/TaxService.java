package rs.raf.banka2_bek.tax.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.banka2_bek.auth.model.User;
import rs.raf.banka2_bek.auth.repository.UserRepository;
import rs.raf.banka2_bek.employee.model.Employee;
import rs.raf.banka2_bek.employee.repository.EmployeeRepository;
import rs.raf.banka2_bek.order.model.Order;
import rs.raf.banka2_bek.order.model.OrderDirection;
import rs.raf.banka2_bek.order.repository.OrderRepository;
import rs.raf.banka2_bek.tax.dto.TaxRecordDto;
import rs.raf.banka2_bek.tax.model.TaxRecord;
import rs.raf.banka2_bek.tax.repository.TaxRecordRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaxService {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.15"); // 15%

    private final TaxRecordRepository taxRecordRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Vraca filtrirane tax recorde za admin/employee portal.
     */
    public List<TaxRecordDto> getTaxRecords(String name, String userType) {
        List<TaxRecord> records = taxRecordRepository.findByFilters(
                (name != null && !name.isBlank()) ? name : null,
                (userType != null && !userType.isBlank()) ? userType : null
        );
        return records.stream().map(this::toDto).collect(Collectors.toList());
    }

    /**
     * Vraca tax record za konkretnog korisnika (autentifikovanog).
     */
    public TaxRecordDto getMyTaxRecord(String email) {
        // Probaj kao employee
        Optional<Employee> empOpt = employeeRepository.findByEmail(email);
        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            Optional<TaxRecord> record = taxRecordRepository.findByUserIdAndUserType(emp.getId(), "EMPLOYEE");
            return record.map(this::toDto).orElseGet(() -> emptyDto(emp.getId(),
                    emp.getFirstName() + " " + emp.getLastName(), "EMPLOYEE"));
        }

        // Probaj kao client (User entity)
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<TaxRecord> record = taxRecordRepository.findByUserIdAndUserType(user.getId(), "CLIENT");
            return record.map(this::toDto).orElseGet(() -> emptyDto(user.getId(),
                    user.getFirstName() + " " + user.getLastName(), "CLIENT"));
        }

        return emptyDto(0L, "Nepoznat", "CLIENT");
    }

    /**
     * Pokrece obracun poreza za sve korisnike koji imaju ordere.
     * Za svakog korisnika: totalProfit = sum(SELL order profits), taxOwed = 15% * totalProfit (ako > 0).
     */
    @Transactional
    public void calculateTaxForAllUsers() {
        LocalDateTime now = LocalDateTime.now();
        List<Order> allDoneOrders = orderRepository.findAll().stream()
                .filter(Order::isDone)
                .toList();

        // Grupisemo ordere po userId + userRole
        Map<String, List<Order>> grouped = allDoneOrders.stream()
                .collect(Collectors.groupingBy(o -> o.getUserId() + ":" + o.getUserRole()));

        for (Map.Entry<String, List<Order>> entry : grouped.entrySet()) {
            String[] parts = entry.getKey().split(":");
            Long userId = Long.parseLong(parts[0]);
            String userRole = parts[1];
            List<Order> userOrders = entry.getValue();

            // Racunamo profit: SELL orderi su profit, BUY orderi su trosak
            // Pojednostavljeno: profit = SUM(sell_value) - SUM(buy_value)
            BigDecimal sellTotal = BigDecimal.ZERO;
            BigDecimal buyTotal = BigDecimal.ZERO;

            for (Order order : userOrders) {
                BigDecimal orderValue = order.getPricePerUnit()
                        .multiply(BigDecimal.valueOf(order.getQuantity()))
                        .multiply(BigDecimal.valueOf(order.getContractSize()));

                if (order.getDirection() == OrderDirection.SELL) {
                    sellTotal = sellTotal.add(orderValue);
                } else {
                    buyTotal = buyTotal.add(orderValue);
                }
            }

            BigDecimal totalProfit = sellTotal.subtract(buyTotal);
            BigDecimal taxOwed = totalProfit.compareTo(BigDecimal.ZERO) > 0
                    ? totalProfit.multiply(TAX_RATE).setScale(4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            // Pronadji ime korisnika
            String userName = resolveUserName(userId, userRole);

            // Kreiraj ili azuriraj tax record
            String userType = "EMPLOYEE".equals(userRole) ? "EMPLOYEE" : "CLIENT";
            TaxRecord record = taxRecordRepository.findByUserIdAndUserType(userId, userType)
                    .orElse(TaxRecord.builder()
                            .userId(userId)
                            .userType(userType)
                            .currency("RSD")
                            .taxPaid(BigDecimal.ZERO)
                            .build());

            record.setUserName(userName);
            record.setTotalProfit(totalProfit);
            record.setTaxOwed(taxOwed);
            record.setCalculatedAt(now);

            taxRecordRepository.save(record);
        }
    }

    private String resolveUserName(Long userId, String userRole) {
        if ("EMPLOYEE".equals(userRole)) {
            return employeeRepository.findById(userId)
                    .map(e -> e.getFirstName() + " " + e.getLastName())
                    .orElse("Zaposleni #" + userId);
        }
        return userRepository.findById(userId)
                .map(u -> u.getFirstName() + " " + u.getLastName())
                .orElse("Klijent #" + userId);
    }

    private TaxRecordDto toDto(TaxRecord record) {
        return new TaxRecordDto(
                record.getId(),
                record.getUserId(),
                record.getUserName(),
                record.getUserType(),
                record.getTotalProfit(),
                record.getTaxOwed(),
                record.getTaxPaid(),
                record.getCurrency()
        );
    }

    private TaxRecordDto emptyDto(Long userId, String userName, String userType) {
        return new TaxRecordDto(null, userId, userName, userType,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "RSD");
    }
}
