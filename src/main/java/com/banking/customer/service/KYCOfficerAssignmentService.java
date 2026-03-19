package com.banking.customer.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KYCOfficerAssignmentService {

    private static final String[] OFFICERS = {
        "OFFICER-001",
        "OFFICER-002",
        "OFFICER-003",
        "OFFICER-004",
        "OFFICER-005"
    };

    private final AtomicInteger currentIndex = new AtomicInteger(0);

    public String assignOfficer() {
        int index = currentIndex.getAndIncrement() % OFFICERS.length;
        return OFFICERS[index];
    }

    public String assignOfficerRoundRobin() {
        return assignOfficer();
    }

    public String assignOfficerRandom() {
        int index = (int) (Math.random() * OFFICERS.length);
        return OFFICERS[index];
    }
}
