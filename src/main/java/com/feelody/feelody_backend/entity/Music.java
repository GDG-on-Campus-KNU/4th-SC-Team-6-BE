package com.feelody.feelody_backend.entity;

import com.feelody.feelody_backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Music extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String musicUrl;

    @Column
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected Music() {
    }
}
