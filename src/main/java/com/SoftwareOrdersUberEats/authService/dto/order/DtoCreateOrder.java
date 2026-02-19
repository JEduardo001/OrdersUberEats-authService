package com.SoftwareOrdersUberEats.authService.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DtoCreateOrder {
    @NotNull
    private UUID idOrder;
    @NotNull
    private UUID idUser;
    @NotNull
    private List<DtoProductsOrder> products;
}


