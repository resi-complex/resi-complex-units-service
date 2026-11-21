package com.resicomplex.units.dto;

import com.resicomplex.units.model.UnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUnitRequest(
        @NotBlank(message = "Unit number is required")
        @Size(max = 20, message = "Unit number must not exceed 20 characters")
        String number,

        @Size(max = 50, message = "Block/Tower must not exceed 50 characters")
        String blockTower,

        @NotNull(message = "Unit type is required (RESIDENTIAL or COMMERCIAL)")
        UnitType type,

        @Size(max = 150, message = "Commercial name must not exceed 150 characters")
        String commercialName,

        @Size(max = 100, message = "Business type must not exceed 100 characters")
        String businessType,

        @Size(max = 100, message = "Opening hours must not exceed 100 characters")
        String openingHours
) {}
