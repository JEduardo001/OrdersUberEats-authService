package com.SoftwareOrdersUberEats.authService.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DtoProductsOrder {

    private UUID idProduct;
    private Integer quantityProducts;
    private Instant createAt;
    private Instant deleteAt;
}
