package com.test.demo.rental.service;

import com.test.demo.rental.model.RentalAgreement;
import com.test.demo.rental.repository.ToolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CheckoutServiceIntegrationTest {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ToolRepository toolRepository;

    @Test
    public void testInvalidDiscount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                checkoutService.toolCheckOut("JAKR", 5, 101, LocalDate.of(2015, 9, 3))
        );
        assertEquals("Discount percent must be in the range 0-100", exception.getMessage());
    }

    @Test
    public void testValidCheckoutLadder() {
        RentalAgreement agreement = checkoutService.toolCheckOut("LADW", 3,
                10, LocalDate.of(2020, 7, 2));
        assertNotNull(agreement);
        assertEquals("LADW", agreement.getTool().getToolCode());
        assertEquals("Ladder", agreement.getTool().getToolType().getName());
        assertEquals("Werner", agreement.getTool().getBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 5), agreement.getDueDate());
        assertEquals(2, agreement.getChargeDays());
        assertEquals(new BigDecimal("3.98"), agreement.getPreDiscountCharge());
        assertEquals(10, agreement.getDiscountPercent());
        assertEquals(new BigDecimal("0.398"), agreement.getDiscountAmount());
        assertEquals(new BigDecimal("3.582"), agreement.getFinalCharge());
        agreement.printDetails();
    }

    @Test
    public void testValidCheckoutChainsaw() {
        RentalAgreement agreement = checkoutService.toolCheckOut("CHNS", 5, 25,
                LocalDate.of(2015, 7, 2));
        assertNotNull(agreement);
        assertEquals("CHNS", agreement.getTool().getToolCode());
        assertEquals("Chainsaw", agreement.getTool().getToolType().getName());
        assertEquals("Stihl", agreement.getTool().getBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 7), agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(new BigDecimal("4.47"), agreement.getPreDiscountCharge());
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(new BigDecimal("1.1175"), agreement.getDiscountAmount());
        assertEquals(new BigDecimal("3.3525"), agreement.getFinalCharge());
        agreement.printDetails();
    }

    @Test
    public void testValidCheckoutJackhammerNoDiscount() {
        RentalAgreement agreement = checkoutService.toolCheckOut("JAKD", 6, 0,
                LocalDate.of(2015, 9, 3));
        assertNotNull(agreement);
        assertEquals("JAKD", agreement.getTool().getToolCode());
        assertEquals("Jackhammer", agreement.getTool().getToolType().getName());
        assertEquals("DeWalt", agreement.getTool().getBrand());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 9, 3), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 9, 9), agreement.getDueDate());
        assertEquals(3, agreement.getChargeDays());
        assertEquals(new BigDecimal("8.97"), agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(new BigDecimal("0.00"), agreement.getDiscountAmount());
        assertEquals(new BigDecimal("8.97"), agreement.getFinalCharge());
    }

    @Test
    public void testValidCheckoutJackhammerLongRental() {
        RentalAgreement agreement = checkoutService.toolCheckOut("JAKR", 9, 0,
                LocalDate.of(2015, 7, 2));
        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getTool().getToolCode());
        assertEquals("Jackhammer", agreement.getTool().getToolType().getName());
        assertEquals("Ridgid", agreement.getTool().getBrand());
        assertEquals(9, agreement.getRentalDays());
        assertEquals(LocalDate.of(2015, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2015, 7, 11), agreement.getDueDate());
        assertEquals(5, agreement.getChargeDays());
        assertEquals(new BigDecimal("14.95"), agreement.getPreDiscountCharge());
        assertEquals(0, agreement.getDiscountPercent());
        assertEquals(new BigDecimal("0.00").setScale(2), agreement.getDiscountAmount());
        assertEquals(new BigDecimal("14.95").setScale(2), agreement.getFinalCharge());
    }

    @Test
    public void testValidCheckoutJackhammerWithDiscount() {
        RentalAgreement agreement = checkoutService.toolCheckOut("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getTool().getToolCode());
        assertEquals("Jackhammer", agreement.getTool().getToolType().getName());
        assertEquals("Ridgid", agreement.getTool().getBrand());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(LocalDate.of(2020, 7, 2), agreement.getCheckoutDate());
        assertEquals(LocalDate.of(2020, 7, 6), agreement.getDueDate());
        assertEquals(1, agreement.getChargeDays()); // 2 chargeable days due to holiday and weekend
        assertEquals(new BigDecimal("2.99"), agreement.getPreDiscountCharge());
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(new BigDecimal("1.495"), agreement.getDiscountAmount());
        assertEquals(new BigDecimal("1.495"), agreement.getFinalCharge());
    }
}
