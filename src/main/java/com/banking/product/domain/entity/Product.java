package com.banking.product.domain.entity;

import com.banking.product.domain.embeddable.AuditFields;
import com.banking.product.domain.enums.ProductFamily;
import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProductFamily family;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Embedded
    private AuditFields audit;

    protected Product() {
    }

    public Product(String code, String name, ProductFamily family, String description) {
        this.code = code;
        this.name = name;
        this.family = family;
        this.description = description;
        this.audit = new AuditFields();
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductFamily getFamily() {
        return family;
    }

    public void setFamily(ProductFamily family) {
        this.family = family;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AuditFields getAudit() {
        return audit;
    }

    public void setAudit(AuditFields audit) {
        this.audit = audit;
    }
}
