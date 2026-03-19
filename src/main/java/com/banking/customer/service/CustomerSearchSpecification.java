package com.banking.customer.service;

import com.banking.customer.domain.entity.Customer;
import com.banking.customer.dto.CustomerSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class CustomerSearchSpecification implements Specification<Customer> {

    private final CustomerSearchCriteria criteria;

    public CustomerSearchSpecification(CustomerSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(jakarta.persistence.criteria.Root<Customer> root,
                                  jakarta.persistence.criteria.CriteriaQuery<?> query,
                                  jakarta.persistence.criteria.CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getName() != null && !criteria.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), 
                "%" + criteria.getName().toLowerCase() + "%"));
        }

        if (criteria.getCustomerNumber() != null && !criteria.getCustomerNumber().isBlank()) {
            predicates.add(cb.equal(root.get("customerNumber"), criteria.getCustomerNumber()));
        }

        if (criteria.getTaxId() != null && !criteria.getTaxId().isBlank()) {
            predicates.add(cb.equal(root.get("taxId"), criteria.getTaxId()));
        }

        if (criteria.getType() != null) {
            predicates.add(cb.equal(root.get("type"), criteria.getType()));
        }

        if (criteria.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
