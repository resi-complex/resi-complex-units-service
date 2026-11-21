package com.resicomplex.units.controller;

import com.resicomplex.units.dto.CreateUnitRequest;
import com.resicomplex.units.dto.UnitResponse;
import com.resicomplex.units.model.UnitType;
import com.resicomplex.units.service.IUnitService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/units")
public class UnitController {

    private final IUnitService unitService;

    public UnitController(IUnitService unitService) {
        this.unitService = unitService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UnitResponse createUnit(@Valid @RequestBody CreateUnitRequest request) {
        return unitService.createUnit(request);
    }

    @GetMapping
    public List<UnitResponse> getUnits(
            @RequestParam(name = "type", required = false) UnitType type
    ) {
        if (type != null) {
            return unitService.getUnitsByType(type);
        }
        return unitService.getAllUnits();
    }

    @GetMapping("/{id}")
    public UnitResponse getUnitById(@PathVariable("id") String id) {
        return unitService.getUnitById(id);
    }
}
