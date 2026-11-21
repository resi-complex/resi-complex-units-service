package com.resicomplex.units.repository;

import com.resicomplex.units.model.Unit;
import com.resicomplex.units.model.UnitType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends JpaRepository<Unit, String> {

    List<Unit> findByType(UnitType type);

    boolean existsByNumber(String number);
}
