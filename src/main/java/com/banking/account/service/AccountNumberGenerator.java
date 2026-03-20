package com.banking.account.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountNumberGenerator {

    private static final String PREFIX = "ACC";
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final int COUNTER_DIGITS = 6;
    private static final int MAX_COUNTER = 999999;

    private final AtomicInteger dailyCounter = new AtomicInteger(0);
    private volatile LocalDate currentDate = LocalDate.now();

    public String generate() {
        LocalDate today = LocalDate.now();
        
        // Reset counter if date changed
        if (!today.equals(currentDate)) {
            synchronized (this) {
                if (!today.equals(currentDate)) {
                    dailyCounter.set(0);
                    currentDate = today;
                }
            }
        }

        int counter = dailyCounter.incrementAndGet();
        if (counter > MAX_COUNTER) {
            throw new IllegalStateException("Daily account number limit exceeded");
        }

        String datePart = today.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String counterPart = String.format("%0" + COUNTER_DIGITS + "d", counter);
        
        return PREFIX + "-" + datePart + "-" + counterPart;
    }
}
