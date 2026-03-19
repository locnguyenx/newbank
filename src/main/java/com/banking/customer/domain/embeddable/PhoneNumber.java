package com.banking.customer.domain.embeddable;

import com.banking.customer.domain.enums.PhoneType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PhoneNumber {

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "phone_number")
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneType type;

    protected PhoneNumber() {
    }

    public PhoneNumber(String countryCode, String number, PhoneType type) {
        this.countryCode = countryCode;
        this.number = number;
        this.type = type;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNumber() {
        return number;
    }

    public PhoneType getType() {
        return type;
    }
}
