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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EmploymentRelationshipServiceTest {

    @Mock
    private EmploymentRelationshipRepository employmentRelationshipRepository;

    @Mock
    private CustomerRepository customerRepository;

    private EmploymentRelationshipService employmentRelationshipService;

    @BeforeEach
    void setUp() {
        employmentRelationshipService = new EmploymentRelationshipService(
            employmentRelationshipRepository,
            customerRepository
        );
    }

    @Test
    void shouldCreateEmploymentRelationshipWithCorporateEmployer() {
        IndividualCustomer employee = createIndividualCustomer(1L, "EMP001");
        CorporateCustomer employer = createCorporateCustomer(2L, "CUST001");
        EmploymentRelationship details = createEmploymentDetails("Engineering", "Developer", LocalDate.of(2024, 1, 15));

        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> {
                EmploymentRelationship saved = invocation.getArgument(0);
                saved.setId(100L);
                return saved;
            });

        EmploymentRelationship result = employmentRelationshipService.createEmployment(employee, employer, details);

        assertNotNull(result);
        assertEquals(employee, result.getEmployee());
        assertEquals(employer, result.getEmployer());
        assertEquals("Engineering", result.getDepartment());
        assertEquals("Developer", result.getPosition());
        assertEquals(LocalDate.of(2024, 1, 15), result.getStartDate());
        assertEquals(EmploymentStatus.ACTIVE, result.getStatus());
        verify(employmentRelationshipRepository).save(any(EmploymentRelationship.class));
    }

    @Test
    void shouldCreateEmploymentRelationshipWithSMEEmployer() {
        IndividualCustomer employee = createIndividualCustomer(1L, "EMP001");
        SMECustomer employer = createSMECustomer(3L, "SME001");
        EmploymentRelationship details = createEmploymentDetails("Sales", "Manager", LocalDate.of(2024, 2, 1));

        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> {
                EmploymentRelationship saved = invocation.getArgument(0);
                saved.setId(101L);
                return saved;
            });

        EmploymentRelationship result = employmentRelationshipService.createEmployment(employee, employer, details);

        assertNotNull(result);
        assertEquals(employer, result.getEmployer());
        assertEquals(EmploymentStatus.ACTIVE, result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenEmployerIsNotCorporateOrSME() {
        IndividualCustomer employee = createIndividualCustomer(1L, "EMP001");
        IndividualCustomer individualAsEmployer = createIndividualCustomer(2L, "EMP002");
        EmploymentRelationship details = createEmploymentDetails("IT", "Staff", LocalDate.now());

        assertThrows(IllegalArgumentException.class, 
            () -> employmentRelationshipService.createEmployment(employee, individualAsEmployer, details));
        
        verify(employmentRelationshipRepository, never()).save(any());
    }

    @Test
    void shouldTerminateEmploymentRelationship() {
        Long employmentId = 1L;
        EmploymentRelationship employment = createEmploymentRelationship(employmentId, EmploymentStatus.ACTIVE);

        when(employmentRelationshipRepository.findById(employmentId))
            .thenReturn(Optional.of(employment));
        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        EmploymentRelationship result = employmentRelationshipService.terminateEmployment(employmentId);

        assertEquals(EmploymentStatus.TERMINATED, result.getStatus());
        assertNotNull(result.getEndDate());
        assertEquals(LocalDate.now(), result.getEndDate());
        verify(employmentRelationshipRepository).save(employment);
    }

    @Test
    void shouldThrowExceptionWhenTerminatingNonExistentEmployment() {
        Long employmentId = 999L;
        when(employmentRelationshipRepository.findById(employmentId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> employmentRelationshipService.terminateEmployment(employmentId));
    }

    @Test
    void shouldBulkUploadEmployeesSuccessfully() {
        SMECustomer employer = createSMECustomer(1L, "SME001");
        String csvContent = """
            employeeNumber,department,position,startDate
            EMP001,Engineering,Developer,2024-01-15
            EMP002,Sales,Manager,2024-02-01
            EMP003,HR,Specialist,2024-03-01
            """;

        IndividualCustomer employee1 = createIndividualCustomer(10L, "EMP001");
        IndividualCustomer employee2 = createIndividualCustomer(11L, "EMP002");
        IndividualCustomer employee3 = createIndividualCustomer(12L, "EMP003");

        when(customerRepository.findByCustomerNumber("EMP001")).thenReturn(Optional.of(employee1));
        when(customerRepository.findByCustomerNumber("EMP002")).thenReturn(Optional.of(employee2));
        when(customerRepository.findByCustomerNumber("EMP003")).thenReturn(Optional.of(employee3));
        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> {
                EmploymentRelationship saved = invocation.getArgument(0);
                saved.setId(System.nanoTime());
                return saved;
            });

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(employer, csvContent);

        assertEquals(3, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
        assertTrue(result.getErrors().isEmpty());
        verify(employmentRelationshipRepository, times(3)).save(any(EmploymentRelationship.class));
    }

    @Test
    void shouldHandleBulkUploadWithPartialFailures() {
        CorporateCustomer employer = createCorporateCustomer(1L, "CUST001");
        String csvContent = """
            employeeNumber,department,position,startDate
            EMP001,Engineering,Developer,2024-01-15
            INVALID_EMP,Sales,Manager,2024-02-01
            EMP003,HR,Specialist,invalid-date
            """;

        IndividualCustomer employee1 = createIndividualCustomer(10L, "EMP001");

        when(customerRepository.findByCustomerNumber("EMP001")).thenReturn(Optional.of(employee1));
        when(customerRepository.findByCustomerNumber("INVALID_EMP")).thenReturn(Optional.empty());
        lenient().when(customerRepository.findByCustomerNumber("EMP003")).thenReturn(Optional.empty());
        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> {
                EmploymentRelationship saved = invocation.getArgument(0);
                saved.setId(System.nanoTime());
                return saved;
            });

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(employer, csvContent);

        assertEquals(1, result.getSuccessCount());
        assertEquals(2, result.getFailureCount());
        assertEquals(2, result.getErrors().size());
        verify(employmentRelationshipRepository, times(1)).save(any(EmploymentRelationship.class));
    }

    @Test
    void shouldHandleEmptyCSVContent() {
        CorporateCustomer employer = createCorporateCustomer(1L, "CUST001");
        String csvContent = "employeeNumber,department,position,startDate\n";

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(employer, csvContent);

        assertEquals(0, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
        assertTrue(result.getErrors().isEmpty());
        verify(employmentRelationshipRepository, never()).save(any());
    }

    @Test
    void shouldHandleCSVWithWhitespace() {
        SMECustomer employer = createSMECustomer(1L, "SME001");
        String csvContent = """
            employeeNumber,department,position,startDate
            EMP001 , Engineering , Developer , 2024-01-15
            """;

        IndividualCustomer employee = createIndividualCustomer(10L, "EMP001");

        when(customerRepository.findByCustomerNumber("EMP001")).thenReturn(Optional.of(employee));
        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> {
                EmploymentRelationship saved = invocation.getArgument(0);
                saved.setId(System.nanoTime());
                return saved;
            });

        BulkUploadResult result = employmentRelationshipService.bulkUploadEmployees(employer, csvContent);

        assertEquals(1, result.getSuccessCount());
        ArgumentCaptor<EmploymentRelationship> captor = ArgumentCaptor.forClass(EmploymentRelationship.class);
        verify(employmentRelationshipRepository).save(captor.capture());
        assertEquals("Engineering", captor.getValue().getDepartment());
    }

    @Test
    void shouldSetEmploymentDetailsOnCreate() {
        IndividualCustomer employee = createIndividualCustomer(1L, "EMP001");
        SMECustomer employer = createSMECustomer(2L, "SME001");
        EmploymentRelationship details = createEmploymentDetails("Finance", "Analyst", LocalDate.of(2024, 6, 1));
        details.setEmployeeNumber("FIN001");

        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> {
                EmploymentRelationship saved = invocation.getArgument(0);
                saved.setId(100L);
                return saved;
            });

        EmploymentRelationship result = employmentRelationshipService.createEmployment(employee, employer, details);

        assertEquals("FIN001", result.getEmployeeNumber());
        assertEquals("Finance", result.getDepartment());
        assertEquals("Analyst", result.getPosition());
    }

    @Test
    void shouldNotTerminateAlreadyTerminatedEmployment() {
        Long employmentId = 1L;
        EmploymentRelationship employment = createEmploymentRelationship(employmentId, EmploymentStatus.TERMINATED);
        employment.setEndDate(LocalDate.now().minusDays(10));

        when(employmentRelationshipRepository.findById(employmentId))
            .thenReturn(Optional.of(employment));
        when(employmentRelationshipRepository.save(any(EmploymentRelationship.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        EmploymentRelationship result = employmentRelationshipService.terminateEmployment(employmentId);

        assertEquals(EmploymentStatus.TERMINATED, result.getStatus());
        verify(employmentRelationshipRepository).save(employment);
    }

    private IndividualCustomer createIndividualCustomer(Long id, String customerNumber) {
        IndividualCustomer customer = new IndividualCustomer(customerNumber, "Test Employee", CustomerStatus.ACTIVE);
        customer.setId(id);
        return customer;
    }

    private CorporateCustomer createCorporateCustomer(Long id, String customerNumber) {
        CorporateCustomer customer = new CorporateCustomer(customerNumber, "Test Corp", CustomerStatus.ACTIVE);
        customer.setId(id);
        return customer;
    }

    private SMECustomer createSMECustomer(Long id, String customerNumber) {
        SMECustomer customer = new SMECustomer(customerNumber, "Test SME", CustomerStatus.ACTIVE);
        customer.setId(id);
        return customer;
    }

    private EmploymentRelationship createEmploymentDetails(String department, String position, LocalDate startDate) {
        IndividualCustomer dummyEmployee = createIndividualCustomer(0L, "DUMMY");
        CorporateCustomer dummyEmployer = createCorporateCustomer(0L, "DUMMY");
        return new EmploymentRelationship(
            dummyEmployee, dummyEmployer, null, department, position, startDate, EmploymentStatus.ACTIVE
        );
    }

    private EmploymentRelationship createEmploymentRelationship(Long id, EmploymentStatus status) {
        IndividualCustomer employee = createIndividualCustomer(1L, "EMP001");
        CorporateCustomer employer = createCorporateCustomer(2L, "CUST001");
        EmploymentRelationship relationship = new EmploymentRelationship(
            employee, employer, "EMP001", "IT", "Developer",
            LocalDate.of(2024, 1, 1), status
        );
        relationship.setId(id);
        if (status == EmploymentStatus.TERMINATED) {
            relationship.setEndDate(LocalDate.now().minusDays(1));
        }
        return relationship;
    }
}
