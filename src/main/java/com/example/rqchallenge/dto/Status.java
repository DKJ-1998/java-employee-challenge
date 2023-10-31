package com.example.rqchallenge.dto;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("success") SUCCESS,
    @JsonEnumDefaultValue FAILED;
}
