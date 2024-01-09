package com.example.demo.entity;

import com.example.demo.enums.PermissionEnum;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long permissionId;

    private PermissionEnum type;

    private String description;
}
