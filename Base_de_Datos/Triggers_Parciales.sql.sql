use inventorysystem;
CREATE TABLE TALONARIOS (
    id INT NOT NULL,  -- Identificador del registro
	tipo_comprobante  INT NOT NULL,  -- [0|1] Recibo, Factura
    serie_comprobante VARCHAR(10) ,  -- Serie del comprobante
    numero_timbrado VARCHAR(20) ,  -- Número de timbrado
    fecha_inicio_timbrado DATE ,  -- Fecha de inicio del timbrado
    fecha_final_timbrado DATE ,  -- Fecha final del timbrado
    numero_inicial INT NOT NULL,  -- Número inicial factura
    numero_final INT NOT NULL,  -- Número final factura
    numero_actual INT NOT NULL,  -- Número actual utilizado
    activo  INT NOT NULL DEFAULT 0,  -- Estado activo del talonario
    CONSTRAINT pk_talonario PRIMARY KEY (id)
);
DELIMITER $$

-- Trigger para validar fechas al insertar
CREATE TRIGGER validar_fechas BEFORE INSERT ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        IF NEW.fecha_inicio_timbrado IS NULL OR NEW.fecha_final_timbrado IS NULL THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Las fechas de inicio y final no pueden ser nulas para Factura.';
        END IF;
        
        IF NEW.fecha_inicio_timbrado >= NEW.fecha_final_timbrado THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La fecha de inicio debe ser menor que la fecha final.';
        END IF;
    END IF;
END$$

-- Trigger para validar fechas al actualizar
CREATE TRIGGER validar_fechas_update BEFORE UPDATE ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        IF NEW.fecha_inicio_timbrado IS NULL OR NEW.fecha_final_timbrado IS NULL THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Las fechas de inicio y final no pueden ser nulas para Factura.';
        END IF;

        IF NEW.fecha_inicio_timbrado >= NEW.fecha_final_timbrado THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La fecha de inicio debe ser menor que la fecha final.';
        END IF;
    END IF;
END$$

-- Trigger para validar números al insertar
CREATE TRIGGER validar_numeros BEFORE INSERT ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.numero_inicial >= NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número inicial debe ser menor que el número final.';
    END IF;
END$$

-- Trigger para validar números al actualizar
CREATE TRIGGER validar_numeros_update BEFORE UPDATE ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.numero_inicial >= NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número inicial debe ser menor que el número final.';
    END IF;
END$$

-- Trigger para validar numero_actual al insertar
CREATE TRIGGER validar_numero_actual_insert BEFORE INSERT ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.numero_actual < NEW.numero_inicial OR NEW.numero_actual > NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número actual debe estar entre el número inicial y el número final.';
    END IF;
END$$

-- Trigger para validar numero_actual al actualizar
CREATE TRIGGER validar_numero_actual_update BEFORE UPDATE ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.numero_actual < NEW.numero_inicial OR NEW.numero_actual > NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número actual debe estar entre el número inicial y el número final.';
    END IF;
END$$

-- Trigger para validar longitud al insertar
CREATE TRIGGER validar_longitud_al_insertar BEFORE INSERT ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        IF LENGTH(NEW.numero_timbrado) != 8 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El numero_timbrado debe ser exactamente de 8 dígitos.';
        END IF;
    END IF;
END$$

-- Trigger para validar longitud al actualizar
CREATE TRIGGER validar_longitud_al_actualizar BEFORE UPDATE ON TALONARIOS
FOR EACH ROW
BEGIN
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        IF LENGTH(NEW.numero_timbrado) != 8 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El numero_timbrado debe ser exactamente de 8 dígitos.';
        END IF;
    END IF;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER validar_unicidad BEFORE INSERT ON TALONARIOS
FOR EACH ROW
BEGIN
    DECLARE count INT DEFAULT 0;
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        SELECT COUNT(*) INTO count FROM TALONARIOS WHERE serie_comprobante = NEW.serie_comprobante AND numero_timbrado = NEW.numero_timbrado;
        IF count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La combinación de serie_comprobante y numero_timbrado ya existe.';
        END IF;
    END IF;
END$$
DELIMITER $$

CREATE TRIGGER validar_unicidad_update BEFORE UPDATE ON TALONARIOS
FOR EACH ROW
BEGIN
    DECLARE count INT DEFAULT 0;
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        SELECT COUNT(*) INTO count FROM TALONARIOS WHERE serie_comprobante = NEW.serie_comprobante AND numero_timbrado = NEW.numero_timbrado AND id != NEW.id;
        IF count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La combinación de serie_comprobante y numero_timbrado ya existe.';
        END IF;
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER validar_rangos_recibo BEFORE INSERT ON TALONARIOS
FOR EACH ROW
BEGIN
    DECLARE count INT DEFAULT 0;

    IF NEW.tipo_comprobante = 0 THEN -- Solo para Recibo
        -- Verificar que el rango no se solape con ningún rango existente para recibo
        SELECT COUNT(*) INTO count 
        FROM TALONARIOS 
        WHERE 
            tipo_comprobante = 0 AND
            (
                NEW.numero_inicial BETWEEN numero_inicial AND numero_final OR
                NEW.numero_final BETWEEN numero_inicial AND numero_final OR
                numero_inicial BETWEEN NEW.numero_inicial AND NEW.numero_final OR
                numero_final BETWEEN NEW.numero_inicial AND NEW.numero_final
            );
        
        IF count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El rango de números de recibo se solapa con un rango existente.';
        END IF;
    END IF;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER validar_rangos_recibo_update BEFORE UPDATE ON TALONARIOS
FOR EACH ROW
BEGIN
    DECLARE count INT DEFAULT 0;

    IF NEW.tipo_comprobante = 0 THEN -- Solo para Recibo
        -- Verificar que el rango no se solape con ningún rango existente para recibo, excluyendo el propio registro
        SELECT COUNT(*) INTO count 
        FROM TALONARIOS 
        WHERE 
            tipo_comprobante = 0 AND
            (
                NEW.numero_inicial BETWEEN numero_inicial AND numero_final OR
                NEW.numero_final BETWEEN numero_inicial AND numero_final OR
                numero_inicial BETWEEN NEW.numero_inicial AND NEW.numero_final OR
                numero_final BETWEEN NEW.numero_inicial AND NEW.numero_final
            )
            AND id != NEW.id;
        
        IF count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El rango de números de recibo se solapa con un rango existente.';
        END IF;
    END IF;
END$$

DELIMITER ;
DELIMITER $$
CREATE TRIGGER `ventas_validar_talonario`
BEFORE INSERT ON `ventas`
FOR EACH ROW
BEGIN
    DECLARE fecha_final DATE;

    SELECT fecha_final_timbrado INTO fecha_final
    FROM TALONARIOS
    WHERE id = NEW.talonario_id;

    IF fecha_final < CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede usar un talonario vencido.';
    END IF;
END$$
DELIMITER ;

-- Definición del trigger before_ventas_insert
DELIMITER //
CREATE TRIGGER before_ventas_insert
BEFORE INSERT ON VENTAS
FOR EACH ROW
BEGIN
    DECLARE max_nro INT;
    DECLARE inicial_nro INT;
    DECLARE final_nro INT;
    DECLARE cliente_activo INT;

    -- Obtener el número actual, inicial y final del talonario
    SELECT numero_actual, numero_inicial, numero_final INTO max_nro, inicial_nro, final_nro
    FROM TALONARIOS
    WHERE id = NEW.talonario_id AND tipo_comprobante = 1 AND activo = 1
    FOR UPDATE;

    -- Incrementar el número de documento
    IF max_nro IS NULL THEN
        SET NEW.nro_documento = inicial_nro;
    ELSE
        SET NEW.nro_documento = max_nro + 1;
    END IF;

    -- Validar el rango del número de documento
    IF NEW.nro_documento > final_nro THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Número de documento excede el rango permitido del talonario';
    END IF;

    -- Validar que el cliente esté activo
    SELECT activo INTO cliente_activo FROM CLIENTES WHERE id = NEW.cliente_id;
    IF cliente_activo != 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no está activo';
    END IF;

    -- Actualizar el número actual en el talonario
    UPDATE TALONARIOS
    SET numero_actual = NEW.nro_documento
    WHERE id = NEW.talonario_id AND tipo_comprobante = 1 AND activo = 1;
END//
DELIMITER ;

-- Definición del trigger after_ventas_update
DELIMITER //
CREATE TRIGGER after_ventas_update
AFTER UPDATE ON VENTAS
FOR EACH ROW
BEGIN
    DECLARE cliente_activo INT;

    -- Asegurarse de que el número de documento en TALONARIOS esté actualizado
    IF OLD.nro_documento != NEW.nro_documento THEN
        UPDATE TALONARIOS
        SET numero_actual = NEW.nro_documento
        WHERE id = NEW.talonario_id AND tipo_comprobante = 1 AND activo = 1;
    END IF;

    -- Validar que el cliente esté activo
    SELECT activo INTO cliente_activo FROM CLIENTES WHERE id = NEW.cliente_id;
    IF cliente_activo != 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no está activo';
    END IF;
END//
DELIMITER ;
-- Definición del trigger before_ventas_insert
DELIMITER //
CREATE TRIGGER before_ventas_insert
BEFORE INSERT ON VENTAS
FOR EACH ROW
BEGIN
    DECLARE max_nro INT;
    DECLARE inicial_nro INT;
    DECLARE final_nro INT;
    DECLARE cliente_activo INT;

    -- Obtener el número actual, inicial y final del talonario
    SELECT numero_actual, numero_inicial, numero_final INTO max_nro, inicial_nro, final_nro
    FROM TALONARIOS
    WHERE id = NEW.talonario_id AND tipo_comprobante = 1 AND activo = 1
    FOR UPDATE;

    -- Incrementar el número de documento
    IF max_nro IS NULL THEN
        SET NEW.nro_documento = inicial_nro;
    ELSE
        SET NEW.nro_documento = max_nro + 1;
    END IF;

    -- Validar el rango del número de documento
    IF NEW.nro_documento > final_nro THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Número de documento excede el rango permitido del talonario';
    END IF;

    -- Validar que el cliente esté activo
    SELECT activo INTO cliente_activo FROM CLIENTES WHERE id = NEW.cliente_id;
    IF cliente_activo != 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no está activo';
    END IF;

    -- Actualizar el número actual en el talonario
    UPDATE TALONARIOS
    SET numero_actual = NEW.nro_documento
    WHERE id = NEW.talonario_id AND tipo_comprobante = 1 AND activo = 1;
END//
DELIMITER ;

-- Definición del trigger after_ventas_update
DELIMITER //
CREATE TRIGGER after_ventas_update
AFTER UPDATE ON VENTAS
FOR EACH ROW
BEGIN
    DECLARE cliente_activo INT;

    -- Asegurarse de que el número de documento en TALONARIOS esté actualizado
    IF OLD.nro_documento != NEW.nro_documento THEN
        UPDATE TALONARIOS
        SET numero_actual = NEW.nro_documento
        WHERE id = NEW.talonario_id AND tipo_comprobante = 1 AND activo = 1;
    END IF;

    -- Validar que el cliente esté activo
    SELECT activo INTO cliente_activo FROM CLIENTES WHERE id = NEW.cliente_id;
    IF cliente_activo != 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El cliente no está activo';
    END IF;
END//
DELIMITER ;

DELIMITER $$
CREATE TRIGGER compra_insert AFTER INSERT ON COMPRAS
FOR EACH ROW
BEGIN
    DECLARE cuotas INT;
    DECLARE irregular INT;
    DECLARE cuotaactual INT;
    DECLARE dias INT;
    DECLARE importecuota DECIMAL(18, 2); 
    DECLARE ultimacuota DECIMAL(18, 2);
    DECLARE vence DATE; 

    -- Verificar que la compra es a crédito (tipo_documento = 1 para crédito)
    IF NEW.tipo_documento = 1 THEN
        -- Verificar que el depósito está especificado
        IF NEW.deposito_id IS NULL THEN
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'No se ha especificado depósito';
        END IF;

        -- Obtener las cuotas e irregularidad del plazo
        SELECT c.cuotas, c.irregular
        INTO cuotas, irregular
        FROM CUOTAS c
        WHERE c.id = NEW.cuota_id;

        -- Calcular el importe de las cuotas y la última cuota
        IF NEW.total_bruto > 0 THEN
            SET importecuota = ROUND(NEW.total_bruto / cuotas, 2);
            SET ultimacuota = NEW.total_bruto - (importecuota * (cuotas - 1));
        END IF;

        SET cuotaactual = 1;

        -- Generar las cuotas
        WHILE cuotaactual <= cuotas DO
            IF irregular > 0 THEN
                SELECT cd.dias
                INTO dias
                FROM CUOTAS_DETALLE cd
                WHERE cd.cuota_id = NEW.cuota_id
                AND cd.cuota = cuotaactual;

                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL dias DAY);
            ELSE 
                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL cuotaactual MONTH);
            END IF;

            -- Ajustar el importe de la última cuota
            IF cuotaactual = cuotas THEN
                SET importecuota = ultimacuota;
            END IF;

            -- Insertar en CUENTAS_PAGAR
            INSERT INTO CUENTAS_PAGAR (
                proveedor_id, factura_id, cuotas, cuota, cuotas_id, importe, vencimiento, pagado
            )
            VALUES (
                NEW.proveedor_id, NEW.id, cuotas, cuotaactual, NEW.cuota_id, importecuota, vence, 0.00
            );

            SET cuotaactual = cuotaactual + 1;
        END WHILE;
    END IF;
END$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER `compra_update` BEFORE UPDATE ON `COMPRAS` FOR EACH ROW 
BEGIN
    DECLARE cuotas INT;
    DECLARE irregular INT;
    DECLARE cuotaactual INT;
    DECLARE dias INT;     
    DECLARE idCtaPagar INT;
    DECLARE importecuota DECIMAL(18, 5); 
    DECLARE ultimacuota DECIMAL(18, 5);
    DECLARE vence DATE; 
    DECLARE msg VARCHAR(255);

    -- Verificar si el tipo_documento cambia de crédito (1) a contado (0)
    IF OLD.tipo_documento = 1 AND NEW.tipo_documento = 0 THEN
        -- Eliminar cuentas asociadas en CUENTAS_PAGAR
        DELETE FROM CUENTAS_PAGAR
        WHERE factura_id = OLD.id;
    END IF;

    -- Si el nuevo tipo_documento es crédito (1), calcular o regenerar cuentas por pagar
    IF NEW.tipo_documento = 1 THEN
        -- Validar que depósito esté especificado
        IF NEW.deposito_id IS NULL THEN
            SET msg = 'No se ha especificado depósito.';
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg; 
        END IF;

        -- Eliminar cuentas asociadas existentes para recalcularlas
        DELETE FROM CUENTAS_PAGAR WHERE factura_id = NEW.id;

        -- Obtener las cuotas e irregularidad del plazo asociado
        SELECT c.cuotas, c.irregular
        INTO cuotas, irregular
        FROM CUOTAS c
        WHERE c.id = NEW.cuota_id;

        -- Calcular el importe de cada cuota y la última cuota
        IF NEW.total_neto > 0 THEN
            SET importecuota = ROUND((NEW.total_neto / cuotas), 5);
            SET ultimacuota = NEW.total_neto - (importecuota * (cuotas - 1));
        END IF;

        -- Inicializar la cuota actual
        SET cuotaactual = 1;

        -- Generar las cuotas
        WHILE cuotaactual <= cuotas DO
            IF irregular > 0 THEN
                -- Obtener los días específicos de vencimiento
                SELECT cd.dias 
                INTO dias 
                FROM CUOTAS_DETALLE cd
                WHERE cd.cuota_id = NEW.cuota_id
                AND cd.cuota = cuotaactual;
                
                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL dias DAY);
            ELSE
                -- Calcular vencimiento de manera uniforme
                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL cuotaactual MONTH);
            END IF;

            -- Si es la última cuota, ajustar el importe
            IF cuotaactual = cuotas THEN
                SET importecuota = ultimacuota;
            END IF;

            -- Insertar en cuentas por pagar
            INSERT INTO CUENTAS_PAGAR (proveedor_id, factura_id, cuotas, cuota, cuotas_id, importe, vencimiento, pagado)
            VALUES (NEW.proveedor_id, NEW.id, cuotas, cuotaactual, NEW.cuota_id, importecuota, vence, 0.0);

            SET cuotaactual = cuotaactual + 1;
        END WHILE;
    END IF;
END$$

DELIMITER ;

DELIMITER $$
CREATE TRIGGER `compra_delete` BEFORE DELETE ON `COMPRAS` FOR EACH ROW 
BEGIN
    -- Paso 1: Eliminar los detalles de la compra relacionados
    DELETE FROM COMPRAS_DETALLE 
    WHERE compra_id = OLD.id;


    -- Paso 4: Eliminar las cuentas a pagar relacionadas con la factura
    DELETE FROM CUENTAS_PAGAR
    WHERE factura_id = OLD.id;
END$$

DELIMITER ;


DELIMITER $$
CREATE TRIGGER compras_detalle_insert
AFTER INSERT ON compras_detalle
FOR EACH ROW
BEGIN
    DECLARE stockActual INT;
    -- Verificar si el stock ya existe
    SELECT COUNT(*) INTO stockActual
    FROM stocks
    WHERE producto_detalle = NEW.productodetalle_id AND deposito_id = (SELECT deposito_id FROM compras WHERE id = NEW.compra_id);
    -- Si no existe, insertar stock
    IF stockActual = 0 THEN
        INSERT INTO stocks (producto_detalle, deposito_id, stockActual, ultimaCompra)
        VALUES (NEW.productodetalle_id, (SELECT deposito_id FROM compras WHERE id = NEW.compra_id), NEW.cantidad, UNIX_TIMESTAMP(NOW()));
    ELSE
        -- Actualizar el stock existente
        UPDATE stocks
        SET stockActual = stockActual + NEW.cantidad,
            ultimaCompra = UNIX_TIMESTAMP(NOW())
        WHERE producto_detalle = NEW.productodetalle_id AND deposito_id = (SELECT deposito_id FROM compras WHERE id = NEW.compra_id);
    END IF;
    -- Insertar lote
    INSERT INTO lotes (productodetalle_id, numeroLote, fechaVencimiento, stockLote)
    VALUES (NEW.productodetalle_id, NEW.lote, NEW.vencimiento, NEW.cantidad);
END$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER compras_detalle_update
AFTER UPDATE ON compras_detalle
FOR EACH ROW
BEGIN
    DECLARE diferenciaCantidad INT;
    DECLARE depositoId INT;

    -- Obtener el depósito relacionado con la compra
    SELECT deposito_id INTO depositoId FROM compras WHERE id = OLD.compra_id;

    -- Calcular la diferencia de cantidad entre el registro viejo y el nuevo
    SET diferenciaCantidad = NEW.cantidad - OLD.cantidad;

    -- Actualizar el stock en la tabla `stocks`
    UPDATE stocks
    SET stockActual = stockActual + diferenciaCantidad,
        ultimaCompra = UNIX_TIMESTAMP(NOW())
    WHERE producto_detalle = OLD.productodetalle_id AND deposito_id = depositoId;

    -- Si el lote o la fecha de vencimiento cambian
    IF OLD.lote != NEW.lote OR OLD.vencimiento != NEW.vencimiento THEN
        -- Restar la cantidad del lote viejo
        UPDATE lotes
        SET stockLote = stockLote - OLD.cantidad
        WHERE productodetalle_id = OLD.productodetalle_id AND numeroLote = OLD.lote;

        -- Eliminar el lote si el stock llega a 0
        DELETE FROM lotes
        WHERE productodetalle_id = OLD.productodetalle_id AND numeroLote = OLD.lote AND stockLote <= 0;

        -- Agregar la cantidad al lote nuevo
        INSERT INTO lotes (productodetalle_id, numeroLote, fechaVencimiento, stockLote)
        VALUES (NEW.productodetalle_id, NEW.lote, NEW.vencimiento, NEW.cantidad)
        ON DUPLICATE KEY UPDATE 
            stockLote = stockLote + VALUES(stockLote);
    ELSE
        -- Si el lote no cambia, solo ajustar la cantidad
        UPDATE lotes
        SET stockLote = stockLote + diferenciaCantidad
        WHERE productodetalle_id = OLD.productodetalle_id AND numeroLote = OLD.lote;
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER compras_detalle_delete
AFTER DELETE ON compras_detalle
FOR EACH ROW
BEGIN
    DECLARE depositoId INT;

    -- Obtener el depósito relacionado con la compra
    SELECT deposito_id INTO depositoId FROM compras WHERE id = OLD.compra_id;

    -- Restar la cantidad del stock en la tabla `stocks`
    UPDATE stocks
    SET stockActual = stockActual - OLD.cantidad
    WHERE producto_detalle = OLD.productodetalle_id AND deposito_id = depositoId;

    -- Restar la cantidad del lote correspondiente
    UPDATE lotes
    SET stockLote = stockLote - OLD.cantidad
    WHERE productodetalle_id = OLD.productodetalle_id AND numeroLote = OLD.lote;

    -- Eliminar el lote si el stock llega a 0
    DELETE FROM lotes
    WHERE productodetalle_id = OLD.productodetalle_id AND numeroLote = OLD.lote AND stockLote <= 0;
END$$

DELIMITER ;
use inventorysystem;
DELIMITER $$

CREATE TRIGGER `ventas_insert` AFTER INSERT ON `VENTAS` FOR EACH ROW 
BEGIN
    DECLARE cuotas INT;
    DECLARE irregular INT;
    DECLARE decimales INT DEFAULT 2;  -- Valor fijo para los decimales
    DECLARE cuotaactual INT;
    DECLARE dias INT;     
    DECLARE idCtaCobrar INT;
    DECLARE importecuota DECIMAL(18,2); 
    DECLARE ultimacuota DECIMAL(18,2);
    DECLARE vence DATE; 
    DECLARE msg VARCHAR(3000);    

    -- Validación: Si el tipo_documento es CRÉDITO (1), manejar las cuotas
    IF NEW.tipo_documento = 1 THEN
        -- Validar que cuota_id sea válido
        IF NEW.cuota_id IS NULL THEN
            SET msg = 'El tipo_documento es CRÉDITO pero no se especificó cuota_id.';
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg; 
        END IF;

        -- Obtener los datos de cuotas e irregularidad del plazo asociado
        SELECT c.cuotas, c.irregular
        INTO cuotas, irregular
        FROM CUOTAS c
        WHERE c.id = NEW.cuota_id;

        -- Calcular el importe de las cuotas
        IF NEW.total_neto > 0 THEN
            SET importecuota = ROUND((NEW.total_neto / cuotas), decimales);
            SET ultimacuota = NEW.total_neto - (importecuota * (cuotas - 1));
        ELSE
            SET importecuota = 0;
            SET ultimacuota = 0;
        END IF;

        SET cuotaactual = 1;

        -- Procesar cuotas
        WHILE cuotaactual <= cuotas DO
            IF irregular = 1 THEN
                -- Si es irregular, obtener días específicos para la cuota
                SELECT cd.dias
                INTO dias
                FROM CUOTAS_DETALLE cd
                WHERE cd.cuota_id = NEW.cuota_id
                  AND cd.cuota = cuotaactual;

                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL dias DAY);
            ELSE
                -- Si no es irregular, calcular el vencimiento mes a mes
                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL cuotaactual MONTH);
            END IF;

            -- Ajustar la última cuota
            IF cuotaactual = cuotas THEN
                SET importecuota = ultimacuota;
            END IF;

            -- Insertar registro en CUENTAS_COBRAR
            INSERT INTO CUENTAS_COBRAR (cuotas, cuota, importe, vencimiento, venta_id, estado)
            VALUES (cuotas, cuotaactual, importecuota, vence, NEW.id, 0);

            SET cuotaactual = cuotaactual + 1;
        END WHILE;
    ELSE
        -- Validación: Si el tipo_documento es CONTADO (0), no se permiten cuotas
        IF NEW.cuota_id IS NOT NULL THEN
            SET msg = 'El tipo_documento es CONTADO pero se especificó una cuota.';
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg; 
        END IF;
    END IF;
END$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER `ventas_update` 
BEFORE UPDATE ON `VENTAS` 
FOR EACH ROW 
BEGIN
    DECLARE cuotas INT;
    DECLARE irregular INT;
    DECLARE decimales INT DEFAULT 2;  -- Número de decimales fijos
    DECLARE cuotaactual INT;
    DECLARE dias INT;
    DECLARE idCtaCobrar INT;
    DECLARE importecuota DECIMAL(18,2); 
    DECLARE ultimacuota DECIMAL(18,2);
    DECLARE vence DATE; 
    DECLARE msg VARCHAR(3000);    

    -- Si el registro ya está impreso, solo permitir modificar "anulado"
    IF OLD.impreso = 1 THEN
        IF NEW.anulado != OLD.anulado THEN
            -- Permitir modificar "anulado"
            SET NEW.anulado = NEW.anulado;
        ELSE
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'No se permite editar este registro porque ya está impreso.';
        END IF;
    ELSE
        -- Si no está impreso (impreso = 0), recalcular según tipo_documento
        IF NEW.tipo_documento = 1 THEN  -- CRÉDITO
            -- Verificar que cuota_id sea válido
            IF NEW.cuota_id IS NULL THEN
                SET msg = 'El tipo_documento es CRÉDITO pero no se especificó cuota_id.';
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
            END IF;

            -- Obtener cuotas y si es irregular
            SELECT c.cuotas, c.irregular
            INTO cuotas, irregular
            FROM CUOTAS c
            WHERE c.id = NEW.cuota_id;

            -- Calcular las cuotas
            IF NEW.total_neto > 0 THEN
                SET importecuota = ROUND((NEW.total_neto / cuotas), decimales);
                SET ultimacuota = NEW.total_neto - (importecuota * (cuotas - 1));
            ELSE
                SET importecuota = 0;
                SET ultimacuota = 0;
            END IF;

            SET cuotaactual = 1;

            -- Eliminar las cuotas existentes para esta venta en CUENTAS_COBRAR
            DELETE FROM CUENTAS_COBRAR WHERE venta_id = OLD.id;

            -- Insertar las nuevas cuotas recalculadas
            WHILE cuotaactual <= cuotas DO
                IF irregular = 1 THEN
                    -- Si es irregular, obtener días específicos para la cuota
                    SELECT cd.dias
                    INTO dias
                    FROM CUOTAS_DETALLE cd
                    WHERE cd.cuota_id = NEW.cuota_id
                      AND cd.cuota = cuotaactual;

                    SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL dias DAY);
                ELSE
                    -- Si no es irregular, calcular vencimientos mes a mes
                    SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL cuotaactual MONTH);
                END IF;

                -- Ajustar la última cuota
                IF cuotaactual = cuotas THEN
                    SET importecuota = ultimacuota;
                END IF;

                -- Insertar la cuota recalculada en CUENTAS_COBRAR
                INSERT INTO CUENTAS_COBRAR (cuotas, cuota, importe, vencimiento, venta_id, estado)
                VALUES (cuotas, cuotaactual, importecuota, vence, NEW.id, 0);

                SET cuotaactual = cuotaactual + 1;
            END WHILE;

        ELSEIF NEW.tipo_documento = 0 THEN  -- CONTADO
            -- Si el tipo_documento es CONTADO, eliminar cualquier cuota existente
            DELETE FROM CUENTAS_COBRAR WHERE venta_id = OLD.id;
            -- Asegurar que cuota_id no esté configurado
            SET NEW.cuota_id = NULL;
        END IF;
    END IF;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER `ventas_before_update`
BEFORE UPDATE ON `VENTAS`
FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(300);

    -- Verificar que solo se pueda anular un registro si impreso = 1
    IF NEW.anulado = 1 AND OLD.impreso = 0 THEN
        SET msg = 'No se puede anular un registro que no ha sido impreso.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;

END$$

DELIMITER ;


DELIMITER $$

CREATE TRIGGER `ventas_before_insert`
BEFORE INSERT ON `VENTAS`
FOR EACH ROW
BEGIN
    DECLARE msg VARCHAR(300);

    -- Verificar que no se pueda insertar un registro con anulado = 1
    IF NEW.anulado = 1 THEN
        SET msg = 'No se puede anular una venta que no ha sido impresa.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER `ventas_insert` AFTER INSERT ON `VENTAS` FOR EACH ROW 
BEGIN
    DECLARE cuotas INT;
    DECLARE irregular INT;
    DECLARE decimales INT DEFAULT 2;  -- Valor fijo para los decimales
    DECLARE cuotaactual INT;
    DECLARE dias INT;     
    DECLARE idCtaCobrar INT;
    DECLARE importecuota DECIMAL(18,2); 
    DECLARE ultimacuota DECIMAL(18,2);
    DECLARE vence DATE; 
    DECLARE msg VARCHAR(3000);    

    -- Validación: Si el tipo_documento es CRÉDITO (1), manejar las cuotas
    IF NEW.tipo_documento = 1 THEN
        -- Validar que cuota_id sea válido
        IF NEW.cuota_id IS NULL THEN
            SET msg = 'El tipo_documento es CRÉDITO pero no se especificó cuota_id.';
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg; 
        END IF;

        -- Obtener los datos de cuotas e irregularidad del plazo asociado
        SELECT c.cuotas, c.irregular
        INTO cuotas, irregular
        FROM CUOTAS c
        WHERE c.id = NEW.cuota_id;

        -- Calcular el importe de las cuotas
        IF NEW.total_neto > 0 THEN
            SET importecuota = ROUND((NEW.total_neto / cuotas), decimales);
            SET ultimacuota = NEW.total_neto - (importecuota * (cuotas - 1));
        ELSE
            SET importecuota = 0;
            SET ultimacuota = 0;
        END IF;

        SET cuotaactual = 1;

        -- Procesar cuotas
        WHILE cuotaactual <= cuotas DO
            IF irregular = 1 THEN
                -- Si es irregular, obtener días específicos para la cuota
                SELECT cd.dias
                INTO dias
                FROM CUOTAS_DETALLE cd
                WHERE cd.cuota_id = NEW.cuota_id
                  AND cd.cuota = cuotaactual;

                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL dias DAY);
            ELSE
                -- Si no es irregular, calcular el vencimiento mes a mes
                SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL cuotaactual MONTH);
            END IF;

            -- Ajustar la última cuota
            IF cuotaactual = cuotas THEN
                SET importecuota = ultimacuota;
            END IF;

            -- Insertar registro en CUENTAS_COBRAR
            INSERT INTO CUENTAS_COBRAR (cuotas, cuota, importe, vencimiento, venta_id, estado)
            VALUES (cuotas, cuotaactual, importecuota, vence, NEW.id, 0);

            SET cuotaactual = cuotaactual + 1;
        END WHILE;
    ELSE
        -- Validación: Si el tipo_documento es CONTADO (0), no se permiten cuotas
        IF NEW.cuota_id IS NOT NULL THEN
            SET msg = 'El tipo_documento es CONTADO pero se especificó una cuota.';
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg; 
        END IF;
    END IF;
END$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER `ventas_update` 
BEFORE UPDATE ON `VENTAS` 
FOR EACH ROW 
BEGIN
    DECLARE cuotas INT;
    DECLARE irregular INT;
    DECLARE decimales INT DEFAULT 2;  -- Número de decimales fijos
    DECLARE cuotaactual INT;
    DECLARE dias INT;
    DECLARE idCtaCobrar INT;
    DECLARE importecuota DECIMAL(18,2); 
    DECLARE ultimacuota DECIMAL(18,2);
    DECLARE vence DATE; 
    DECLARE msg VARCHAR(3000);    

    -- Si el registro ya está impreso, solo permitir modificar "anulado"
    IF OLD.impreso = 1 THEN
        IF NEW.anulado != OLD.anulado THEN
            -- Permitir modificar "anulado"
            SET NEW.anulado = NEW.anulado;
        ELSE
            SIGNAL SQLSTATE '45000' 
            SET MESSAGE_TEXT = 'No se permite editar este registro porque ya está impreso.';
        END IF;
    ELSE
        -- Si no está impreso (impreso = 0), recalcular según tipo_documento
        IF NEW.tipo_documento = 1 THEN  -- CRÉDITO
            -- Verificar que cuota_id sea válido
            IF NEW.cuota_id IS NULL THEN
                SET msg = 'El tipo_documento es CRÉDITO pero no se especificó cuota_id.';
                SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
            END IF;

            -- Obtener cuotas y si es irregular
            SELECT c.cuotas, c.irregular
            INTO cuotas, irregular
            FROM CUOTAS c
            WHERE c.id = NEW.cuota_id;

            -- Calcular las cuotas
            IF NEW.total_neto > 0 THEN
                SET importecuota = ROUND((NEW.total_neto / cuotas), decimales);
                SET ultimacuota = NEW.total_neto - (importecuota * (cuotas - 1));
            ELSE
                SET importecuota = 0;
                SET ultimacuota = 0;
            END IF;

            SET cuotaactual = 1;

            -- Eliminar las cuotas existentes para esta venta en CUENTAS_COBRAR
            DELETE FROM CUENTAS_COBRAR WHERE venta_id = OLD.id;

            -- Insertar las nuevas cuotas recalculadas
            WHILE cuotaactual <= cuotas DO
                IF irregular = 1 THEN
                    -- Si es irregular, obtener días específicos para la cuota
                    SELECT cd.dias
                    INTO dias
                    FROM CUOTAS_DETALLE cd
                    WHERE cd.cuota_id = NEW.cuota_id
                      AND cd.cuota = cuotaactual;

                    SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL dias DAY);
                ELSE
                    -- Si no es irregular, calcular vencimientos mes a mes
                    SET vence = DATE_ADD(NEW.fechaFactura, INTERVAL cuotaactual MONTH);
                END IF;

                -- Ajustar la última cuota
                IF cuotaactual = cuotas THEN
                    SET importecuota = ultimacuota;
                END IF;

                -- Insertar la cuota recalculada en CUENTAS_COBRAR
                INSERT INTO CUENTAS_COBRAR (cuotas, cuota, importe, vencimiento, venta_id, estado)
                VALUES (cuotas, cuotaactual, importecuota, vence, NEW.id, 0);

                SET cuotaactual = cuotaactual + 1;
            END WHILE;

        ELSEIF NEW.tipo_documento = 0 THEN  -- CONTADO
            -- Si el tipo_documento es CONTADO, eliminar cualquier cuota existente
            DELETE FROM CUENTAS_COBRAR WHERE venta_id = OLD.id;
            -- Asegurar que cuota_id no esté configurado
            SET NEW.cuota_id = NULL;
        END IF;
    END IF;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER `ventas_before_delete`
BEFORE DELETE ON `VENTAS`
FOR EACH ROW
BEGIN
    DECLARE cuentaId INT;
    DECLARE cobroId INT;

    -- Verificar si el registro está marcado como impreso
    IF OLD.impreso = 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede eliminar una venta que ya está marcada como impresa.';
    END IF;

    -- Verificar si existen detalles de la venta y eliminarlos
    IF EXISTS (SELECT 1 FROM VENTAS_DETALLE WHERE venta_id = OLD.id) THEN
        DELETE FROM VENTAS_DETALLE WHERE venta_id = OLD.id;
    END IF;

    -- Después de validar y eliminar dependencias, la cabecera se eliminará automáticamente
    -- debido a que este trigger se ejecuta en el evento BEFORE DELETE.

END$$

DELIMITER ;

DELIMITER $$

CREATE TRIGGER trg_before_insert_ventasdetalle
BEFORE INSERT ON VENTAS_DETALLE
FOR EACH ROW
BEGIN
    DECLARE loteId INT;
    DECLARE fechaVencimiento DATE;

    -- Obtener el lote y fecha de vencimiento desde LOTES basado en el producto detalle
    SELECT id, fechaVencimiento
    INTO loteId, fechaVencimiento
    FROM LOTES
    WHERE productodetalle_id = NEW.productodetalle_id
      AND stockLote > 0
    ORDER BY fechaVencimiento ASC
    LIMIT 1;

    -- Validar si se encontró un lote
    IF loteId IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay lotes disponibles para el producto detalle especificado.';
    END IF;

    -- Actualizar los valores en el registro a insertar
    SET NEW.lote_id = loteId;
    SET NEW.vencimiento = fechaVencimiento;

    -- Actualizar el stock del lote utilizado
    UPDATE LOTES
    SET stockLote = stockLote - NEW.cantidad
    WHERE id = loteId;

    -- Asegurar que el stock en la tabla STOCKS también se actualice
    UPDATE STOCKS
    SET stockActual = stockActual - NEW.cantidad
    WHERE producto_detalle = NEW.productodetalle_id
      AND deposito_id = (SELECT deposito_id FROM VENTAS WHERE id = NEW.venta_id);
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER trg_before_update_ventasdetalle
BEFORE UPDATE ON VENTAS_DETALLE
FOR EACH ROW
BEGIN
    DECLARE cantidadDiferencia INT;
    DECLARE nuevoLoteId INT;
    DECLARE nuevaFechaVencimiento DATE;

    -- Calcular la diferencia de cantidad entre la nueva y la antigua
    SET cantidadDiferencia = NEW.cantidad - OLD.cantidad;

    -- Actualizar el stock del lote basado en la diferencia
    UPDATE LOTES
    SET stockLote = stockLote - cantidadDiferencia
    WHERE id = OLD.lote_id;

    -- Actualizar el stock general basado en la diferencia
    UPDATE STOCKS
    SET stockActual = stockActual - cantidadDiferencia
    WHERE producto_detalle = OLD.productodetalle_id
      AND deposito_id = (SELECT deposito_id FROM VENTAS WHERE id = OLD.venta_id);

    -- Si cambia el productodetalle_id, buscar un nuevo lote
    IF NEW.productodetalle_id != OLD.productodetalle_id THEN
        -- Seleccionar un nuevo lote para el producto modificado
        SELECT id, fechaVencimiento
        INTO nuevoLoteId, nuevaFechaVencimiento
        FROM LOTES
        WHERE productodetalle_id = NEW.productodetalle_id
          AND stockLote > 0
        ORDER BY fechaVencimiento ASC
        LIMIT 1;

        -- Validar si hay un nuevo lote
        IF nuevoLoteId IS NULL THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No hay lotes disponibles para el nuevo producto detalle especificado.';
        END IF;

        -- Asignar el nuevo lote y fecha de vencimiento
        SET NEW.lote_id = nuevoLoteId;
        SET NEW.vencimiento = nuevaFechaVencimiento;
    END IF;
END$$

DELIMITER ;
DELIMITER $$

CREATE TRIGGER trg_before_delete_ventasdetalle
BEFORE DELETE ON VENTAS_DETALLE
FOR EACH ROW
BEGIN
    -- Restaurar el stock del lote
    UPDATE LOTES
    SET stockLote = stockLote + OLD.cantidad
    WHERE id = OLD.lote_id;

    -- Restaurar el stock general
    UPDATE STOCKS
    SET stockActual = stockActual + OLD.cantidad
    WHERE producto_detalle = OLD.productodetalle_id
      AND deposito_id = (SELECT deposito_id FROM VENTAS WHERE id = OLD.venta_id);
END$$

DELIMITER ;
