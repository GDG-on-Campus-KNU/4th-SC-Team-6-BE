package com.feelody.feelody_backend.entity;

import com.feelody.feelody_backend.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Music extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column
    private String musicUrl;

    @Column
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected Music() {
    }
}
