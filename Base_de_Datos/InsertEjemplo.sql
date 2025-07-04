INSERT INTO roles (id, rol) VALUES (6,'SuperUsuario');
INSERT INTO usuarios (id, rol_id, usuario, contraseña,activo) VALUES (1, 1, 'Rosangela', 'ini1234',1);
INSERT INTO usuarios (id, rol_id, usuario, contraseña,activo) VALUES (2, 2, 'Gustavo', 'ini54678',1);
INSERT INTO usuarios (id, rol_id, usuario, contraseña,activo) VALUES (3, 5, 'Delia', 'ini2167',1);
INSERT INTO usuarios (id, rol_id, usuario, contraseña,activo) VALUES (4, 6, 'Martin', 'ini9831',0);
INSERT INTO permisos (id, rol) VALUES (1,'ADMINISTRACION_ACCESO');
-- Inserting 'menu1' with 'Archivo'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('menu1', 'Archivo', 'Opciones relacionadas con crear, modificar, etc', 1, 0);

-- Inserting 'menu2' with 'Edición' as per your request
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('menu2', 'Edición', 'Opciones relacionadas con edición', 2, 0);

-- Inserting 'menu3' with 'Caja'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('menu3', 'Caja', 'Opciones relacionadas con caja', 3, 0);

-- Inserting 'menu4' with 'Stock'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('menu4', 'Stock', 'Opciones relacionadas con stock', 4, 0);

-- Inserting 'menu5' with 'Compras'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('menu5', 'Compras', 'Opciones relacionadas con compras', 5, 0);

-- Inserting 'menu6' with 'Ventas'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('menu6', 'Ventas', 'Opciones relacionadas con ventas', 6, 0);

-- Inserting 'menu7' with 'Seguridad', shifting the original 'menu6' to 'menu7'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('menu7', 'Seguridad', 'Opciones relacionadas con seguridad', 7, 0);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.1', 'Nuevo', 'Crear nuevo archivo', 1, 1);

-- Guardar
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.2', 'Guardar', 'Guardar archivo', 2, 1);

-- Borrar
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.3', 'Borrar', 'Eliminar archivo', 3, 1);

-- Buscar
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.4', 'Buscar', 'Buscar en archivos', 4, 1);

-- Filtrar
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.5', 'Filtrar', 'Filtrar registro', 5, 1);

-- Imprimir
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.5', 'Imprimir', 'Imprimir archivo', 6, 1);

-- Insertar Fila
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.6', 'Insertar Fila', 'Insertar nueva fila', 7, 1);

-- Eliminar Fila
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu1.7', 'Eliminar Fila', 'Eliminar fila seleccionada', 8, 1);

-- Inserting submenu items under 'Edición'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu2.1', 'Primero', 'Ir al primer elemento', 1, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu2.2', 'Siguiente', 'Ir al siguiente elemento', 2, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu2.3', 'Anterior', 'Ir al elemento anterior', 3, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu2.4', 'Ultimo', 'Ir al último elemento', 4, 1);

-- Inserting submenu items under 'Apertura Caja'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu3.1', 'Caja', 'Acceder a la caja', 1, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu3.2', 'Planillas de Caja', 'Acceder a las planillas de caja', 2, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu3.3', 'Reporte de Caja', 'Generar reporte de caja', 3, 1);

-- Inserting submenu items under 'Stock'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu4.1', 'Productos', 'Acceder a los productos', 1, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu4.2', 'Lista de Precios y Detalles', 'Ver lista de precios y detalles', 2, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu4.3', 'Stock', 'Acceder al stock', 3, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu4.4', 'Reporte de Stock', 'Generar reporte de stock', 4, 1);
-- Inserting submenu items under 'Compras'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu5.1', 'Proveedores', 'Gestionar proveedores', 1, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu5.2', 'Compras', 'Registrar compras', 2, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu5.3', 'Pago', 'Gestionar pagos', 3, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu5.4', 'Reporte de Compras', 'Generar reporte de compras', 4, 1);
-- Inserting submenu items under 'Ventas'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu6.1', 'Clientes', 'Administrar clientes', 1, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu6.2', 'Talonarios', 'Gestionar talonarios', 2, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu6.3', 'Cobros', 'Procesar cobros', 3, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu6.4', 'Ventas', 'Registrar ventas', 4, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu6.5', 'Reporte de Ventas', 'Generar reporte de ventas', 5, 1);
-- Inserting submenu items under 'Seguridad'
INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu7.1', 'Roles', 'Gestionar roles de usuario', 1, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu7.2', 'Usuarios', 'Administrar usuarios', 2, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu7.3', 'Permisos', 'Asignar permisos', 3, 1);

INSERT INTO MENUS (id, nombre, descripcion, orden, tab) 
VALUES ('submenu7.4', 'Reporte Ingreso', 'Generar reporte de ingreso', 4, 1);




-- Plan de pago en 30/60/90 días
INSERT INTO CUOTAS (id, descripcion, cuotas, irregular) VALUES (1, '30/60/90 Días', 3, 1);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (1, 1, 1, 30);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (2, 1, 2, 60);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (3, 1, 3, 90);

--  Plan de pago en 45/90/135 días
INSERT INTO CUOTAS (id, descripcion, cuotas, irregular) VALUES (2, '45/90/135 Días', 3, 1);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (4, 2, 1, 45);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (5, 2, 2, 90);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (6, 2, 3, 135);

-- Plan de pago en 15/30/45 días
INSERT INTO CUOTAS (id, descripcion, cuotas, irregular) VALUES (3, '15/30/45 Días', 3, 1);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (7, 3, 1, 15);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (8, 3, 2, 30);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (9, 3, 3, 45);

--  Plan de pago en 60/120/180 días
INSERT INTO CUOTAS (id, descripcion, cuotas, irregular) VALUES (4, '60/120/180 Días', 3, 1);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (10, 4, 1, 60);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (11, 4, 2, 120);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (12, 4, 3, 180);

--  Plan de pago en 90/180/270 días
INSERT INTO CUOTAS (id, descripcion, cuotas, irregular) VALUES (5, '30/180/270 Días', 3, 1);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (13, 5, 1, 90);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (14, 5, 2, 180);
INSERT INTO CUOTAS_DETALLE (id, cuota_id, cuota, dias) VALUES (15, 5, 3, 270);
