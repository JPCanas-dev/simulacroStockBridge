-- =============================================================================
-- DATOS INICIALES (SEED DATA): STOCKBRIDGE
-- =============================================================================

-- Usuarios iniciales (BCrypt passwords):
-- admin / admin123
-- bodeguero / bodega123
-- empleado1 / empleado123
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$GJh4olz9wT5IiL6spkzmI.rjAsWiHbsAk7owSIWzP2mQKIbfC2JZm', 'ROLE_ADMIN'),
('bodeguero', '$2a$10$JMM24T7MvQesYTwF8L84BOdLYirAl6nmATYYB1kGX5YPp6d9UZ1Lq', 'ROLE_BODEGA'),
('empleado1', '$2a$10$6GIOBFn/VhqaOlVNj5rsMedu14oN/5yy6ofALnUa1b6.Mlz1Sy50i', 'ROLE_EMPLEADO');

-- Catálogo de productos iniciales
INSERT INTO products (code, name, description, category, price, stock, is_active) VALUES
('MON-001', 'Monitor Dell 27 4K', 'Monitor profesional IPS UHD con ajuste de altura', 'MONITORES', 350.00, 15, TRUE),
('MON-002', 'Monitor LG Ultrawide 34', 'Monitor curvo para productividad multitarea', 'MONITORES', 520.00, 2, TRUE),
('LAP-001', 'Laptop Lenovo ThinkPad T14', 'Procesador Core i7 16GB RAM 512GB SSD', 'COMPUTADORES', 1200.00, 8, TRUE),
('ACC-001', 'Teclado Mecánico Logitech MX', 'Conexión inalámbrica multidispositivo retroiluminado', 'ACCESORIOS', 95.00, 25, TRUE),
('ACC-002', 'Mouse Inalámbrico Logitech MX Master 3S', 'Sensor electromagnético de alta precisión', 'ACCESORIOS', 85.00, 0, TRUE),
('DIS-001', 'Disco Duro Externo 2TB', 'Copia de seguridad resistente a golpes (obsoleto)', 'ALMACENAMIENTO', 70.00, 5, FALSE);

-- Movimientos de inventario iniciales (reflejan la entrada de stock)
INSERT INTO inventory_movements (product_id, request_id, movement_type, quantity, created_at) VALUES
(1, NULL, 'ENTRADA', 15, CURRENT_TIMESTAMP),
(2, NULL, 'ENTRADA', 2, CURRENT_TIMESTAMP),
(3, NULL, 'ENTRADA', 8, CURRENT_TIMESTAMP),
(4, NULL, 'ENTRADA', 25, CURRENT_TIMESTAMP);
