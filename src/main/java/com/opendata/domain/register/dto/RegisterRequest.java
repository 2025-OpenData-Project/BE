package com.opendata.domain.register.dto;

import com.opendata.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

public record RegisterRequest(
        String email,
        String password
) {
    public User from(String encodedPassword){
        return User.builder()
                .email(email)
                .password(encodedPassword)
                .build();
    }
}

