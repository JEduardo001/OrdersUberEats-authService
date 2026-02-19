package com.SoftwareOrdersUberEats.authService.entity;

import com.SoftwareOrdersUberEats.authService.enums.statesResource.StatusResourceAuthEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "auth_table", indexes = {
        @Index(name = "indexUsername", columnList = "username"),
        @Index(name = "indexEmail", columnList = "email")

})
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Schema(allowableValues = {"ACTIVE", "INACTIVE", "DELETED"})
    private StatusResourceAuthEnum status;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_auth",
            joinColumns = @JoinColumn(name = "id_auth"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private List<RoleEntity> roles = new ArrayList<>();
    private Instant createdAt;
    private Instant disableAt;
}
