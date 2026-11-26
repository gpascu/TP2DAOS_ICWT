-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tp2daos_icwt_g
-- ------------------------------------------------------
-- Server version	8.0.43

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
-- Table structure for table `contrato`
--

DROP TABLE IF EXISTS `contrato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contrato` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(255) DEFAULT NULL,
  `dia_vencimiento` int DEFAULT NULL,
  `direccion_propiedad` varchar(255) DEFAULT NULL,
  `duracion_meses` int DEFAULT NULL,
  `eliminado` bit(1) NOT NULL,
  `estado` enum('ACTIVO','BORRADOR','FINALIZADO','RESCINDIDO') DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `importe_mensual` decimal(38,2) DEFAULT NULL,
  `inquilino` varchar(255) DEFAULT NULL,
  `propietario` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contrato`
--

LOCK TABLES `contrato` WRITE;
/*!40000 ALTER TABLE `contrato` DISABLE KEYS */;
INSERT INTO `contrato` VALUES (1,'Depto 2 ambientes',10,'Bv. Gálvez 1550, Santa Fe',12,_binary '\0','ACTIVO','2026-01-01',350000.00,'Juan Perez','Maria Gomez'),(2,'Casa 3 ambientes con patio',31,'San Martín 2340, Santo Tomé',12,_binary '\0','ACTIVO','2026-06-15',420000.00,'Lucia Fernandez','Carlos Ruiz'),(3,'Local comercial',5,'Urquiza 980, Santa Fe',12,_binary '\0','FINALIZADO','2024-03-01',280000.00,'Pedro Sanchez','Ana Torres'),(4,'Contrato creado por la coleccion S01',28,'Calle S01 123',12,_binary '\0','ACTIVO','2026-11-26',350000.00,'Inquilino S01','Propietario S01'),(5,'Contrato para probar el ultimo dia de febrero',31,'Calle S01 456',12,_binary '\0','ACTIVO','2026-11-26',350000.00,'Inquilino S01 b','Propietario S01 b'),(6,'Dato de prueba de la Actividad 02',10,'Calle Prueba S03 123',12,_binary '\0','ACTIVO','2026-11-26',350000.00,'Inquilino Prueba S03','Propietario Prueba S03'),(7,'Dato de prueba de la Actividad 02',10,'Calle Prueba S03 123',1,_binary '\0','ACTIVO','2000-01-01',350000.00,'Inquilino Prueba S03','Propietario Prueba S03'),(8,'Preparacion minima de casos S04',10,'Deposito prueba S04 1795715194203',12,_binary '\0','ACTIVO','2026-10-27',250000.00,'Inquilino S04 1795715194203','Propietario S04 1795715194203');
/*!40000 ALTER TABLE `contrato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `factura`
--

DROP TABLE IF EXISTS `factura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `factura` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `concepto` varchar(255) DEFAULT NULL,
  `estado` enum('ANULADA','PAGADA','PENDIENTE','VENCIDA') DEFAULT NULL,
  `fecha_emision` date DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `importe` decimal(38,2) DEFAULT NULL,
  `mensual` bit(1) NOT NULL,
  `periodo` varchar(255) DEFAULT NULL,
  `contrato_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhrgglc2vv23cgd9xn6vnj14qn` (`contrato_id`),
  CONSTRAINT `FKhrgglc2vv23cgd9xn6vnj14qn` FOREIGN KEY (`contrato_id`) REFERENCES `contrato` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `factura`
--

LOCK TABLES `factura` WRITE;
/*!40000 ALTER TABLE `factura` DISABLE KEYS */;
INSERT INTO `factura` VALUES (1,'Alquiler mensual 2026-12','ANULADA','2026-11-26','2026-12-28',350000.00,_binary '','2026-12',4),(2,'Alquiler mensual 2026-12','PENDIENTE','2026-11-26','2026-12-28',350000.00,_binary '','2026-12',4),(3,'Alquiler mensual 2027-02','PENDIENTE','2026-11-26','2027-02-28',350000.00,_binary '','2027-02',5),(4,'Alquiler mensual 2026-11','PAGADA','2026-11-01','2026-11-10',350000.00,_binary '\0','2026-11',1);
/*!40000 ALTER TABLE `factura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_estado_incidente`
--

DROP TABLE IF EXISTS `historial_estado_incidente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_estado_incidente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `estado_anterior` enum('ABIERTO','CANCELADO','EN_PROCESO','REABIERTO','RESUELTO') DEFAULT NULL,
  `estado_nuevo` enum('ABIERTO','CANCELADO','EN_PROCESO','REABIERTO','RESUELTO') DEFAULT NULL,
  `fecha_hora` datetime(6) DEFAULT NULL,
  `incidente_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5xyt388ahfjjx94qj3v9kb1jb` (`incidente_id`),
  CONSTRAINT `FK5xyt388ahfjjx94qj3v9kb1jb` FOREIGN KEY (`incidente_id`) REFERENCES `incidente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_estado_incidente`
--

LOCK TABLES `historial_estado_incidente` WRITE;
/*!40000 ALTER TABLE `historial_estado_incidente` DISABLE KEYS */;
INSERT INTO `historial_estado_incidente` VALUES (1,NULL,'ABIERTO','2026-11-26 17:43:51.211668',1),(2,'ABIERTO','EN_PROCESO','2026-11-26 17:43:52.028771',1),(3,'EN_PROCESO','CANCELADO','2026-11-26 17:43:52.378405',1),(4,NULL,'ABIERTO','2026-11-26 17:46:34.307934',2),(5,'ABIERTO','EN_PROCESO','2026-11-26 17:46:34.388199',2),(6,'EN_PROCESO','RESUELTO','2026-11-26 17:46:34.689085',2);
/*!40000 ALTER TABLE `historial_estado_incidente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incidente`
--

DROP TABLE IF EXISTS `incidente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `incidente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `categoria` enum('ELECTRICIDAD','GAS','GENERAL','PLOMERIA') DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `estado` enum('ABIERTO','CANCELADO','EN_PROCESO','REABIERTO','RESUELTO') DEFAULT NULL,
  `fecha_hora_alta` datetime(6) DEFAULT NULL,
  `prioridad` enum('ALTA','BAJA','MEDIA') DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `contrato_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr91hhulj5cs4i6nlo136g60hr` (`contrato_id`),
  CONSTRAINT `FKr91hhulj5cs4i6nlo136g60hr` FOREIGN KEY (`contrato_id`) REFERENCES `contrato` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incidente`
--

LOCK TABLES `incidente` WRITE;
/*!40000 ALTER TABLE `incidente` DISABLE KEYS */;
INSERT INTO `incidente` VALUES (1,'PLOMERIA','Sale agua debajo de la pileta.','CANCELADO','2026-11-26 17:43:51.211668','MEDIA','Pérdida en la cocina',6),(2,'PLOMERIA','Se detecto perdida de agua bajo mesada','RESUELTO','2026-11-26 17:46:34.307934','MEDIA','Filtracion de cocina S04 1795715194203',8);
/*!40000 ALTER TABLE `incidente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `informe_resolucion`
--

DROP TABLE IF EXISTS `informe_resolucion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `informe_resolucion` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `costo` decimal(38,2) DEFAULT NULL,
  `descripcion_trabajo` varchar(255) DEFAULT NULL,
  `fecha_hora_resolucion` datetime(6) DEFAULT NULL,
  `observaciones` varchar(255) DEFAULT NULL,
  `responsable` varchar(255) DEFAULT NULL,
  `incidente_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7eenmn0hmg7hkuvavrwmvgtqh` (`incidente_id`),
  CONSTRAINT `FK7eenmn0hmg7hkuvavrwmvgtqh` FOREIGN KEY (`incidente_id`) REFERENCES `incidente` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `informe_resolucion`
--

LOCK TABLES `informe_resolucion` WRITE;
/*!40000 ALTER TABLE `informe_resolucion` DISABLE KEYS */;
INSERT INTO `informe_resolucion` VALUES (1,12500.00,'Se reemplazo flexible y verifico funcionamiento','2026-11-26 17:46:34.689085','Sin filtraciones al finalizar','Tecnico S04',2);
/*!40000 ALTER TABLE `informe_resolucion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pago`
--

DROP TABLE IF EXISTS `pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pago` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `fecha_pago` date DEFAULT NULL,
  `importe_pagado` decimal(38,2) DEFAULT NULL,
  `interes` decimal(38,2) DEFAULT NULL,
  `medio` enum('CREDITO','DEBITO','EFECTIVO','OTROS','TRANSFERENCIA') DEFAULT NULL,
  `factura_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKknnq77eqc7tg3793mbed1pcx4` (`factura_id`),
  CONSTRAINT `FKrtu0je6xnvrvwuflslyslq2eo` FOREIGN KEY (`factura_id`) REFERENCES `factura` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago`
--

LOCK TABLES `pago` WRITE;
/*!40000 ALTER TABLE `pago` DISABLE KEYS */;
INSERT INTO `pago` VALUES (1,'2026-11-26',355000.00,5000.00,'TRANSFERENCIA',4);
/*!40000 ALTER TABLE `pago` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-26 19:08:48
