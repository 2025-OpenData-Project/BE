package com.opendata.domain.login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record LoginRequest (
        @NotBlank String email,
        @NotBlank String password){
}