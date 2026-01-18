package com.SoftwareOrdersUberEats.authService.dto.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoResponseApiLogin {
    Integer status;
    String token;

}
