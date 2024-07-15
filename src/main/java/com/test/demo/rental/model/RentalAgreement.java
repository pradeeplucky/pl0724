package com.test.demo.rental.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.test.demo.rental.util.serializer.CurrencySerializer;
import com.test.demo.rental.util.serializer.LocalDateSerializer;
import com.test.demo.rental.util.serializer.PercentSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalAgreement {
    private Tool tool;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal dailyCharge;
    private int chargeDays;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal preDiscountCharge;
    @JsonSerialize(using = PercentSerializer.class)
    private int discountPercent;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal discountAmount;
    @JsonSerialize(using = CurrencySerializer.class)
    private BigDecimal finalCharge;

    public RentalAgreement(Tool tool, int rentalDays, LocalDate checkoutDate, LocalDate dueDate, BigDecimal dailyCharge, int chargeDays, BigDecimal preDiscountCharge, int discountPercent, BigDecimal discountAmount, BigDecimal finalCharge) {
        this.tool = tool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.dailyCharge = dailyCharge;
        this.chargeDays = chargeDays;
        this.preDiscountCharge = preDiscountCharge;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.finalCharge = finalCharge;
    }

    public Tool getTool() {
        return tool;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public int getChargeDays() {
        return chargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    public void printDetails() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(module);
        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
        try {
            String json = writer.writeValueAsString(this);
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}