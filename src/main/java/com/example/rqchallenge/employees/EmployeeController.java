package com.example.rqchallenge.employees;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.rqchallenge.client.DummyClient;
import com.example.rqchallenge.dto.Status;
import com.google.common.collect.ImmutableList;

@RestController
public class EmployeeController implements IEmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final DummyClient dummyClient;

    public EmployeeController(DummyClient dummyClient) {
        this.dummyClient = dummyClient;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("Getting all employees");
        var employeesDto = dummyClient.getEmployees();
        if (!Status.SUCCESS.equals(employeesDto.status())) {
            logger.error("Failed to get employees from dummy, status is {}", employeesDto.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        var employees = employeesDto.data();
        logger.info("Found all {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        logger.info("Finding employee names containing {}", searchString);
        var employeesDto = dummyClient.getEmployees();
        if (!Status.SUCCESS.equals(employeesDto.status())) {
            logger.error("Failed to get employees from dummy, status is {}", employeesDto.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        var matchingEmployees = employeesDto.data().parallelStream()
            .filter(employee -> employee.name().contains(searchString))
            .collect(ImmutableList.toImmutableList());
        logger.info("Found employee names containing {}", searchString);
        return ResponseEntity.ok(matchingEmployees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        logger.info("Getting employee with id {}", id);
        var employeeDto = dummyClient.getEmployeeById(id);
        if (!Status.SUCCESS.equals(employeeDto.status())) {
            logger.error("Failed to get employee {} from dummy, status is {}", id, employeeDto.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        var employee = employeeDto.data();
        logger.info("Found employee {}: {}", id, employee);
        return ResponseEntity.ok(employee);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.info("Getting highest salary of employees");
        var employeesDto = dummyClient.getEmployees();
        if (!Status.SUCCESS.equals(employeesDto.status())) {
            logger.error("Failed to get employees from dummy, status is {}", employeesDto.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        var employees = employeesDto.data();
        var optionalHighestSalary = employees.stream()
            .map(Employee::salary)
            .max(Integer::compare);
        if (optionalHighestSalary.isPresent()) {
            var highestSalary = optionalHighestSalary.get();
            logger.info("Found highest salary of employees: {}", highestSalary);
            return ResponseEntity.ok(highestSalary);
        }
        logger.error("Highest salary not found (no employees)");
        return ResponseEntity.noContent()
            .header("message", "No employees found")
            .build();
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("Getting top ten highest earning employees");
        var employeesDto = dummyClient.getEmployees();
        if (!Status.SUCCESS.equals(employeesDto.status())) {
            logger.error("Failed to get employees from dummy, status is {}", employeesDto.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        var employees = employeesDto.data();
        if (employees.size() < 10) {
            logger.error("Failed to get 10 employees from dummy, only found {}", employees.size());
            return ResponseEntity.noContent()
                .header("message", "Less than 10 employees found")
                .build();
        }
        var topTenHighestEarningEmployeeNames = employees.stream()
            .sorted(Comparator.comparingInt(Employee::salary).reversed())
            .map(Employee::name)
            .limit(10)
            .collect(ImmutableList.toImmutableList());
        logger.info("Found top ten highest earning employees: {}", topTenHighestEarningEmployeeNames);
        return ResponseEntity.ok(topTenHighestEarningEmployeeNames);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        logger.info("Creating employee: {}", employeeInput);
        try {
            return createEmployeeFromInput(employeeInput);
        } catch (ClassCastException ex) {
            logger.error("Error parsing create employee input: {}", employeeInput);
            return ResponseEntity.badRequest()
                .header("message", "Error parsing input")
                .build();
        }
    }

    private ResponseEntity<Employee> createEmployeeFromInput(Map<String, Object> employeeInput) {
        var name = String.valueOf(employeeInput.get("name"));
        var salary = (Integer) employeeInput.get("salary");
        var age = (Integer) employeeInput.get("age");
        var creationResponse = dummyClient.createEmployee(name, salary, age);
        if (!Status.SUCCESS.equals(creationResponse.status())) {
            logger.error("Failed to create employee (name='{}', salary={}, age={}), status is {}",
                name, salary, age, creationResponse.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        var createdEmployee = creationResponse.data();
        logger.info("Created employee: {}", createdEmployee);
        return ResponseEntity.ok(createdEmployee);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        logger.info("Deleting employee {}", id);
        var employeeDto = dummyClient.getEmployeeById(id);
        if (!Status.SUCCESS.equals(employeeDto.status())) {
            logger.error("Failed to get employee {} from dummy, status is {}", id, employeeDto.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        var deletedEmployeeName = employeeDto.data().name();
        logger.info("Deleting employee {} (name: {})", id, deletedEmployeeName);

        var deletionResponse = dummyClient.deleteEmployeeById(id);
        if (!Status.SUCCESS.equals(deletionResponse.status())) {
            logger.error("Failed to delete employee {}, status is {}", id, deletionResponse.status());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        logger.info("Deleted employee {}", deletedEmployeeName);
        return ResponseEntity.ok(deletedEmployeeName);
    }
}
