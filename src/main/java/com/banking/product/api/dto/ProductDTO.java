package com.banking.product.api.dto;

public class ProductDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String family;
    private String status;
    private Long version;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String code, String name, String description, String family, String status, Long version) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.family = family;
        this.status = status;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
