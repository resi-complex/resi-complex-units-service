package com.resicomplex.units.dto;

import com.resicomplex.units.model.UnitType;
import java.time.LocalDateTime;

public record UnitResponse(
        String id,
        String number,
        String blockTower,
        UnitType type,
        String commercialName,
        String businessType,
        String openingHours,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
