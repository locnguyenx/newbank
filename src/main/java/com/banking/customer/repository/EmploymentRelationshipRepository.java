package com.banking.customer.repository;

import com.banking.customer.domain.entity.EmploymentRelationship;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.enums.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmploymentRelationshipRepository extends JpaRepository<EmploymentRelationship, Long> {

    List<EmploymentRelationship> findByEmployeeAndStatus(IndividualCustomer employee, EmploymentStatus status);

    List<EmploymentRelationship> findByEmployerAndStatus(Customer employer, EmploymentStatus status);

    Optional<EmploymentRelationship> findByEmployerAndEmployee(Customer employer, IndividualCustomer employee);
}