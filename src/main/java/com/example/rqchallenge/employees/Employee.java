package com.example.rqchallenge.employees;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Employee(
    String id,
    @JsonProperty("employee_name") String name,
    @JsonProperty("employee_salary") int salary,
    @JsonProperty("employee_age") int age,
    String profileImage) {
}
