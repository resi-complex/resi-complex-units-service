package com.resicomplex.units.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.resicomplex.units.dto.CreateUnitRequest;
import com.resicomplex.units.dto.UnitResponse;
import com.resicomplex.units.model.Unit;
import com.resicomplex.units.model.UnitType;
import com.resicomplex.units.repository.UnitRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitService unitService;

    @Test
    void createUnit_shouldCreateResidentialUnitSuccessfully() {
        CreateUnitRequest request = new CreateUnitRequest(
                "A-101",
                "Torre A",
                UnitType.RESIDENTIAL,
                null,
                null,
                null
        );

        when(unitRepository.existsByNumber("A-101")).thenReturn(false);
        when(unitRepository.save(any(Unit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UnitResponse response = unitService.createUnit(request);

        assertNotNull(response.id());
        assertEquals("A-101", response.number());
        assertEquals("Torre A", response.blockTower());
        assertEquals(UnitType.RESIDENTIAL, response.type());
        assertNull(response.commercialName());
        assertEquals("OCCUPIED", response.status());
        verify(unitRepository).save(any(Unit.class));
    }

    @Test
    void createUnit_shouldCreateCommercialUnitWithBusinessDetails() {
        CreateUnitRequest request = new CreateUnitRequest(
                "L-01",
                "Plazoleta Central",
                UnitType.COMMERCIAL,
                "Cafetería El Grano",
                "Cafetería",
                "Lun-Sab 7:00am - 8:00pm"
        );

        when(unitRepository.existsByNumber("L-01")).thenReturn(false);
        when(unitRepository.save(any(Unit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UnitResponse response = unitService.createUnit(request);

        assertNotNull(response.id());
        assertEquals("L-01", response.number());
        assertEquals(UnitType.COMMERCIAL, response.type());
        assertEquals("Cafetería El Grano", response.commercialName());
        assertEquals("Cafetería", response.businessType());
        assertEquals("Lun-Sab 7:00am - 8:00pm", response.openingHours());
        assertEquals("OCCUPIED", response.status());
    }

    @Test
    void createUnit_shouldThrowConflictWhenNumberAlreadyExists() {
        CreateUnitRequest request = new CreateUnitRequest(
                "A-101",
                "Torre A",
                UnitType.RESIDENTIAL,
                null,
                null,
                null
        );

        when(unitRepository.existsByNumber("A-101")).thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> unitService.createUnit(request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(unitRepository, never()).save(any());
    }

    @Test
    void createUnit_shouldThrowBadRequestWhenCommercialUnitLacksCommercialName() {
        CreateUnitRequest request = new CreateUnitRequest(
                "L-02",
                "Plazoleta Central",
                UnitType.COMMERCIAL,
                "   ",
                "Farmacia",
                "24 Horas"
        );

        when(unitRepository.existsByNumber("L-02")).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> unitService.createUnit(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(unitRepository, never()).save(any());
    }

    @Test
    void getUnitsByType_shouldFilterUnitsByType() {
        Unit commercialUnit = new Unit();
        commercialUnit.setId("unit-1");
        commercialUnit.setNumber("L-01");
        commercialUnit.setType(UnitType.COMMERCIAL);
        commercialUnit.setCommercialName("Droguería Salud");
        commercialUnit.setStatus("OCCUPIED");
        commercialUnit.setCreatedAt(LocalDateTime.now());
        commercialUnit.setUpdatedAt(LocalDateTime.now());

        when(unitRepository.findByType(UnitType.COMMERCIAL)).thenReturn(List.of(commercialUnit));

        List<UnitResponse> results = unitService.getUnitsByType(UnitType.COMMERCIAL);

        assertEquals(1, results.size());
        assertEquals(UnitType.COMMERCIAL, results.getFirst().type());
        assertEquals("Droguería Salud", results.getFirst().commercialName());
    }

    @Test
    void getUnitById_shouldThrowNotFoundWhenDoesNotExist() {
        when(unitRepository.findById("non-existent")).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> unitService.getUnitById("non-existent")
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
