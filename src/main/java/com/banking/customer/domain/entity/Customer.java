package com.banking.customer.domain.entity;

import com.banking.customer.domain.embeddable.Address;
import com.banking.customer.domain.embeddable.AuditFields;
import jakarta.persistence.Embedded;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.banking.customer.domain.embeddable.PhoneNumber;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.CustomerType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "customer_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_number", unique = true, nullable = false)
    private String customerNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, insertable = false, updatable = false)
    private CustomerType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CustomerStatus status;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tax_id")
    private String taxId;

    @ElementCollection
    @CollectionTable(name = "customer_addresses", joinColumns = @JoinColumn(name = "customer_id"))
    private List<Address> addresses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "customer_phones", joinColumns = @JoinColumn(name = "customer_id"))
    private List<PhoneNumber> phones = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "customer_emails", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "email")
    private List<String> emails = new ArrayList<>();

    @Embedded
    private AuditFields auditFields;

    protected Customer() {
    }

    protected Customer(String customerNumber, String name, CustomerStatus status) {
        this.customerNumber = customerNumber;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public CustomerType getType() {
        return type;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public List<PhoneNumber> getPhones() {
        return phones;
    }

    public void addPhone(PhoneNumber phone) {
        this.phones.add(phone);
    }

    public List<String> getEmails() {
        return emails;
    }

    public void addEmail(String email) {
        this.emails.add(email);
    }

    public AuditFields getAuditFields() {
        return auditFields;
    }

    @PrePersist
    protected void onCreate() {
        if (auditFields == null) {
            auditFields = new AuditFields("SYSTEM");
        }
        auditFields.setUpdatedBy("SYSTEM");
    }

    @PreUpdate
    protected void onUpdate() {
        if (auditFields != null) {
            auditFields.setUpdatedBy("SYSTEM");
        }
    }
}
