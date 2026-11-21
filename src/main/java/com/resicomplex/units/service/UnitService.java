package com.resicomplex.units.service;

import com.resicomplex.units.dto.CreateUnitRequest;
import com.resicomplex.units.dto.UnitResponse;
import com.resicomplex.units.model.Unit;
import com.resicomplex.units.model.UnitType;
import com.resicomplex.units.repository.UnitRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UnitService implements IUnitService {

    private static final String DEFAULT_STATUS = "OCCUPIED";

    private final UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public UnitResponse createUnit(CreateUnitRequest request) {
        if (unitRepository.existsByNumber(request.number())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A unit with number '" + request.number() + "' already exists"
            );
        }

        if (request.type() == UnitType.COMMERCIAL
                && (request.commercialName() == null || request.commercialName().isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Commercial name is required when unit type is COMMERCIAL"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        Unit unit = new Unit();
        unit.setId(UUID.randomUUID().toString());
        unit.setNumber(request.number().trim());
        unit.setBlockTower(request.blockTower());
        unit.setType(request.type());

        if (request.type() == UnitType.COMMERCIAL) {
            unit.setCommercialName(request.commercialName().trim());
            unit.setBusinessType(request.businessType());
            unit.setOpeningHours(request.openingHours());
        } else {
            unit.setCommercialName(null);
            unit.setBusinessType(null);
            unit.setOpeningHours(null);
        }

        unit.setStatus(DEFAULT_STATUS);
        unit.setCreatedAt(now);
        unit.setUpdatedAt(now);

        Unit saved = unitRepository.save(unit);
        return toResponse(saved);
    }

    @Override
    public List<UnitResponse> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<UnitResponse> getUnitsByType(UnitType type) {
        return unitRepository.findByType(type).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UnitResponse getUnitById(String id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unit not found with id: " + id
                ));
        return toResponse(unit);
    }

    private UnitResponse toResponse(Unit unit) {
        return new UnitResponse(
                unit.getId(),
                unit.getNumber(),
                unit.getBlockTower(),
                unit.getType(),
                unit.getCommercialName(),
                unit.getBusinessType(),
                unit.getOpeningHours(),
                unit.getStatus(),
                unit.getCreatedAt(),
                unit.getUpdatedAt()
        );
    }
}
