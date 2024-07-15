package com.test.demo.rental.service;

import com.test.demo.rental.model.RentalAgreement;
import com.test.demo.rental.model.Tool;
import com.test.demo.rental.model.ToolType;
import com.test.demo.rental.repository.ToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.Supplier;

@Service
public class CheckoutService {

    private final ToolRepository toolRepository;
    private final HolidayService holidayService;

    @Autowired
    public CheckoutService(ToolRepository toolRepository, HolidayService holidayService) {
        this.toolRepository = toolRepository;
        this.holidayService = holidayService;
    }

    public RentalAgreement toolCheckOut(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        validateCheckout(rentalDays, discountPercent);

        Tool tool = toolRepository.getToolByCode(toolCode)
                .orElseThrow(invalidToolCode(toolCode));
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);

        int chargeDays = calculateChargeDays(tool.getToolType(), checkoutDate, dueDate);

        BigDecimal dailyCharge = tool.getToolType().getDailyCharge();
        BigDecimal preDiscountCharge = dailyCharge.multiply(BigDecimal.valueOf(chargeDays));
        BigDecimal discountAmount = preDiscountCharge
                .multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100));

        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        return new RentalAgreement(
                tool,
                rentalDays,
                checkoutDate,
                dueDate,
                tool.getToolType().getDailyCharge(),
                chargeDays,
                preDiscountCharge,
                discountPercent,
                discountAmount,
                finalCharge
        );
    }

    private Supplier<IllegalArgumentException> invalidToolCode(String toolCode) {
        return () -> new IllegalArgumentException("Invalid tool code: " + toolCode);
    }

    private void validateCheckout(int rentalDays, int discountPercent) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater");
        }

        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be in the range 0-100");
        }
    }

    private int calculateChargeDays(ToolType toolType, LocalDate start, LocalDate end) {
        int chargeDays = 0;
        LocalDate currentDate = start.plusDays(1);
 
        while (!currentDate.isAfter(end)) {
            boolean isWeekday = holidayService.isWeekday(currentDate);
            boolean isHoliday = holidayService.isHoliday(currentDate);
            if ((isWeekday && toolType.isWeekdayCharge() && !isHoliday)
                    || (!isWeekday && !isHoliday && toolType.isWeekendCharge())
                    || (isHoliday && toolType.isHolidayCharge())) {
                chargeDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return chargeDays;
    }
}
