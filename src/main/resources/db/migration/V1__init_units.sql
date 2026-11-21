CREATE TABLE units (
    id VARCHAR(36) PRIMARY KEY,
    number VARCHAR(20) NOT NULL UNIQUE,
    block_tower VARCHAR(50) NULL,
    type VARCHAR(20) NOT NULL, -- 'RESIDENTIAL' o 'COMMERCIAL'
    commercial_name VARCHAR(150) NULL, -- Nombre del negocio si es local
    business_type VARCHAR(100) NULL,   -- Ej: Cafetería, Peluquería, Farmacia
    opening_hours VARCHAR(100) NULL,   -- Ej: Lun-Sab 8:00am - 8:00pm
    status VARCHAR(20) NOT NULL DEFAULT 'OCCUPIED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

CREATE INDEX idx_units_type ON units (type);
CREATE INDEX idx_units_status ON units (status);
