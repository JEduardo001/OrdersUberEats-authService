package com.SoftwareOrdersUberEats.authService.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoCreateUser {
    //auth data
    @NotEmpty
    private String username;
    @Email
    private String email;
    @NotEmpty
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_\\-]).{8,35}$", // 1 upperCase, 1 lower case, 1 number 1 symbol
            message = "Password must contain upper, lower, number and special character"

    )
    private String password;
    @NotEmpty
    private String confirmPassword;

    //user data
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @NotNull
    private LocalDate birthday;
    @NotEmpty
    private String street;
    @NotEmpty
    private String city;
    @NotNull
    private Double latitude;
    @NotNull
    private Double longitude;
}
