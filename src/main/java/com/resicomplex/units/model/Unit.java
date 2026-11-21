package com.resicomplex.units.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
public class Unit {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "number", length = 20, nullable = false, unique = true)
    private String number;

    @Column(name = "block_tower", length = 50)
    private String blockTower;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    private UnitType type;

    @Column(name = "commercial_name", length = 150)
    private String commercialName;

    @Column(name = "business_type", length = 100)
    private String businessType;

    @Column(name = "opening_hours", length = 100)
    private String openingHours;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
