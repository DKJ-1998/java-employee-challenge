package com.example.rqchallenge.employees;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import com.example.rqchallenge.client.DummyClient;
import com.example.rqchallenge.dto.EmployeeDeletionDto;
import com.example.rqchallenge.dto.EmployeeDto;
import com.example.rqchallenge.dto.EmployeesDto;
import com.example.rqchallenge.dto.Status;

@SpringBootTest(classes = EmployeeController.class)
class EmployeeControllerTest {
    private static final int HIGHEST_SALARY = 80000;
    private static final int MEDIUM_SALARY = 70000;
    private static final int LOW_SALARY = 60000;
    private static final String EMPLOYEE_1_ID = "1";
    private static final String EMPLOYEE_1_NAME = "Alice";
    private static final int EMPLOYEE_1_AGE = 30;
    private static final Employee EMPLOYEE_1 = new Employee(EMPLOYEE_1_ID, EMPLOYEE_1_NAME, MEDIUM_SALARY, EMPLOYEE_1_AGE, "");
    private static final Employee EMPLOYEE_2 = new Employee("2", "Bob", MEDIUM_SALARY, 31, "");
    private static final Employee EMPLOYEE_3 = new Employee("3", "Charlie", LOW_SALARY, 32, "");
    private static final Employee EMPLOYEE_4 = new Employee("4", "Mr Bob Jones", HIGHEST_SALARY, 33, "");
    private static final Employee EMPLOYEE_5 = new Employee("5", "Dave", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_6 = new Employee("6", "Eve", MEDIUM_SALARY, 33, "");
    private static final Employee EMPLOYEE_7 = new Employee("7", "Frank", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_8 = new Employee("8", "Grace", MEDIUM_SALARY, 33, "");
    private static final Employee EMPLOYEE_9 = new Employee("9", "Heidi", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_10 = new Employee("10", "Ivan", MEDIUM_SALARY, 33, "");
    private static final Employee EMPLOYEE_11 = new Employee("11", "Judy", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_12 = new Employee("12", "Keith", MEDIUM_SALARY, 33, "");
    private static final Employee EMPLOYEE_13 = new Employee("13", "Lucy", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_14 = new Employee("14", "Mike", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_15 = new Employee("15", "Norbert", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_16 = new Employee("16", "Oscar", MEDIUM_SALARY, 33, "");
    private static final Employee EMPLOYEE_17 = new Employee("17", "Pete", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_18 = new Employee("18", "Ruth", MEDIUM_SALARY, 33, "");
    private static final Employee EMPLOYEE_19 = new Employee("19", "Steve", LOW_SALARY, 33, "");
    private static final Employee EMPLOYEE_20 = new Employee("20", "Taylor", MEDIUM_SALARY, 33, "");
    private static final List<Employee> ALL_EMPLOYEES = List.of(
        EMPLOYEE_1, EMPLOYEE_2, EMPLOYEE_3, EMPLOYEE_4, EMPLOYEE_5, EMPLOYEE_6, EMPLOYEE_7, EMPLOYEE_8, EMPLOYEE_9, EMPLOYEE_10,
        EMPLOYEE_11, EMPLOYEE_12, EMPLOYEE_13, EMPLOYEE_14, EMPLOYEE_15, EMPLOYEE_16, EMPLOYEE_17, EMPLOYEE_18, EMPLOYEE_19, EMPLOYEE_20);
    private static final List<Employee> NINE_EMPLOYEES = List.of(
        EMPLOYEE_1, EMPLOYEE_2, EMPLOYEE_3, EMPLOYEE_4, EMPLOYEE_5, EMPLOYEE_6, EMPLOYEE_7, EMPLOYEE_8, EMPLOYEE_9);
    private static final EmployeesDto EMPLOYEES_DTO = new EmployeesDto(Status.SUCCESS, ALL_EMPLOYEES);
    private static final EmployeesDto FAILED_EMPLOYEES_DTO = new EmployeesDto(Status.FAILED, List.of());
    private static final EmployeesDto NO_EMPLOYEES_DTO = new EmployeesDto(Status.SUCCESS, List.of());
    private static final EmployeesDto NINE_EMPLOYEES_DTO = new EmployeesDto(Status.SUCCESS, NINE_EMPLOYEES);
    private static final EmployeeDto EMPLOYEE_DTO_1 = new EmployeeDto(Status.SUCCESS, EMPLOYEE_1);
    private static final EmployeeDto FAILED_EMPLOYEE_DTO_1 = new EmployeeDto(Status.FAILED, null);
    private static final EmployeeDeletionDto EMPLOYEE_DELETION_DTO = new EmployeeDeletionDto(Status.SUCCESS, "message");
    private static final EmployeeDeletionDto FAILED_EMPLOYEE_DELETION_DTO = new EmployeeDeletionDto(Status.FAILED, "message");
    private static final String MATCHING_SEARCH_STRING = "Bob";
    private static final String NON_MATCHING_SEARCH_STRING = "something";

    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private DummyClient dummyClient;

    @Test
    void getAllEmployees_ValidResponseFromClient_EmployeesReturned() {
        when(dummyClient.getEmployees()).thenReturn(EMPLOYEES_DTO);

        var getAllEmployeesResponse = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, getAllEmployeesResponse.getStatusCode());
        assertEquals(ALL_EMPLOYEES, getAllEmployeesResponse.getBody());
    }

    @Test
    void getAllEmployees_InvalidStatusResponseFromClient_BadGatewayReturned() {
        when(dummyClient.getEmployees()).thenReturn(FAILED_EMPLOYEES_DTO);

        var getAllEmployeesResponse = employeeController.getAllEmployees();

        assertEquals(HttpStatus.BAD_GATEWAY, getAllEmployeesResponse.getStatusCode());
    }

    @Test
    void getEmployeesByNameSearch_EmployeeMatchedAndEmployeeContainsString_MatchingEmployeesReturned() {
        when(dummyClient.getEmployees()).thenReturn(EMPLOYEES_DTO);

        var getEmployeesByNameSearchResponse = employeeController.getEmployeesByNameSearch(MATCHING_SEARCH_STRING);

        var expectedMatchingEmployees = List.of(EMPLOYEE_2, EMPLOYEE_4);
        assertEquals(HttpStatus.OK, getEmployeesByNameSearchResponse.getStatusCode());
        assertEquals(expectedMatchingEmployees, getEmployeesByNameSearchResponse.getBody());
    }

    @Test
    void getEmployeesByNameSearch_NoEmployeeMatchesForString_NoEmployeesReturned() {
        when(dummyClient.getEmployees()).thenReturn(EMPLOYEES_DTO);

        var getEmployeesByNameSearchResponse = employeeController.getEmployeesByNameSearch(NON_MATCHING_SEARCH_STRING);

        var expectedEmployees = List.of();
        assertEquals(HttpStatus.OK, getEmployeesByNameSearchResponse.getStatusCode());
        assertEquals(expectedEmployees, getEmployeesByNameSearchResponse.getBody());
    }

    @Test
    void getEmployeesByNameSearch_InvalidStatusResponseFromClient_BadGatewayReturned() {
        when(dummyClient.getEmployees()).thenReturn(FAILED_EMPLOYEES_DTO);

        var getEmployeesByNameSearchResponse = employeeController.getAllEmployees();

        assertEquals(HttpStatus.BAD_GATEWAY, getEmployeesByNameSearchResponse.getStatusCode());
    }

    @Test
    void getEmployeeById_ValidResponseFromClient_EmployeeReturned() {
        when(dummyClient.getEmployeeById(EMPLOYEE_1_ID)).thenReturn(EMPLOYEE_DTO_1);

        var getEmployeeResponse = employeeController.getEmployeeById(EMPLOYEE_1_ID);

        assertEquals(HttpStatus.OK, getEmployeeResponse.getStatusCode());
        assertEquals(EMPLOYEE_1, getEmployeeResponse.getBody());
    }

    @Test
    void getEmployeeById_InvalidStatusResponseFromClient_BadGatewayReturned() {
        when(dummyClient.getEmployeeById(EMPLOYEE_1_ID)).thenReturn(FAILED_EMPLOYEE_DTO_1);

        var getEmployeeResponse = employeeController.getEmployeeById(EMPLOYEE_1_ID);

        assertEquals(HttpStatus.BAD_GATEWAY, getEmployeeResponse.getStatusCode());
    }

    @Test
    void getHighestSalaryOfEmployees_ValidResponseFromClient_HighestSalaryReturned() {
        when(dummyClient.getEmployees()).thenReturn(EMPLOYEES_DTO);

        var getAllEmployeesResponse = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.OK, getAllEmployeesResponse.getStatusCode());
        assertEquals(HIGHEST_SALARY, getAllEmployeesResponse.getBody());
    }

    @Test
    void getHighestSalaryOfEmployees_InvalidStatusResponseFromClient_BadGatewayReturned() {
        when(dummyClient.getEmployees()).thenReturn(FAILED_EMPLOYEES_DTO);

        var getAllEmployeesResponse = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.BAD_GATEWAY, getAllEmployeesResponse.getStatusCode());
    }

    @Test
    void getHighestSalaryOfEmployees_NoEmployeesReturnedByClient_NoContentReturned() {
        when(dummyClient.getEmployees()).thenReturn(NO_EMPLOYEES_DTO);

        var getAllEmployeesResponse = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.NO_CONTENT, getAllEmployeesResponse.getStatusCode());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_ValidResponseFromClient_TopTenHighestEarnersReturned() {
        when(dummyClient.getEmployees()).thenReturn(EMPLOYEES_DTO);

        var getTopTenEarnersResponse = employeeController.getTopTenHighestEarningEmployeeNames();

        var actualTopTenEarners = getTopTenEarnersResponse.getBody();
        var expectedTopTenEarners = List.of(EMPLOYEE_1_NAME, "Bob", "Mr Bob Jones", "Eve", "Grace", "Ivan", "Keith", "Oscar", "Ruth", "Taylor");
        assertEquals(HttpStatus.OK, getTopTenEarnersResponse.getStatusCode());
        assertEquals(10, actualTopTenEarners.size());
        assertTrue(actualTopTenEarners.containsAll(expectedTopTenEarners),
            String.format("Expected top ten earners are %s, returned %s", expectedTopTenEarners, actualTopTenEarners));
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_LessThanTenEmployeesReturnedByClient_NoContentReturned() {
        when(dummyClient.getEmployees()).thenReturn(NINE_EMPLOYEES_DTO);

        var getTopTenEarnersResponse = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(HttpStatus.NO_CONTENT, getTopTenEarnersResponse.getStatusCode());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_InvalidStatusResponseFromClient_BadGatewayReturned() {
        when(dummyClient.getEmployees()).thenReturn(NINE_EMPLOYEES_DTO);

        var getTopTenEarnersResponse = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(HttpStatus.NO_CONTENT, getTopTenEarnersResponse.getStatusCode());
    }

    @Test
    void createEmployee_EmployeeCreatedInClient_CreatedEmployeeReturned() {
        when(dummyClient.createEmployee(EMPLOYEE_1_NAME, MEDIUM_SALARY, EMPLOYEE_1_AGE)).thenReturn(EMPLOYEE_DTO_1);

        Map<String, Object> employeeInput = Map.of(
            "name", EMPLOYEE_1_NAME,
            "salary", MEDIUM_SALARY,
            "age", EMPLOYEE_1_AGE);
        var creationResponse = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.OK, creationResponse.getStatusCode());
        assertEquals(EMPLOYEE_1, creationResponse.getBody());
    }

    @Test
    void createEmployee_InvalidAge_CreatedEmployeeReturned() {
        Map<String, Object> employeeInput = Map.of(
            "name", EMPLOYEE_1_NAME,
            "salary", MEDIUM_SALARY,
            "age", "text");
        var creationResponse = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, creationResponse.getStatusCode());
    }

    @Test
    void createEmployee_InvalidSalary_CreatedEmployeeReturned() {
        Map<String, Object> employeeInput = Map.of(
            "name", EMPLOYEE_1_NAME,
            "salary", "text",
            "age", EMPLOYEE_1_AGE);
        var creationResponse = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, creationResponse.getStatusCode());
    }

    @Test
    void createEmployee_InvalidStatusResponseFromClient_BadGatewayReturned() {
        when(dummyClient.createEmployee(EMPLOYEE_1_NAME, MEDIUM_SALARY, EMPLOYEE_1_AGE)).thenReturn(FAILED_EMPLOYEE_DTO_1);

        Map<String, Object> employeeInput = Map.of(
            "name", EMPLOYEE_1_NAME,
            "salary", MEDIUM_SALARY,
            "age", EMPLOYEE_1_AGE);
        var creationResponse = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_GATEWAY, creationResponse.getStatusCode());
    }

    @Test
    void deleteEmployeeById_EmployeeDeletedInClient_EmployeeNameReturned() {
        when(dummyClient.getEmployeeById(EMPLOYEE_1_ID)).thenReturn(EMPLOYEE_DTO_1);
        when(dummyClient.deleteEmployeeById(EMPLOYEE_1_ID)).thenReturn(EMPLOYEE_DELETION_DTO);

        var getEmployeeResponse = employeeController.deleteEmployeeById(EMPLOYEE_1_ID);

        assertEquals(HttpStatus.OK, getEmployeeResponse.getStatusCode());
        assertEquals(EMPLOYEE_1_NAME, getEmployeeResponse.getBody());
    }

    @Test
    void deleteEmployeeById_InvalidResponseFromClientGettingEmployeeName_BadGatewayReturned() {
        when(dummyClient.getEmployeeById(EMPLOYEE_1_ID)).thenReturn(FAILED_EMPLOYEE_DTO_1);

        var getEmployeeResponse = employeeController.deleteEmployeeById(EMPLOYEE_1_ID);

        assertEquals(HttpStatus.BAD_GATEWAY, getEmployeeResponse.getStatusCode());
    }

    @Test
    void deleteEmployeeById_InvalidResponseFromClientDeletingEmployee_BadGatewayReturned() {
        when(dummyClient.getEmployeeById(EMPLOYEE_1_ID)).thenReturn(EMPLOYEE_DTO_1);
        when(dummyClient.deleteEmployeeById(EMPLOYEE_1_ID)).thenReturn(FAILED_EMPLOYEE_DELETION_DTO);

        var getEmployeeResponse = employeeController.deleteEmployeeById(EMPLOYEE_1_ID);

        assertEquals(HttpStatus.BAD_GATEWAY, getEmployeeResponse.getStatusCode());
    }
}
