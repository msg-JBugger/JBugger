package com.example.demo.entity;

import com.example.demo.enums.RightEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    private RightEnum type;

    private String description;

    @ManyToMany(
            targetEntity = Role.class,
            mappedBy = "permissions"
    )
    private Set<Role> roles = new HashSet<>();
}
