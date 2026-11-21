package com.resicomplex.units.service;

import com.resicomplex.units.dto.CreateUnitRequest;
import com.resicomplex.units.dto.UnitResponse;
import com.resicomplex.units.model.UnitType;
import java.util.List;

public interface IUnitService {

    UnitResponse createUnit(CreateUnitRequest request);

    List<UnitResponse> getAllUnits();

    List<UnitResponse> getUnitsByType(UnitType type);

    UnitResponse getUnitById(String id);
}
