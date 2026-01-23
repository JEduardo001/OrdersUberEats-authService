package com.SoftwareOrdersUberEats.authService.dto.events;

import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceAuthEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DtoCreateUserEvent {
    //auth data
    @NotNull
    private UUID id;

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
    @Enumerated(EnumType.STRING)
    private StatusResourceAuthEnum status;

}
