package com.example.rqchallenge.dto;

import java.util.List;

import com.example.rqchallenge.employees.Employee;

public record EmployeesDto(Status status, List<Employee> data) {
}
