package com.SoftwareOrdersUberEats.authService.entity;

import com.SoftwareOrdersUberEats.authService.enums.StatusResourceRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "role_table")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private StatusResourceRole status;
    private Instant createdAt;
    private Instant disableAt;
}
