
-- Generado automáticamente 
-- ============================================================
-- Tabla: sucursales 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sucursales`
--

DROP TABLE IF EXISTS `sucursales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sucursales` (
  `id` int NOT NULL,
  `sucursal` varchar(100) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `contacto` varchar(200) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:47
 
 
 
-- Tabla: ciudades 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ciudades`
--

DROP TABLE IF EXISTS `ciudades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ciudades` (
  `id` int NOT NULL,
  `ciudad` varchar(100) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:50
 
 
 
-- Tabla: categorias 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` int NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `activo` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:48
 
 
 
-- Tabla: marcas 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `marcas`
--

DROP TABLE IF EXISTS `marcas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marcas` (
  `id` int NOT NULL,
  `marca` varchar(50) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:47
 
 
 
-- Tabla: colores 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `colores`
--

DROP TABLE IF EXISTS `colores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colores` (
  `id` int NOT NULL,
  `color` varchar(50) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:48
 
 
 
-- Tabla: disenos 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `disenos`
--

DROP TABLE IF EXISTS `disenos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disenos` (
  `id` int NOT NULL,
  `diseno` varchar(50) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:50
 
 
 
-- Tabla: tamanos 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tamanos`
--

DROP TABLE IF EXISTS `tamanos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tamanos` (
  `id` int NOT NULL,
  `tamano` varchar(50) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:49
 
 
 
-- Tabla: monedas 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `monedas`
--

DROP TABLE IF EXISTS `monedas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `monedas` (
  `id` int NOT NULL,
  `moneda` varchar(100) DEFAULT NULL,
  `abreviatura` varchar(5) DEFAULT NULL,
  `decimales` int DEFAULT NULL,
  `activo` int NOT NULL DEFAULT '0',
  `cotizacion` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ukmoneda` (`abreviatura`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:46
 
 
 
-- Tabla: roles 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL,
  `rol` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_rol` (`rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:49
 
 
 
-- Tabla: menus 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` varchar(50) NOT NULL,
  `nombre` varchar(25) DEFAULT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `orden` int NOT NULL,
  `tab` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:48
 
 
 
-- Tabla: talonarios 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `talonarios`
--

DROP TABLE IF EXISTS `talonarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `talonarios` (
  `id` int NOT NULL,
  `tipo_comprobante` int NOT NULL,
  `serie_comprobante` varchar(10) DEFAULT NULL,
  `numero_timbrado` varchar(20) DEFAULT NULL,
  `fecha_inicio_timbrado` date DEFAULT NULL,
  `fecha_final_timbrado` date DEFAULT NULL,
  `numero_inicial` int NOT NULL,
  `numero_final` int NOT NULL,
  `numero_actual` int NOT NULL,
  `activo` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_fechas` BEFORE INSERT ON `talonarios` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_numeros` BEFORE INSERT ON `talonarios` FOR EACH ROW BEGIN
    IF NEW.numero_inicial >= NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número inicial debe ser menor que el número final.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_numero_actual_insert` BEFORE INSERT ON `talonarios` FOR EACH ROW BEGIN
    IF NEW.numero_actual < NEW.numero_inicial OR NEW.numero_actual > NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número actual debe estar entre el número inicial y el número final.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_longitud_al_insertar` BEFORE INSERT ON `talonarios` FOR EACH ROW BEGIN
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        IF LENGTH(NEW.numero_timbrado) != 8 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El numero_timbrado debe ser exactamente de 8 dígitos.';
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_unicidad` BEFORE INSERT ON `talonarios` FOR EACH ROW BEGIN
    DECLARE count INT DEFAULT 0;
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        SELECT COUNT(*) INTO count FROM TALONARIOS WHERE serie_comprobante = NEW.serie_comprobante AND numero_timbrado = NEW.numero_timbrado;
        IF count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La combinación de serie_comprobante y numero_timbrado ya existe.';
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_rangos_recibo` BEFORE INSERT ON `talonarios` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_fechas_update` BEFORE UPDATE ON `talonarios` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_numeros_update` BEFORE UPDATE ON `talonarios` FOR EACH ROW BEGIN
    IF NEW.numero_inicial >= NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número inicial debe ser menor que el número final.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_numero_actual_update` BEFORE UPDATE ON `talonarios` FOR EACH ROW BEGIN
    IF NEW.numero_actual < NEW.numero_inicial OR NEW.numero_actual > NEW.numero_final THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El número actual debe estar entre el número inicial y el número final.';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_longitud_al_actualizar` BEFORE UPDATE ON `talonarios` FOR EACH ROW BEGIN
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        IF LENGTH(NEW.numero_timbrado) != 8 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'El numero_timbrado debe ser exactamente de 8 dígitos.';
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_unicidad_update` BEFORE UPDATE ON `talonarios` FOR EACH ROW BEGIN
    DECLARE count INT DEFAULT 0;
    IF NEW.tipo_comprobante = 1 THEN -- Solo para Factura
        SELECT COUNT(*) INTO count FROM TALONARIOS WHERE serie_comprobante = NEW.serie_comprobante AND numero_timbrado = NEW.numero_timbrado AND id != NEW.id;
        IF count > 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La combinación de serie_comprobante y numero_timbrado ya existe.';
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `validar_rangos_recibo_update` BEFORE UPDATE ON `talonarios` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:47
 
 
 
-- Tabla: depositos 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `depositos`
--

DROP TABLE IF EXISTS `depositos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `depositos` (
  `id` int NOT NULL,
  `deposito` varchar(100) NOT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `contacto` varchar(200) DEFAULT NULL,
  `telefono` varchar(50) DEFAULT NULL,
  `sucursal_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sucursal_id` (`sucursal_id`),
  CONSTRAINT `depositos_ibfk_1` FOREIGN KEY (`sucursal_id`) REFERENCES `sucursales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:50
 
 
 
-- Tabla: productos 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` int NOT NULL,
  `producto` varchar(45) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `categoria_id` int NOT NULL,
  `marca_id` int NOT NULL,
  `moneda_id` int NOT NULL,
  `impuesto` int NOT NULL DEFAULT '0',
  `servicio` int NOT NULL DEFAULT '0',
  `pesable` int NOT NULL DEFAULT '0',
  `perecedero` int NOT NULL DEFAULT '0',
  `activo` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_producto_categoria` (`categoria_id`),
  KEY `fk_producto_marca` (`marca_id`),
  KEY `fk_producto_monedas` (`moneda_id`),
  CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `fk_producto_marca` FOREIGN KEY (`marca_id`) REFERENCES `marcas` (`id`),
  CONSTRAINT `fk_producto_monedas` FOREIGN KEY (`moneda_id`) REFERENCES `monedas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:46
 
 
 
-- Tabla: precios 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `precios`
--

DROP TABLE IF EXISTS `precios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `precios` (
  `id` int NOT NULL,
  `listaprecio` varchar(255) DEFAULT NULL,
  `moneda_id` int DEFAULT NULL,
  `activo` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_precio_moneda` (`moneda_id`),
  CONSTRAINT `fk_precio_moneda` FOREIGN KEY (`moneda_id`) REFERENCES `monedas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:48
 
 
 
-- Tabla: usuarios 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL,
  `rol_id` int DEFAULT NULL,
  `usuario` varchar(100) NOT NULL,
  `contraseña` varchar(200) NOT NULL,
  `activo` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fkusuario_rol` (`rol_id`),
  CONSTRAINT `fkusuario_rol` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:49
 
 
 
-- Tabla: clientes 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clientes`
--

DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id` int NOT NULL,
  `nrodocumento` varchar(20) DEFAULT NULL,
  `tipodocumento` varchar(3) DEFAULT NULL,
  `cliente` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `celular` varchar(15) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `correo` varchar(50) DEFAULT NULL,
  `ciudad_id` int NOT NULL,
  `precio_id` int DEFAULT NULL,
  `fecha` datetime NOT NULL,
  `activo` int NOT NULL DEFAULT '0',
  `divisoria` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cliente_precio` (`precio_id`),
  KEY `fk_cliente_ciudad` (`ciudad_id`),
  CONSTRAINT `fk_cliente_ciudad` FOREIGN KEY (`ciudad_id`) REFERENCES `ciudades` (`id`),
  CONSTRAINT `fk_cliente_precio` FOREIGN KEY (`precio_id`) REFERENCES `precios` (`id`),
  CONSTRAINT `chk_tipodocumentoclientes` CHECK ((`tipodocumento` in (_utf8mb4'CI',_utf8mb4'RUC')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:50
 
 
 
-- Tabla: proveedores 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `proveedores`
--

DROP TABLE IF EXISTS `proveedores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proveedores` (
  `id` int NOT NULL,
  `fecha` datetime NOT NULL,
  `proveedor` varchar(100) NOT NULL,
  `nrodocumento` varchar(20) DEFAULT NULL,
  `celular` varchar(15) NOT NULL,
  `direccion` varchar(500) NOT NULL,
  `ciudad_id` int NOT NULL,
  `activo` int NOT NULL DEFAULT '0',
  `tipodocumento` varchar(3) DEFAULT NULL,
  `divisoria` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkproveed_ciudad` (`ciudad_id`),
  CONSTRAINT `fkproveed_ciudad` FOREIGN KEY (`ciudad_id`) REFERENCES `ciudades` (`id`),
  CONSTRAINT `ch_tipodocumento` CHECK ((`tipodocumento` in (_utf8mb4'CI',_utf8mb4'RUC'))),
  CONSTRAINT `chk_tipodocumento` CHECK ((`tipodocumento` in (_utf8mb4'CI',_utf8mb4'RUC')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:48
 
 
 
-- Tabla: cuotas 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cuotas`
--

DROP TABLE IF EXISTS `cuotas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuotas` (
  `id` int NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `cuotas` int DEFAULT NULL,
  `irregular` int DEFAULT NULL,
  `activo` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:49
 
 
 
-- Tabla: productos_detalle 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `productos_detalle`
--

DROP TABLE IF EXISTS `productos_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos_detalle` (
  `id` int NOT NULL,
  `cabecera_id` int NOT NULL,
  `codigobarras` varchar(20) NOT NULL,
  `color_id` int DEFAULT NULL,
  `tamano_id` int DEFAULT NULL,
  `diseno_id` int DEFAULT NULL,
  `costo` decimal(18,5) NOT NULL,
  `uxb` decimal(18,5) NOT NULL DEFAULT '1.00000',
  `stockminimo` decimal(18,5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_producto` (`codigobarras`),
  KEY `fk_producto_detalle_cab` (`cabecera_id`),
  KEY `fk_producto_detalle_col` (`color_id`),
  KEY `fk_producto_detalle_tamano` (`tamano_id`),
  KEY `fk_producto_detalle_diseno` (`diseno_id`),
  CONSTRAINT `fk_producto_detalle_cab` FOREIGN KEY (`cabecera_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `fk_producto_detalle_col` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`),
  CONSTRAINT `fk_producto_detalle_diseno` FOREIGN KEY (`diseno_id`) REFERENCES `disenos` (`id`),
  CONSTRAINT `fk_producto_detalle_tamano` FOREIGN KEY (`tamano_id`) REFERENCES `tamanos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_insert_productos_detalle` BEFORE INSERT ON `productos_detalle` FOR EACH ROW BEGIN
    -- Asegurarse de que no haya duplicados en PRECIOS_DETALLE
    DECLARE existing_count INT;
    SELECT COUNT(*) INTO existing_count
    FROM PRECIOS_DETALLE
    WHERE productoDetalle_id = NEW.codigobarras;

    IF existing_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Código de barras ya existe en PRECIOS_DETALLE';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_update_productos_detalle` BEFORE UPDATE ON `productos_detalle` FOR EACH ROW BEGIN
    -- Actualizar las referencias en PRECIOS_DETALLE
    UPDATE PRECIOS_DETALLE
    SET productoDetalle_id = NEW.codigobarras
    WHERE productoDetalle_id = OLD.codigobarras;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:48
 
 
 
-- Tabla: precios_detalle 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `precios_detalle`
--

DROP TABLE IF EXISTS `precios_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `precios_detalle` (
  `id` int NOT NULL,
  `precio_id` int NOT NULL,
  `productoDetalle_id` varchar(20) NOT NULL,
  `precio` decimal(12,6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_precio_detalle_precio` (`precio_id`),
  KEY `fk_precio_detalle_productoDetalle` (`productoDetalle_id`),
  CONSTRAINT `fk_precio_detalle_precio` FOREIGN KEY (`precio_id`) REFERENCES `precios` (`id`),
  CONSTRAINT `fk_precio_detalle_productoDetalle` FOREIGN KEY (`productoDetalle_id`) REFERENCES `productos_detalle` (`codigobarras`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:47
 
 
 
-- Tabla: cuotas_detalle 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cuotas_detalle`
--

DROP TABLE IF EXISTS `cuotas_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuotas_detalle` (
  `id` int NOT NULL,
  `cuota_id` int DEFAULT NULL,
  `cuota` int DEFAULT NULL,
  `dias` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkdetcuota` (`cuota_id`),
  CONSTRAINT `fkdetcuota` FOREIGN KEY (`cuota_id`) REFERENCES `cuotas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:46
 
 
 
-- Tabla: permisos 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `permisos`
--

DROP TABLE IF EXISTS `permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permisos` (
  `rol_id` int NOT NULL,
  `menu_id` varchar(50) NOT NULL,
  PRIMARY KEY (`rol_id`,`menu_id`),
  KEY `fk_menu_permiso` (`menu_id`),
  CONSTRAINT `fk_menu_permiso` FOREIGN KEY (`menu_id`) REFERENCES `menus` (`id`),
  CONSTRAINT `fk_rol_permiso` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:49
 
 
 
-- Tabla: stocks 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `stocks`
--

DROP TABLE IF EXISTS `stocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stocks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `producto_detalle` varchar(75) NOT NULL,
  `deposito_id` int DEFAULT NULL,
  `stockActual` int NOT NULL,
  `ultimaCompra` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `producto_detalle` (`producto_detalle`),
  KEY `deposito_id` (`deposito_id`),
  CONSTRAINT `stocks_ibfk_1` FOREIGN KEY (`producto_detalle`) REFERENCES `productos_detalle` (`codigobarras`),
  CONSTRAINT `stocks_ibfk_2` FOREIGN KEY (`deposito_id`) REFERENCES `depositos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:46
 
 
 
-- Tabla: lotes 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `lotes`
--

DROP TABLE IF EXISTS `lotes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lotes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `productodetalle_id` varchar(20) NOT NULL,
  `numeroLote` varchar(20) NOT NULL,
  `fechaVencimiento` date DEFAULT NULL,
  `stockLote` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fkvtadetprod` (`productodetalle_id`),
  CONSTRAINT `fkvtadetprod` FOREIGN KEY (`productodetalle_id`) REFERENCES `productos_detalle` (`codigobarras`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:47
 
 
 
-- Tabla: ajustes_stock 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ajustes_stock`
--

DROP TABLE IF EXISTS `ajustes_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ajustes_stock` (
  `id` int NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `motivo` varchar(20) DEFAULT NULL,
  `deposito_id` int DEFAULT NULL,
  `aprobado` int NOT NULL DEFAULT '0',
  `contabilizado` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `deposito_id` (`deposito_id`),
  CONSTRAINT `ajustes_stock_ibfk_1` FOREIGN KEY (`deposito_id`) REFERENCES `depositos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:51
 
 
 
-- Tabla: compras 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `compras`
--

DROP TABLE IF EXISTS `compras`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compras` (
  `id` int NOT NULL,
  `fechaFactura` date DEFAULT NULL,
  `proveedor_id` int DEFAULT NULL,
  `factura` varchar(20) DEFAULT NULL,
  `moneda_id` int DEFAULT NULL,
  `deposito_id` int DEFAULT NULL,
  `tipo_documento` int DEFAULT NULL,
  `cuota_id` int DEFAULT NULL,
  `total_neto` decimal(18,5) DEFAULT NULL,
  `total_bruto` decimal(18,5) DEFAULT NULL,
  `total_impuesto` decimal(18,5) DEFAULT NULL,
  `total_exento` decimal(18,5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkCompraProveedor` (`proveedor_id`),
  KEY `fkCompraMoneda` (`moneda_id`),
  KEY `fkCompraCuota` (`cuota_id`),
  KEY `fkCompraDeposito` (`deposito_id`),
  CONSTRAINT `fkCompraCuota` FOREIGN KEY (`cuota_id`) REFERENCES `cuotas` (`id`),
  CONSTRAINT `fkCompraDeposito` FOREIGN KEY (`deposito_id`) REFERENCES `depositos` (`id`),
  CONSTRAINT `fkCompraMoneda` FOREIGN KEY (`moneda_id`) REFERENCES `monedas` (`id`),
  CONSTRAINT `fkCompraProveedor` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `compra_insert` AFTER INSERT ON `compras` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `compra_update` BEFORE UPDATE ON `compras` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `compra_delete` BEFORE DELETE ON `compras` FOR EACH ROW BEGIN
    -- Paso 1: Eliminar los detalles de la compra relacionados
    DELETE FROM COMPRAS_DETALLE 
    WHERE compra_id = OLD.id;

    -- Paso 2: Eliminar los detalles de los pagos relacionados con las cuentas a pagar de esta factura
    DELETE FROM PAGOS_DETALLE
    WHERE cuenta_pagar_id IN (
        SELECT id
        FROM CUENTAS_PAGAR
        WHERE factura_id = OLD.id
    );

    -- Paso 3: Eliminar los pagos relacionados con los detalles eliminados anteriormente
    DELETE FROM PAGOS
    WHERE id IN (
        SELECT DISTINCT pago_id
        FROM PAGOS_DETALLE
        WHERE cuenta_pagar_id IN (
            SELECT id
            FROM CUENTAS_PAGAR
            WHERE factura_id = OLD.id
        )
    );

    -- Paso 4: Eliminar las cuentas a pagar relacionadas con la factura
    DELETE FROM CUENTAS_PAGAR
    WHERE factura_id = OLD.id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:49
 
 
 
-- Tabla: ventas 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas` (
  `id` int NOT NULL,
  `fechaFactura` date DEFAULT NULL,
  `fechaProceso` datetime DEFAULT NULL,
  `cliente_id` int DEFAULT NULL,
  `nro_documento` int DEFAULT NULL,
  `serie` varchar(10) NOT NULL,
  `moneda_id` int DEFAULT NULL,
  `deposito_id` int DEFAULT NULL,
  `tipo_documento` int DEFAULT NULL,
  `cuota_id` int DEFAULT NULL,
  `total_neto` decimal(18,5) DEFAULT NULL,
  `total_bruto` decimal(18,5) DEFAULT NULL,
  `total_impuesto` decimal(18,5) DEFAULT NULL,
  `total_exento` decimal(18,5) DEFAULT NULL,
  `impreso` int NOT NULL DEFAULT '0',
  `anulado` int NOT NULL DEFAULT '0',
  `talonario_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_cli_venta` (`cliente_id`),
  KEY `fk_moneda_venta` (`moneda_id`),
  KEY `fk_cuota_venta` (`cuota_id`),
  KEY `fk_deposito_venta` (`deposito_id`),
  KEY `fk_talonario_venta` (`talonario_id`),
  CONSTRAINT `fk_cli_venta` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `fk_cuota_venta` FOREIGN KEY (`cuota_id`) REFERENCES `cuotas` (`id`),
  CONSTRAINT `fk_deposito_venta` FOREIGN KEY (`deposito_id`) REFERENCES `depositos` (`id`),
  CONSTRAINT `fk_moneda_venta` FOREIGN KEY (`moneda_id`) REFERENCES `monedas` (`id`),
  CONSTRAINT `fk_talonario_venta` FOREIGN KEY (`talonario_id`) REFERENCES `talonarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_ventas_insert` BEFORE INSERT ON `ventas` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `ventas_before_insert` BEFORE INSERT ON `ventas` FOR EACH ROW BEGIN
    DECLARE msg VARCHAR(300);

    -- Verificar que no se pueda insertar un registro con anulado = 1
    IF NEW.anulado = 1 THEN
        SET msg = 'No se puede anular una venta que no ha sido impresa.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `ventas_insert` AFTER INSERT ON `ventas` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `ventas_update` BEFORE UPDATE ON `ventas` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `ventas_before_update` BEFORE UPDATE ON `ventas` FOR EACH ROW BEGIN
    DECLARE msg VARCHAR(300);

    -- Verificar que solo se pueda anular un registro si impreso = 1
    IF NEW.anulado = 1 AND OLD.impreso = 0 THEN
        SET msg = 'No se puede anular un registro que no ha sido impreso.';
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = msg;
    END IF;

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `after_ventas_update` AFTER UPDATE ON `ventas` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `ventas_before_delete` BEFORE DELETE ON `ventas` FOR EACH ROW BEGIN
    DECLARE cuentaId INT;
    DECLARE cobroId INT;

    -- Verificar si el registro está marcado como impreso
    IF OLD.impreso = 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede eliminar una venta que ya está marcada como impresa.';
    END IF;

    -- Verificar si existen registros relacionados en CUENTAS_COBRAR
    IF EXISTS (SELECT 1 FROM CUENTAS_COBRAR WHERE venta_id = OLD.id) THEN
        -- Eliminar los detalles de cobros relacionados
        DELETE FROM COBROS_DETALLE 
        WHERE cuenta_cobrar_id IN (
            SELECT id FROM CUENTAS_COBRAR WHERE venta_id = OLD.id
        );

        -- Eliminar los cobros relacionados
        DELETE FROM COBROS 
        WHERE id IN (
            SELECT cobro_id 
            FROM COBROS_DETALLE 
            WHERE cuenta_cobrar_id IN (
                SELECT id FROM CUENTAS_COBRAR WHERE venta_id = OLD.id
            )
        );

        -- Eliminar las cuentas por cobrar asociadas
        DELETE FROM CUENTAS_COBRAR WHERE venta_id = OLD.id;
    END IF;

    -- Verificar si existen detalles de la venta y eliminarlos
    IF EXISTS (SELECT 1 FROM VENTAS_DETALLE WHERE venta_id = OLD.id) THEN
        DELETE FROM VENTAS_DETALLE WHERE venta_id = OLD.id;
    END IF;

    -- Después de validar y eliminar dependencias, la cabecera se eliminará automáticamente
    -- debido a que este trigger se ejecuta en el evento BEFORE DELETE.

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:47
 
 
 
-- Tabla: cotizaciones 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cotizaciones`
--

DROP TABLE IF EXISTS `cotizaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cotizaciones` (
  `id` int NOT NULL,
  `moneda_id` int DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `compra` decimal(18,5) DEFAULT NULL,
  `venta` decimal(18,5) DEFAULT NULL,
  `activo` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `moneda_id` (`moneda_id`),
  CONSTRAINT `cotizaciones_ibfk_1` FOREIGN KEY (`moneda_id`) REFERENCES `monedas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:47
 
 
 
-- Tabla: ajustes_stock_detalle 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ajustes_stock_detalle`
--

DROP TABLE IF EXISTS `ajustes_stock_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ajustes_stock_detalle` (
  `id` int NOT NULL,
  `ajuste_id` int DEFAULT NULL,
  `productodetalle_id` varchar(20) DEFAULT NULL,
  `cantidad_actual` decimal(18,5) DEFAULT NULL,
  `cantidad_ajuste` decimal(18,5) DEFAULT NULL,
  `lote` varchar(20) NOT NULL,
  `lotevence` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ajuste_id` (`ajuste_id`,`productodetalle_id`),
  KEY `fk_ajustesdetalles_productoDetalle` (`productodetalle_id`),
  CONSTRAINT `ajustes_stock_detalle_ibfk_1` FOREIGN KEY (`ajuste_id`) REFERENCES `ajustes_stock` (`id`),
  CONSTRAINT `fk_ajustesdetalles_productoDetalle` FOREIGN KEY (`productodetalle_id`) REFERENCES `productos_detalle` (`codigobarras`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:50
 
 
 
-- Tabla: compras_detalle 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `compras_detalle`
--

DROP TABLE IF EXISTS `compras_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compras_detalle` (
  `id` int NOT NULL,
  `compra_id` int NOT NULL,
  `productodetalle_id` varchar(20) DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `precio` decimal(18,5) NOT NULL,
  `impuesto` decimal(18,5) NOT NULL,
  `descuento` decimal(18,5) DEFAULT NULL,
  `base` decimal(18,5) DEFAULT NULL,
  `total` decimal(18,5) NOT NULL,
  `lote` varchar(20) DEFAULT NULL,
  `vencimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `compra_id` (`compra_id`),
  KEY `fkvtadetprodcompras` (`productodetalle_id`),
  CONSTRAINT `compras_detalle_ibfk_1` FOREIGN KEY (`compra_id`) REFERENCES `compras` (`id`),
  CONSTRAINT `fkvtadetprodcompras` FOREIGN KEY (`productodetalle_id`) REFERENCES `productos_detalle` (`codigobarras`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `compras_detalle_insert` AFTER INSERT ON `compras_detalle` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `compras_detalle_update` AFTER UPDATE ON `compras_detalle` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `compras_detalle_delete` AFTER DELETE ON `compras_detalle` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:51
 
 
 
-- Tabla: ventas_detalle 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ventas_detalle`
--

DROP TABLE IF EXISTS `ventas_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas_detalle` (
  `id` int NOT NULL,
  `venta_id` int DEFAULT NULL,
  `productodetalle_id` varchar(20) DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `precio` decimal(18,5) DEFAULT NULL,
  `impuesto` decimal(18,5) DEFAULT NULL,
  `descuento` decimal(18,5) DEFAULT NULL,
  `base` decimal(18,5) DEFAULT NULL,
  `total` decimal(18,5) DEFAULT NULL,
  `lote_id` int DEFAULT NULL,
  `vencimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkvtadetallprod` (`productodetalle_id`),
  KEY `fkvtadetpventa` (`venta_id`),
  KEY `fkvtalote` (`lote_id`),
  CONSTRAINT `fkvtadetallprod` FOREIGN KEY (`productodetalle_id`) REFERENCES `productos_detalle` (`codigobarras`),
  CONSTRAINT `fkvtadetpventa` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `fkvtalote` FOREIGN KEY (`lote_id`) REFERENCES `lotes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_before_insert_ventasdetalle` BEFORE INSERT ON `ventas_detalle` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_before_update_ventasdetalle` BEFORE UPDATE ON `ventas_detalle` FOR EACH ROW BEGIN
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
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `trg_before_delete_ventasdetalle` BEFORE DELETE ON `ventas_detalle` FOR EACH ROW BEGIN
    -- Restaurar el stock del lote
    UPDATE LOTES
    SET stockLote = stockLote + OLD.cantidad
    WHERE id = OLD.lote_id;

    -- Restaurar el stock general
    UPDATE STOCKS
    SET stockActual = stockActual + OLD.cantidad
    WHERE producto_detalle = OLD.productodetalle_id
      AND deposito_id = (SELECT deposito_id FROM VENTAS WHERE id = OLD.venta_id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:49
 
 
 
-- Tabla: cuentas_cobrar 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cuentas_cobrar`
--

DROP TABLE IF EXISTS `cuentas_cobrar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuentas_cobrar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cuotas` int NOT NULL,
  `cuota` int NOT NULL,
  `importe` decimal(18,5) NOT NULL,
  `vencimiento` date NOT NULL,
  `cobrado` decimal(18,5) NOT NULL DEFAULT '0.00000',
  `venta_id` int NOT NULL,
  `estado` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_cuota` (`venta_id`,`cuota`),
  CONSTRAINT `fkcuentcoventas` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:46
 
 
 
-- Tabla: cuentas_pagar 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cuentas_pagar`
--

DROP TABLE IF EXISTS `cuentas_pagar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuentas_pagar` (
  `id` int NOT NULL AUTO_INCREMENT,
  `proveedor_id` int DEFAULT NULL,
  `factura_id` int DEFAULT NULL,
  `cuotas` int DEFAULT NULL,
  `cuota` int DEFAULT NULL,
  `cuotas_id` int DEFAULT NULL,
  `importe` decimal(18,5) DEFAULT NULL,
  `vencimiento` date DEFAULT NULL,
  `pagado` decimal(18,5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkproveedor` (`proveedor_id`),
  KEY `fkfactura` (`factura_id`),
  KEY `fkcuentapagarcuota` (`cuotas_id`),
  CONSTRAINT `fkcuentapagarcuota` FOREIGN KEY (`cuotas_id`) REFERENCES `cuotas` (`id`),
  CONSTRAINT `fkfactura` FOREIGN KEY (`factura_id`) REFERENCES `compras` (`id`),
  CONSTRAINT `fkproveedor` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:48
 
 
 
-- Tabla: notas_credito 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `notas_credito`
--

DROP TABLE IF EXISTS `notas_credito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notas_credito` (
  `id` int NOT NULL,
  `venta_id` int NOT NULL,
  `cliente_id` int NOT NULL,
  `deposito_id` int NOT NULL,
  `moneda_id` int NOT NULL,
  `factura` varchar(50) NOT NULL,
  `serie` varchar(10) NOT NULL,
  `nro_documento` int DEFAULT NULL,
  `fecha` date NOT NULL,
  `motivo` varchar(100) DEFAULT NULL,
  `total_neto` decimal(18,5) NOT NULL,
  `total_impuesto` decimal(18,5) NOT NULL,
  `total_exento` decimal(18,5) NOT NULL,
  `total_bruto` decimal(18,5) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `venta_id` (`venta_id`),
  KEY `cliente_id` (`cliente_id`),
  KEY `deposito_id` (`deposito_id`),
  KEY `moneda_id` (`moneda_id`),
  CONSTRAINT `notas_credito_ibfk_1` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `notas_credito_ibfk_2` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `notas_credito_ibfk_3` FOREIGN KEY (`deposito_id`) REFERENCES `depositos` (`id`),
  CONSTRAINT `notas_credito_ibfk_4` FOREIGN KEY (`moneda_id`) REFERENCES `monedas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:50
 
 
 
-- Tabla: notas_credito_detalle 
---------------------------------------- 
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: inventorysystem
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `notas_credito_detalle`
--

DROP TABLE IF EXISTS `notas_credito_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notas_credito_detalle` (
  `id` int NOT NULL,
  `nota_credito_id` int NOT NULL,
  `venta_detalle_id` int NOT NULL,
  `productodetalle_id` varchar(20) NOT NULL,
  `cantidad` decimal(18,5) NOT NULL,
  `precio` decimal(18,5) NOT NULL,
  `descuento` decimal(18,5) DEFAULT NULL,
  `impuesto` decimal(18,5) NOT NULL,
  `subtotal` decimal(18,5) NOT NULL,
  `lote_id` int DEFAULT NULL,
  `vencimiento` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `nota_credito_id` (`nota_credito_id`),
  KEY `venta_detalle_id` (`venta_detalle_id`),
  KEY `productodetalle_id` (`productodetalle_id`),
  KEY `fkntalote` (`lote_id`),
  CONSTRAINT `fkntalote` FOREIGN KEY (`lote_id`) REFERENCES `lotes` (`id`),
  CONSTRAINT `notas_credito_detalle_ibfk_1` FOREIGN KEY (`nota_credito_id`) REFERENCES `notas_credito` (`id`),
  CONSTRAINT `notas_credito_detalle_ibfk_2` FOREIGN KEY (`venta_detalle_id`) REFERENCES `ventas_detalle` (`id`),
  CONSTRAINT `notas_credito_detalle_ibfk_3` FOREIGN KEY (`productodetalle_id`) REFERENCES `productos_detalle` (`codigobarras`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-02 15:09:50
 
 
