package br.edu.infnet.libraryigor.model.services.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LoanUtils {

    public static int calculateDaysInDelay(LocalDate returnDate, LocalDate today) {
         return Integer.parseInt(String.valueOf(ChronoUnit.DAYS.between(returnDate, today)));
    }

    public static double calculateDailyFine(double bookPrice) {
        return bookPrice;
    }
}