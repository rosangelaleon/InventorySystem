Use InventorySystem;

CREATE TABLE ROLES (
    id INT NOT NULL,
    rol VARCHAR(100) NOT NULL,
    CONSTRAINT pkroles PRIMARY KEY (id),
    CONSTRAINT unique_rol UNIQUE (rol)
);

CREATE TABLE USUARIOS (
    id INT NOT NULL,
	rol_id INT,
    usuario VARCHAR(100) NOT NULL,
    contraseña VARCHAR(200) NOT NULL,
	activo INT NOT NULL DEFAULT 0,
	CONSTRAINT pkusuarios PRIMARY KEY (id),
    CONSTRAINT fkusuario_rol  FOREIGN KEY (rol_id) REFERENCES ROLES(id)
);


CREATE TABLE MENUS (
id VARCHAR(50),
nombre VARCHAR (25), /*ALMACENA EL NOMBRE DEL TIPO DE PERMISO*/
descripcion VARCHAR (100), /*DESCRIPCION DEL MENU*/
orden INT NOT NULL,/* --SERVIRÁ para organizar los registros según aparecen en el menu*/
tab INT NOT NULL, /*--Esto se usaría en caso de que se haga un reporte para que sirva de identación*/
CONSTRAINT pkmenu PRIMARY KEY (id)
);

CREATE TABLE PERMISOS (
    rol_id INT,
    menu_id VARCHAR(50),
    CONSTRAINT fk_rol_permiso FOREIGN KEY (rol_id) REFERENCES ROLES(id),
    CONSTRAINT fk_menu_permiso FOREIGN KEY (menu_id) REFERENCES MENUS(id),
    CONSTRAINT pk_roles_permiso PRIMARY KEY (rol_id, menu_id)
     
);
CREATE TABLE COTIZACIONES (
    id iNT NOT NULL,
    moneda_id INT,
    fecha DATE,
    compra DECIMAL(18, 5),
    venta DECIMAL(18, 5),
    activo  INT NOT NULL DEFAULT 0,
     CONSTRAINT pk_coti PRIMARY KEY (id),
    FOREIGN KEY (moneda_id) REFERENCES Monedas(id)
);

CREATE TABLE MONEDAS(
    id INT NOT NULL,
    moneda VARCHAR(100),
    abreviatura VARCHAR(5),
    decimales INT,
    activo  INT NOT NULL DEFAULT 0,
    cotizacion  INT NOT NULL DEFAULT 0,
    CONSTRAINT pkmoneda PRIMARY KEY (id),
    CONSTRAINT ukmoneda UNIQUE (abreviatura)
);

CREATE TABLE CIUDADES (
    id INT NOT NULL,
    ciudad VARCHAR(100),
    descripcion VARCHAR(255),
    CONSTRAINT pk_ciudades PRIMARY KEY (id)
);

CREATE TABLE PROVEEDORES (
    id INT NOT NULL,   /* Identificador del proveedor */ 
    fecha DATETIME NOT NULL,          /* Fecha de registro del proveedor */
    proveedor VARCHAR(100) NOT NULL,  /* Nombre de la empresa o la persona proveedora */
    tipodocumento VARCHAR(3),         /* Tipo de documento, acepta 'CI' o 'RUC' o puede ser NULL */
    nrodocumento VARCHAR(20),         /* Número de CI o RUC */
    celular VARCHAR(15),              /* Número de celular del proveedor */
    direccion VARCHAR(500),           /* Dirección de la empresa (puede ser larga) */
	divisoria INT ,   
    ciudad_id INT NOT NULL,           /* Ciudad de la empresa (opcional) */
    activo INT NOT NULL DEFAULT 0,    /* Estado del proveedor (1 si está activo, 0 si no) */
    CONSTRAINT pk_proveedores PRIMARY KEY (id),
    CONSTRAINT fkproveed_ciudad FOREIGN KEY (ciudad_id) REFERENCES CIUDADES (id),
    CONSTRAINT chk_tipodocumento CHECK (tipodocumento IN ('CI', 'RUC')) /* Restricción CHECK para tipodocumento */
);

CREATE TABLE CATEGORIAS (
    id INT NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100),
    activo INT,
    CONSTRAINT pk_categorias PRIMARY KEY (id)
);

CREATE TABLE MARCAS (
    id INT NOT NULL,
    marca VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255),
    CONSTRAINT pk_marcas PRIMARY KEY (id)
);

CREATE TABLE PRODUCTOS (
    id INT NOT NULL,
    producto VARCHAR(45) NOT NULL,
    descripcion VARCHAR(500),
    categoria_id INT NOT NULL,
    marca_id INT NOT NULL,
    impuesto INT NOT NULL DEFAULT 0,
    servicio INT NOT NULL DEFAULT 0,
    pesable INT NOT NULL DEFAULT 0,
    perecedero INT NOT NULL DEFAULT 0,
    activo INT NOT NULL DEFAULT 0,
    CONSTRAINT pk_productos PRIMARY KEY (id),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES CATEGORIAS (id),
    CONSTRAINT fk_producto_marca FOREIGN KEY (marca_id) REFERENCES MARCAS (id)
);

CREATE TABLE COLORES (
    id INT NOT NULL,
    color VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500),
    CONSTRAINT pk_colores PRIMARY KEY (id)
);

CREATE TABLE TAMANOS (
    id INT NOT NULL,
    tamano VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500),
    CONSTRAINT pk_tamanos PRIMARY KEY (id)
);

CREATE TABLE DISENOS (
    id INT NOT NULL,
    diseno VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500),
    CONSTRAINT pk_disenos PRIMARY KEY (id)
);

CREATE TABLE PRODUCTOS_DETALLE(
	id INT NOT NULL,
    cabecera_id INT NOT NULL, /* Relacionado con la tabla PRODUCTOS */
    codigobarras VARCHAR(20) NOT NULL, /* Código del producto (hasta 20 caracteres) */
    color_id INT NULL, /* Relacionado con la tabla COLORES */
    tamano_id INT NULL, /* Relacionado con la tabla TAMANOS */
    diseno_id INT NULL, /* Relacionado con la tabla DISENOS */
    moneda_id INT, /* Relacionado con la tabla MONEDAS */
    costo DECIMAL(18,5) NOT NULL, /* Cantidades y precios no pueden ser enteros */
    uxb DECIMAL(18,5) NOT NULL DEFAULT 1, /* Cantidad por unidad de presentación */
    stockminimo DECIMAL(18,5) NOT NULL, /* Stock mínimo requerido del producto */
    CONSTRAINT uk_producto UNIQUE (codigobarras),
    CONSTRAINT PK_producto_detalle PRIMARY  KEY (id),
    CONSTRAINT fk_producto_detalle_cab FOREIGN KEY (cabecera_id) REFERENCES PRODUCTOS(id),
    CONSTRAINT fk_producto_detalle_col FOREIGN KEY (color_id) REFERENCES COLORES(id),
    CONSTRAINT fk_producto_detalle_tamano FOREIGN KEY (tamano_id) REFERENCES TAMANOS(id),
    CONSTRAINT fk_producto_detalle_diseno FOREIGN KEY (diseno_id) REFERENCES DISENOS(id),
    CONSTRAINT fk_producto_detalle_moneda FOREIGN KEY (moneda_id) REFERENCES MONEDAS(id)
);


CREATE TABLE CLIENTES (
    id INT NOT NULL,
    nrodocumento VARCHAR(20),
     tipodocumento VARCHAR(3),         /* Tipo de documento, acepta 'CI' o 'RUC' o puede ser NULL */
    cliente VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    celular VARCHAR(15) ,
    direccion VARCHAR(255) ,
    correo VARCHAR(50) ,
    ciudad_id INT NOT NULL,
    precio_id INT, 
    fecha DATETIME NOT NULL,   
    activo INT NOT NULL DEFAULT 0,
    CONSTRAINT pk_clientes PRIMARY KEY (id),
	CONSTRAINT chk_tipodocumento CHECK (tipodocumento IN ('CI', 'RUC')), /* Restricción CHECK para tipodocumento */
    CONSTRAINT fk_cliente_precio FOREIGN KEY (precio_id) REFERENCES PRECIOS(id),
    CONSTRAINT fk_cliente_ciudad FOREIGN KEY (ciudad_id) REFERENCES CIUDADES(id)
);

/*un depósito por lo general a una sucursal [Central / suc 1 / suc n]
*/
CREATE TABLE SUCURSALES (
    id INT NOT NULL,    						/* Identificador de la sucursal */
    sucursal VARCHAR(100) NOT NULL,               /* Nombre de la sucursal*/
    direccion VARCHAR(200),                         /* Dirección donde se encuentra la s(opcional) */
	contacto VARCHAR(200), 						/*Nombre de la persona a quien contactar*/
	telefono VARCHAR(50), 						/*Telefono de contacto*/
    PRIMARY KEY (id)
);
/*Un depósito no siempre está en el local o sucursal*/
CREATE TABLE DEPOSITOS (
    id INT NOT NULL,    						/* Identificador del depósito */
    deposito VARCHAR(100) NOT NULL,               /* Nombre del depósito */
    direccion VARCHAR(200),                         /* Dirección donde se encuentra el depósito (opcional) */
	contacto VARCHAR(200), 						/*Nombre de la persona a quien contactar*/
	telefono VARCHAR(50), 						/*Telefono de contacto*/
    sucursal_id INT,                              /* Identificador de la sucursal */
    PRIMARY KEY (id),
    FOREIGN KEY (sucursal_id) REFERENCES SUCURSALES(id)
);
CREATE TABLE PRECIOS(
    id INT NOT NULL,
    listaprecio varchar(255),
    moneda_id INT,
    activo INT NOT NULL DEFAULT 0,
    CONSTRAINT pk_precios PRIMARY KEY (id),
    CONSTRAINT fk_precio_moneda FOREIGN KEY (moneda_id) REFERENCES MONEDAS(id)
);

CREATE TABLE PRECIOS_DETALLE (
    id INT NOT NULL,
    precio_id INT NOT NULL,
    productoDetalle_id VARCHAR(20) NOT NULL,
    precio DECIMAL(12,6) NOT NULL, 
   CONSTRAINT pk_precio_detalle PRIMARY KEY (id),
    CONSTRAINT fk_precio_detalle_precio FOREIGN KEY (precio_id) REFERENCES PRECIOS(id),
    CONSTRAINT fk_precio_detalle_productoDetalle FOREIGN KEY (productoDetalle_id) REFERENCES PRODUCTOS_DETALLE(codigobarras)
);

CREATE TABLE LOTES (
    id INT NOT NULL AUTO_INCREMENT, 					 /* Identificador del lote */
	productodetalle_id VARCHAR(20) NOT NULL,     /* Identificador del detalle del producto al que pertenece el lote */
    numeroLote VARCHAR(20) NOT NULL,     /* Número de lote o código del lote */
    fechaVencimiento DATE,               /* Fecha de vencimiento del lote (si aplica) */
    stockLote INT NOT NULL,  /* Stock disponible del lote */
    CONSTRAINT pk_lotes PRIMARY KEY (id),
  CONSTRAINT fkvtadetprod FOREIGN KEY (productodetalle_id) REFERENCES PRODUCTOS_DETALLE( codigobarras)
);

CREATE TABLE CUOTAS(
    id INT NOT NULL,   				  /* Identificador del registro*/
    descripcion  VARCHAR (100),                  /* 15/30/45 DIAS*/      
    cuotas INT,                       /* Cantidad de Cuotas Ejemplo: 3*/
    irregular  INT NOT NULL DEFAULT 0,                    /* [0|1] no,si, SI 1, se necesita detalle, si 0 no hace falta detalle*/
    activo INT NOT NULL DEFAULT 0, 
   CONSTRAINT pkcuota PRIMARY KEY (id)
    
);

CREATE TABLE CUOTAS_DETALLE(
    id INT NOT NULL,   /* Identificador del registro*/
    cuota_id INT,      /* Relaciona con CUOTAS*/      
    cuota INT,         /* Secuencia de cuotas, 1,2,3 Si la cabecera tiene irregular=1 */
    dias INT,          /* Según la cabecera 15, 30,45*/
    CONSTRAINT pkcuotadet PRIMARY KEY (id),
    CONSTRAINT fkdetcuota FOREIGN KEY (cuota_id) REFERENCES CUOTAS(id)
);

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
CREATE TABLE VENTAS (
    id INT  NOT NULL,  -- Identificador del registro
    fechaFactura  DATE,  -- Fecha de la factura
    fechaProceso datetime,
    cliente_id INT,  -- Relaciona a tabla CLIENTES
    nro_documento INT,  -- Entero que debe ser de 7 dígitos 0000001 SET AL GRABAR
    serie VARCHAR(10) NOT NULL,
    moneda_id INT,  -- Relaciona con tabla MONEDAS
    deposito_id INT,  -- El depósito del cual se va a sacar el producto
    tipo_documento INT,  -- [0|1] CONTADO CO, CRÉDITO CR.
    cuota_id INT,  -- Relaciona con la tabla de CUOTAS para calcular CUENTAS_A_COBRAR
    total_neto DECIMAL(18,5),  -- Sumatoria de totales sin IVA
    total_bruto DECIMAL(18,5),  -- Sumatoria de totales 
    total_impuesto DECIMAL(18,5),  -- Sumatoria del IVA
    total_exento DECIMAL(18,5),   -- Sumatoria de totales de productos exentos
    impreso INT NOT NULL DEFAULT 0,  -- Una vez impreso no se puede eliminar
    anulado INT NOT NULL DEFAULT 0,  -- Sólo puede anularse si impreso = 1
    talonario_id INT NOT NULL,  -- Relaciona a tabla TALONARIOS
    CONSTRAINT pk_venta PRIMARY KEY (id),
    CONSTRAINT fk_cli_venta FOREIGN KEY (cliente_id) REFERENCES CLIENTES(id),
    CONSTRAINT fk_moneda_venta FOREIGN KEY (moneda_id) REFERENCES MONEDAS(id),
    CONSTRAINT fk_cuota_venta FOREIGN KEY (cuota_id) REFERENCES CUOTAS(id),
    CONSTRAINT fk_deposito_venta FOREIGN KEY (deposito_id) REFERENCES DEPOSITOS(id),
    CONSTRAINT fk_talonario_venta FOREIGN KEY (talonario_id) REFERENCES TALONARIOS(id)
);

CREATE TABLE VENTAS_DETALLE(
id INT NOT NULL,             /*Identificador del registro*/
venta_id INT,               /*Identificador y relación con la cabecera*/
productodetalle_id VARCHAR(20), /*igualar nombre de campos en ambas tablas Identificador y relación con PRODUCTOS_DETALLE (codigo de barras)*/
cantidad INT,      
precio DECIMAL(18,5),        /*Si el producto es gravado se asume que es iva incluido*/
impuesto DECIMAL(18,5),       /*Se calcula del precio*/
descuento DECIMAL(18,5),     /*Debe ser en importe no en %*/
base DECIMAL(18,5),     /*el precio - el impuesto*/
total DECIMAL(18,5),         /*total = (precio - descuento ) * cantidad*/
lote_id INT,
vencimiento DATE,	
CONSTRAINT pkventa PRIMARY KEY (id),
CONSTRAINT fkvtadetallprod FOREIGN KEY (productodetalle_id) REFERENCES PRODUCTOS_DETALLE( codigobarras),
CONSTRAINT fkvtadetpventa FOREIGN KEY (venta_id) REFERENCES VENTAS(id),
CONSTRAINT fkvtalote FOREIGN KEY (lote_id) REFERENCES LOTES(id)
);

 CREATE TABLE STOCKS (
 id INT NOT NULL AUTO_INCREMENT,					
 producto_detalle varchar(75) NOT NULL,					/*Relaciona a tabla ProductosDetalle*/
 deposito_id INT,					/*Relaciona a tabla Depositos*/
 stockActual int(11) NOT NULL,			 /* Cantidad disponible */
 ultimaCompra bigint(20) NOT NULL,
 PRIMARY KEY (id),
 FOREIGN KEY (producto_detalle) REFERENCES PRODUCTOS_DETALLE(codigobarras), 
  FOREIGN KEY (deposito_id) REFERENCES DEPOSITOS(id)
);

CREATE TABLE AJUSTES_STOCK (
 id INT NOT NULL,				/* Identificador del tamaño */
 fecha DATETIME,				/* Fecha y hora del proceso */
 motivo VARCHAR(20),
 deposito_id INT,               /*Relaciona a tabla Depositos*/
 aprobado INT NOT NULL DEFAULT 0, 					/*[0|1] Indica si o no*/
 contabilizado INT NOT NULL DEFAULT 0, 					/*[0|1] Indica si o no*/
 PRIMARY KEY (id),
  FOREIGN KEY (deposito_id) REFERENCES DEPOSITOS(id)
);

 CREATE TABLE AJUSTES_STOCK_DETALLE(
 id INT NOT NULL,				/* Identificador del tamaño */
 ajuste_id INT,					/*Relaciona a tabla AJUSTES_STOCK*/
 productodetalle_id VARCHAR(20),     /* Identificador del detalle del producto al que pertenece el lote */
 cantidad_actual INT , /*Cantidad que el sistema recupera de STOCK, OJO CON LOTES*/
 cantidad_ajuste INT, /*Cantidad se insertará en STOCK, OJO CON LOTES*/
 lote VARCHAR(20) NOT NULL,     /* Número de lote o código del lote */
 lotevence DATE,               /* Fecha de vencimiento del lote (si aplica) */
 PRIMARY KEY (id),
 CONSTRAINT fk_ajustesdetalles_productoDetalle FOREIGN KEY (productodetalle_id) REFERENCES PRODUCTOS_DETALLE(codigobarras),
 FOREIGN KEY (ajuste_id) REFERENCES AJUSTES_STOCK(id),
 CONSTRAINT UNIQUE (ajuste_id, productoDetalle_id) /*PARA que no se repita el ajuste de un mismo producto en el 
 el mismo ajuste*/
);


CREATE TABLE CUENTAS_PAGAR (
     id INT NOT NULL AUTO_INCREMENT,                     /* Identificador del registro */
    proveedor_id INT,                    /* ID del proveedor al que se le debe pagar */
    factura_id INT,                     /* ID de la factura relacionada */
    cuotas INT,                         /* Cantidad de cuotas en que está dividido el monto total */
    cuota INT,                          /* El número de la cuota, ej. 1 de 3 */
    cuotas_id INT,                      /* Relaciona con la tabla de CUOTAS para calcular CUENTAS_PAGAR */
    importe DECIMAL(18,5),              /* Importe de la cuota */
    vencimiento DATE,                   /* Fecha de vencimiento de la cuota */
    pagado DECIMAL(18,5),              /* Monto pagado de la cuota */
    CONSTRAINT pkcuentapagar PRIMARY KEY (id),
    CONSTRAINT fkproveedor FOREIGN KEY (proveedor_id) REFERENCES PROVEEDORES(id),
    CONSTRAINT fkfactura FOREIGN KEY (factura_id) REFERENCES COMPRAS(id), /* Ajusta según la estructura de tu tabla de facturas */
    CONSTRAINT fkcuentapagarcuota FOREIGN KEY (cuotas_id) REFERENCES CUOTAS(id)
);
CREATE TABLE COMPRAS (
    id INT NOT NULL,    					 /* Identificador de la compra */
    fechaFactura DATE,                       /* Fecha de la factura de compra */
    proveedor_id INT,                  /* Nombre o identificación del proveedor */
    factura VARCHAR(20),                       /*  la factura de compra (por ejemplo, "001-001") */
    moneda_id INT,                            /* Relación con tabla MONEDAS */
    deposito_id INT,                          /* Depósito del cual se obtienen los productos */
    tipo_documento INT,                       /* [0|1] CONTADO CO, CRÉDITO CR */
    cuota_id INT,                            /* Relación con la tabla de CUOTAS para calcular CUENTAS_A_PAGAR */
    total_neto DECIMAL(18,5),  -- Sumatoria de totales sin IVA
    total_bruto DECIMAL(18,5),  -- Sumatoria de totales 
    total_impuesto DECIMAL(18, 5),            /* Sumatoria del IVA */
    total_exento DECIMAL(18, 5),              /* Sumatoria de totales de productos exentos */       
    CONSTRAINT pkCompra PRIMARY KEY (id),
    CONSTRAINT fkCompraProveedor FOREIGN KEY (proveedor_id) REFERENCES PROVEEDORES(id),
    CONSTRAINT fkCompraMoneda FOREIGN KEY (moneda_id) REFERENCES MONEDAS(id),
    CONSTRAINT fkCompraCuota FOREIGN KEY (cuota_id) REFERENCES cuotas(id),
    CONSTRAINT fkCompraDeposito FOREIGN KEY (deposito_id) REFERENCES DEPOSITOS(id)
);

CREATE TABLE COMPRAS_DETALLE (
    id INT NOT NULL,   							/* Identificador del detalle de compra */
	compra_id INT NOT NULL,                        /* Identificador de la compra a la que pertenece */
    productodetalle_id VARCHAR(20),
    cantidad INT,              /* Cantidad comprada */
    precio DECIMAL(18,5) NOT NULL,       /* Precio unitario del producto */
    impuesto DECIMAL(18,5) NOT NULL,             /* Impuesto asociado al producto (si aplica) */
    descuento DECIMAL(18,5),                    /* Descuento aplicado al producto (opcional) */
    base DECIMAL(18,5),     /*el precio - el impuesto*/
    total DECIMAL(18,5) NOT NULL,                /* Total del detalle de compra */
    lote VARCHAR(20),
    vencimiento DATE,
    PRIMARY KEY pkcompraDetalle(id),
    FOREIGN KEY (compra_id) REFERENCES COMPRAS(id),
   CONSTRAINT fkvtadetprodcompras FOREIGN KEY (productodetalle_id) REFERENCES PRODUCTOS_DETALLE( codigobarras)
);
CREATE TABLE NOTAS_CREDITO (
    id INT NOT NULL,                    -- Identificador único de la nota de crédito
    venta_id INT NOT NULL,              -- Relaciona con la venta original
    cliente_id INT NOT NULL,            -- Relaciona con el cliente asociado
    deposito_id INT NOT NULL,           -- Relaciona con el depósito de la nota
    moneda_id INT NOT NULL,             -- Relaciona con la moneda utilizada
    factura VARCHAR(50) NOT NULL,        -- Número de la factura
	serie VARCHAR(10) NOT NULL,			-- Serie de la nota
    nro_documento INT,  -- Entero que debe ser de 7 dígitos 0000001 SET AL GRABAR ,
    fecha DATE NOT NULL,                -- Fecha de emisión de la nota
    motivo VARCHAR(100),          	 -- motivo
    total_neto DECIMAL(18, 5) NOT NULL, -- Total sin impuestos
    total_impuesto DECIMAL(18, 5) NOT NULL, -- Total del impuesto
    total_exento DECIMAL(18, 5) NOT NULL, -- Total exento de impuestos
    total_bruto DECIMAL(18, 5) NOT NULL, -- Total con impuestos
    PRIMARY KEY (id),
    FOREIGN KEY (venta_id) REFERENCES VENTAS(id),
    FOREIGN KEY (cliente_id) REFERENCES CLIENTES(id),
    FOREIGN KEY (deposito_id) REFERENCES DEPOSITOS(id),
    FOREIGN KEY (moneda_id) REFERENCES MONEDAS(id)
);

CREATE TABLE NOTAS_CREDITO_DETALLE (
    id INT NOT NULL,                         -- Identificador del detalle
    nota_credito_id INT NOT NULL,            -- Relaciona con la cabecera de la nota de crédito
    venta_detalle_id INT NOT NULL,           -- Relaciona con el detalle de la venta
    productodetalle_id VARCHAR(20) NOT NULL, -- Relaciona con PRODUCTOS_DETALLE
    cantidad DECIMAL(18, 5) NOT NULL,        -- Cantidad devuelta
    precio DECIMAL(18, 5) NOT NULL,          -- Precio del producto
    descuento DECIMAL(18, 5),                -- Descuento aplicado
    impuesto DECIMAL(18, 5) NOT NULL,        -- Impuesto aplicado
    subtotal DECIMAL(18, 5) NOT NULL,        -- Subtotal del ítem
    lote_id INT,
	vencimiento DATE,	
    PRIMARY KEY (id),
    FOREIGN KEY (nota_credito_id) REFERENCES NOTAS_CREDITO(id),
    FOREIGN KEY (venta_detalle_id) REFERENCES VENTAS_DETALLE(id),
    FOREIGN KEY (productodetalle_id) REFERENCES PRODUCTOS_DETALLE(codigobarras),
    CONSTRAINT fkntalote FOREIGN KEY (lote_id) REFERENCES LOTES(id)
);
CREATE TABLE CUENTAS_COBRAR (
    id INT NOT NULL AUTO_INCREMENT,  /* Identificador del registro */
    cuotas INT NOT NULL,  /* Cantidad de cuotas en que está dividido el monto total */
    cuota INT NOT NULL,  /* El item de la cuota Ej. 1, Si fuera el primero del ejemplo sería [cuota de cuotas] [Cuota 1 de 3] */
    importe DECIMAL(18,5) NOT NULL,  /* Importe de la cuota */
    vencimiento DATE NOT NULL,  /* Fecha de vencimiento de la cuota, dependerá de la fecha factura, y si cuotas es regular o irregular */
    cobrado DECIMAL(18,5) NOT NULL DEFAULT 0.00,  /* Representa el monto sea total o parcial cobrado de importe */
    venta_id INT NOT NULL,  /* Relaciona con la tabla VENTAS */
    estado INT NOT NULL DEFAULT 0,  /* Estado de la cuota: 0 = Pendiente, 1 = Pagado */
    CONSTRAINT pkcuentaco PRIMARY KEY (id),
    CONSTRAINT fkcuentcoventas FOREIGN KEY (venta_id) REFERENCES VENTAS(id),
    CONSTRAINT uq_cuota UNIQUE (venta_id, cuota)
);


