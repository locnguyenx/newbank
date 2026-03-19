package com.banking.customer.repository;

import com.banking.customer.domain.entity.KYCDocument;
import com.banking.customer.domain.entity.KYCCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KYCDocumentRepository extends JpaRepository<KYCDocument, Long> {

    List<KYCDocument> findByKycCheck(KYCCheck kycCheck);
}
