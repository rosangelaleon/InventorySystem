# Sistema de Gestión y Control de Stock

Sistema desarrollado en Java que permite administrar stock, compras, ventas y generar reportes con control de usuarios.

## Características Principales

* **Gestión de Stock** - Control de productos, categorías, depósitos, sucursales y ajustes.
* **Gestión de Ventas** - Administración de clientes, precios, talonarios, cuotas, cotizaciones y ventas.
* **Gestión de Compras** - Gestión de proveedores y órdenes de compra.
* **Sistema de Reportes** - Análisis detallados con filtros y exportaciones.
* **Gestión de Seguridad** - Control de usuarios, roles y permisos.

## Tecnologías

**Lenguaje:** Java 20.0.1  
**IDE:** NetBeans 18  
**Base de Datos:** MySQL 8.0+  
**Arquitectura:** MVC  
**Reportes:** JasperReports 9.0.0  
**Análisis y Diseño:** Visual Paradigm 17.2)

## Instalación

1. **Base de Datos**
```sql
CREATE DATABASE inventory_system;
USE inventory_system;
SOURCE Base_de_Datos/Estructura_BD_Con_Triggers.sql;
```

2. **Aplicación**
   - Clonar el repositorio
   - Verificar librerías en carpeta `Libraries/`
   - Configurar conexión a base de datos
   - Ejecutar `InventorySystem.jar`

## Estructura del Proyecto

```
inventory-system/
├── src/                # Código fuente (MVC)
├── Libraries/          # Librerías JAR
├── BasedeDatos/        # Scripts SQL
├── Documentos/         # Diagramas UML, capturas del sistema y manual de usuario
├── Reportes/           # Plantillas JasperReports
└── README.md
```

## Uso del Sistema

📋 **Manual Completo**: `Documentos/ManualUsuario.pdf`

## Funcionalidades Técnicas

* Triggers de base de datos para automatización
* Control de concurrencia y transacciones
* Exportación de reportes en múltiples formatos

---

**Desarrollado por**: Rosangela Isabel León Silva y Gustavo Abel León Silva  