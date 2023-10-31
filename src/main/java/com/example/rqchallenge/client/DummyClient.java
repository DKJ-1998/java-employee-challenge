package com.example.rqchallenge.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import com.example.rqchallenge.dto.EmployeeDeletionDto;
import com.example.rqchallenge.dto.EmployeeDto;
import com.example.rqchallenge.dto.EmployeesDto;

@FeignClient(name = "dummy", url = "${clients.dummy.url}")
public interface DummyClient {

    @GetMapping("/v1/employees")
    EmployeesDto getEmployees();

    @GetMapping("/v1/employee/{id}")
    EmployeeDto getEmployeeById(@PathVariable String id);

    @PostMapping("/v1/create")
    EmployeeDto createEmployee(
        @NotBlank @RequestParam String name,
        @PositiveOrZero @RequestParam int salary,
        @PositiveOrZero @RequestParam int age);

    @DeleteMapping("/v1/delete/{id}")
    EmployeeDeletionDto deleteEmployeeById(@NotBlank @PathVariable String id);
}
