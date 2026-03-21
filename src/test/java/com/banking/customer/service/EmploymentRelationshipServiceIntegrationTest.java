package com.banking.customer.service;

import com.banking.customer.domain.entity.CorporateCustomer;
import com.banking.customer.domain.entity.Customer;
import com.banking.customer.domain.entity.EmploymentRelationship;
import com.banking.customer.domain.entity.IndividualCustomer;
import com.banking.customer.domain.entity.SMECustomer;
import com.banking.customer.domain.enums.CustomerStatus;
import com.banking.customer.domain.enums.EmploymentStatus;
import com.banking.customer.dto.BulkUploadResult;
import com.banking.customer.repository.CustomerRepository;
import com.banking.customer.repository.EmploymentRelationshipRepository;
import com.banking.masterdata.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EmploymentRelationshipServiceIntegrationTest {

    @MockBean
    private CurrencyRepository currencyRepository;

    @Autowired
    private EmploymentRelationshipService employmentRelationshipService;

    @Autowired
    private EmploymentRelationshipRepository employmentRelationshipRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private IndividualCustomer testEmployee;
    private CorporateCustomer testCorporateEmployer;
    private SMECustomer testSMEEmployer;

    @BeforeEach
    void setUp() {
        employmentRelationshipRepository.deleteAll();
        customerRepository.deleteAll();

        testEmployee = new IndividualCustomer("EMP-001", "John Doe", CustomerStatus.ACTIVE);
        testEmployee.setTaxId("EMP-T001");
        testEmployee = customerRepository.save(testEmployee);

        testCorporateEmployer = new CorporateCustomer("CORP-001", "Acme Corp", CustomerStatus.ACTIVE);
        testCorporateEmployer.setTaxId("CORP-T001");
        testCorporateEmployer = (CorporateCustomer) customerRepository.save(testCorporateEmployer);

        testSMEEmployer = new SMECustomer("SME-001", "Small Business", CustomerStatus.ACTIVE);
        testSMEEmployer.setTaxId("SME-T001");
        testSMEEmployer = (SMECustomer) customerRepository.save(testSMEEmployer);
    }

    @Test
    void shouldCreateEmploymentRelationshipWithCorporateEmployer() {
        EmploymentRelationship details = createEmploymentDetails("Engineering", "Developer", LocalDate.of(2024, 1, 15));

        EmploymentRelationship result = employmentRelationshipService.createEmployment(
            testEmployee, testCorporateEmployer, details
        );

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testEmployee.getId(), result.getEmployee().getId());
        assertEquals(testCorporateEmployer.getId(), result.getEmployer().getId());
        assertEquals("Engineering", result.getDepartment());
        assertEquals("Developer", result.getPosition());
        assertEquals(LocalDate.of(2024, 1, 15), result.getStartDate());
        assertEquals(EmploymentStatus.ACTIVE, result.getStatus());
        assertNull(result.getEndDate());
    }

    @Test
    void shouldCreateEmploymentRelationshipWithSMEEmployer() {
        EmploymentRelationship details = createEmploymentDetails("Sales", "Manager", LocalDate.of(2024, 2, 1));

        EmploymentRelationship result = employmentRelationshipService.createEmployment(
            testEmployee, testSMEEmployer, details
        );

        assertNotNull(result);
        assertEquals(testSMEEmployer.getId(), result.getEmployer().getId());
        assertEquals(EmploymentStatus.ACTIVE, result.getStatus());
    }

    @Test
    void shouldTerminateEmploymentRelationship() {
        EmploymentRelationship employment = createAndSaveEmployment(testEmployee, testCorporateEmployer);

        EmploymentRelationship result = employmentRelationshipService.terminateEmployment(employment.getId());

        assertEquals(EmploymentStatus.TERMINATED, result.getStatus());
        assertNotNull(result.getEndDate());
        assertEquals(LocalDate.now(), result.getEndDate());
    }

    @Test
    void shouldFindActiveEmploymentsForEmployee() {
        createAndSaveEmployment(testEmployee, testCorporateEmployer);
        createAndSaveEmployment(testEmployee, testSMEEmployer);

        List<EmploymentRelationship> activeEmployments = employmentRelationshipRepository
            .findByEmployeeAndStatus(testEmployee, EmploymentStatus.ACTIVE);

        assertEquals(2, activeEmployments.size());
    }

    @Test
    void shouldFindActiveEmploymentsForEmployer() {
        IndividualCustomer employee2 = createAndSaveIndividualCustomer("EMP-002", "Jane Doe");
        createAndSaveEmployment(testEmployee, testCorporateEmployer);
        createAndSaveEmployment(employee2, testCorporateEmployer);

        List<EmploymentRelationship> employerEmployments = employmentRelationshipRepository
            .findByEmployerAndStatus(testCorporateEmployer, EmploymentStatus.ACTIVE);

        assertEquals(2, employerEmployments.size());
    }

    @Test
    void shouldBulkUploadEmployeesSuccessfully() {
        IndividualCustomer emp2 = createAndSaveIndividualCustomer("EMP-002", "Jane Smith");
        IndividualCustomer emp3 = createAndSaveIndividualCustomer("EMP-003", "Bob Johnson");

        String csvContent = """
            employeeNumber,department,position,startDate
            EMP-001,Engineering,Developer,2024-01-15
            EMP-002,Sales,Manager,2024-02-01
            EMP-003,HR,Specialist,2024-03-01
            """;

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(testCorporateEmployer, csvContent);

        assertEquals(3, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
        assertTrue(result.getErrors().isEmpty());

        List<EmploymentRelationship> employments = employmentRelationshipRepository.findAll();
        assertEquals(3, employments.size());
        employments.forEach(emp -> {
            assertEquals(EmploymentStatus.ACTIVE, emp.getStatus());
            assertEquals(testCorporateEmployer.getId(), emp.getEmployer().getId());
        });
    }

    @Test
    void shouldHandleBulkUploadWithMixedResults() {
        IndividualCustomer emp2 = createAndSaveIndividualCustomer("EMP-002", "Jane Smith");

        String csvContent = """
            employeeNumber,department,position,startDate
            EMP-001,Engineering,Developer,2024-01-15
            INVALID,Sales,Manager,2024-02-01
            EMP-002,HR,Specialist,invalid-date
            """;

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(testCorporateEmployer, csvContent);

        assertEquals(1, result.getSuccessCount());
        assertEquals(2, result.getFailureCount());
        assertEquals(2, result.getErrors().size());
        assertTrue(result.getErrors().get(0).contains("Invalid date format") || 
                   result.getErrors().get(1).contains("Invalid date format"));
    }

    @Test
    void shouldHandleEmptyCSVContent() {
        String csvContent = "employeeNumber,department,position,startDate\n";

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(testCorporateEmployer, csvContent);

        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void shouldThrowExceptionForInvalidEmployer() {
        assertThrows(IllegalArgumentException.class, () -> {
            IndividualCustomer individualAsEmployer = new IndividualCustomer("INVALID", "Not an Employer", CustomerStatus.ACTIVE);
            individualAsEmployer = customerRepository.save(individualAsEmployer);
            
            EmploymentRelationship details = createEmploymentDetails("IT", "Staff", LocalDate.now());
            employmentRelationshipService.createEmployment(testEmployee, individualAsEmployer, details);
        });
    }

    @Test
    void shouldThrowExceptionWhenTerminatingNonExistentEmployment() {
        assertThrows(RuntimeException.class, () -> {
            employmentRelationshipService.terminateEmployment(99999L);
        });
    }

    private EmploymentRelationship createAndSaveEmployment(IndividualCustomer employee, Customer employer) {
        EmploymentRelationship employment = new EmploymentRelationship(
            employee, employer, null, "Test Department", "Test Position",
            LocalDate.of(2024, 1, 1), EmploymentStatus.ACTIVE
        );
        return employmentRelationshipRepository.save(employment);
    }

    private IndividualCustomer createAndSaveIndividualCustomer(String number, String name) {
        IndividualCustomer customer = new IndividualCustomer(number, name, CustomerStatus.ACTIVE);
        return customerRepository.save(customer);
    }

    private EmploymentRelationship createEmploymentDetails(String department, String position, LocalDate startDate) {
        IndividualCustomer dummyEmployee = new IndividualCustomer("DUMMY", "Dummy", CustomerStatus.ACTIVE);
        CorporateCustomer dummyEmployer = new CorporateCustomer("DUMMY", "Dummy", CustomerStatus.ACTIVE);
        EmploymentRelationship details = new EmploymentRelationship(
            dummyEmployee, dummyEmployer, null, department, position, startDate, EmploymentStatus.ACTIVE
        );
        return details;
    }
}
