package com.feelody.feelody_backend.entity;

import com.feelody.feelody_backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.UUID;

@Entity
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    protected Member() {}
}
