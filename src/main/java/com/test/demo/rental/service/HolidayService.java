package com.test.demo.rental.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

@Service
public class HolidayService {

    public boolean isHoliday(LocalDate date) {
        return isIndependenceDay(date) || isLaborDay(date);
    }

    private boolean isIndependenceDay(LocalDate date) {
        int day = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        if (date.getMonth() == Month.JULY) {
            if (day == 4 && (dayOfWeek != DayOfWeek.SATURDAY
             && dayOfWeek != DayOfWeek.SUNDAY)) {
                return true;
            }
            if (day == 3 && dayOfWeek == DayOfWeek.FRIDAY) {
                return true;
            }
            if (day == 5 && dayOfWeek == DayOfWeek.MONDAY) {
                return true;
            }
        }
        return false;
    }

    private boolean isLaborDay(LocalDate date) {
        return date.getMonth() == Month.SEPTEMBER
                && date.getDayOfMonth() <= 7
                && date.getDayOfWeek() == DayOfWeek.MONDAY;
    }

    public boolean isWeekday(LocalDate currentDate) {
        return currentDate.getDayOfWeek() != DayOfWeek.SATURDAY
                && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY;
    }
}
