package com.banking.charges;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.banking.common.security.mfa.MfaService;
import com.banking.common.audit.AuditLogService;
import com.banking.common.audit.AuditLogRepository;

@SpringBootApplication
@ComponentScan(
    basePackages = {
        "com.banking.charges",
        "com.banking.common.security",
        "com.banking.common.audit"
    }
)
@EntityScan(basePackages = {"com.banking.charges.domain", "com.banking.common.security"})
@EnableJpaRepositories(basePackages = {"com.banking.charges.repository", "com.banking.common.security"})
public class ChargesTestApplication {

    @MockBean
    private MfaService mfaService;

    @MockBean
    private AuditLogService auditLogService;

    @MockBean
    private AuditLogRepository auditLogRepository;
}
