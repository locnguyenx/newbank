package com.banking.customer.repository;

import com.banking.customer.domain.entity.CustomerAuthorization;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.enums.AuthorizationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerAuthorizationRepository extends JpaRepository<CustomerAuthorization, Long> {

    List<CustomerAuthorization> findByCustomerIdAndStatus(Long customerId, AuthorizationStatus status);

    List<CustomerAuthorization> findByAuthorizedPersonAndStatus(IndividualCustomer authorizedPerson, AuthorizationStatus status);
}
