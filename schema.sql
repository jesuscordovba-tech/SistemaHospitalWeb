/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-12.0.2-MariaDB, for osx10.20 (arm64)
--
-- ------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `atenciones`
--

DROP TABLE IF EXISTS `atenciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `atenciones` (
  `id_atencion` int(11) NOT NULL AUTO_INCREMENT,
  `id_cita` int(11) NOT NULL,
  `id_paciente` int(11) NOT NULL,
  `id_medico` int(11) NOT NULL,
  `diagnostico` text DEFAULT NULL,
  `tratamiento` text DEFAULT NULL,
  `receta` text DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `fecha_atencion` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_atencion`),
  KEY `id_cita` (`id_cita`),
  KEY `id_medico` (`id_medico`),
  KEY `idx_atenciones_paciente` (`id_paciente`),
  CONSTRAINT `atenciones_ibfk_1` FOREIGN KEY (`id_cita`) REFERENCES `citas` (`id_cita`) ON DELETE CASCADE,
  CONSTRAINT `atenciones_ibfk_2` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`) ON DELETE CASCADE,
  CONSTRAINT `atenciones_ibfk_3` FOREIGN KEY (`id_medico`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bitacora`
--

DROP TABLE IF EXISTS `bitacora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `bitacora` (
  `id_bitacora` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) DEFAULT NULL,
  `accion` varchar(255) DEFAULT NULL,
  `fecha_accion` datetime DEFAULT current_timestamp(),
  `detalle` text DEFAULT NULL,
  PRIMARY KEY (`id_bitacora`),
  KEY `id_usuario` (`id_usuario`),
  CONSTRAINT `bitacora_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `id_cita` int(11) NOT NULL AUTO_INCREMENT,
  `id_paciente` int(11) NOT NULL,
  `id_medico` int(11) NOT NULL,
  `fecha_cita` date NOT NULL,
  `hora_cita` time NOT NULL,
  `motivo` text DEFAULT NULL,
  `estado` enum('Pendiente','Confirmada','Cancelada','Realizada') NOT NULL,
  `observaciones` text DEFAULT NULL,
  PRIMARY KEY (`id_cita`),
  KEY `idx_citas_medico_fecha_hora` (`id_medico`,`fecha_cita`,`hora_cita`),
  KEY `idx_citas_paciente_fecha` (`id_paciente`,`fecha_cita`),
  KEY `idx_citas_medico_fecha` (`id_medico`,`fecha_cita`),
  CONSTRAINT `citas_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_medico` FOREIGN KEY (`id_medico`) REFERENCES `usuarios` (`id_usuario`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=505 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trg_citas_bitacora
AFTER INSERT ON citas
FOR EACH ROW
BEGIN
    INSERT INTO bitacora (id_usuario, accion, detalle, fecha_accion)
    VALUES (NEW.id_medico, 'INSERT', CONCAT('Nueva cita ID ', NEW.id_cita), NOW());
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `laboratorios`
--

DROP TABLE IF EXISTS `laboratorios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `laboratorios` (
  `id_laboratorio` int(11) NOT NULL AUTO_INCREMENT,
  `id_paciente` int(11) NOT NULL,
  `id_medico` int(11) DEFAULT NULL,
  `tipo_examen` varchar(100) NOT NULL,
  `fecha_solicitud` date DEFAULT curdate(),
  `estado` enum('Pendiente','Procesando','Completo') NOT NULL DEFAULT 'Pendiente',
  PRIMARY KEY (`id_laboratorio`),
  KEY `idx_lab_paciente_estado` (`id_paciente`,`estado`),
  KEY `idx_lab_fecha` (`fecha_solicitud`),
  KEY `idx_lab_medico` (`id_medico`),
  KEY `idx_labs_paciente_estado` (`id_paciente`,`estado`),
  CONSTRAINT `fk_labs_medico` FOREIGN KEY (`id_medico`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `laboratorios_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=183 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `pacientes` (
  `id_paciente` int(11) NOT NULL AUTO_INCREMENT,
  `id_usuario` int(11) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `sexo` enum('M','F','Otro') NOT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_paciente`),
  KEY `fk_paciente_usuario` (`id_usuario`),
  CONSTRAINT `fk_paciente_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `resultado_laboratorio`
--

DROP TABLE IF EXISTS `resultado_laboratorio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `resultado_laboratorio` (
  `id_resultado` int(11) NOT NULL AUTO_INCREMENT,
  `id_laboratorio` int(11) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `valor_resultado` varchar(100) DEFAULT NULL,
  `unidad_medida` varchar(30) DEFAULT '-',
  `fecha_registro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_resultado`),
  KEY `idx_resultados_lab` (`id_laboratorio`),
  CONSTRAINT `resultado_laboratorio_ibfk_1` FOREIGN KEY (`id_laboratorio`) REFERENCES `laboratorios` (`id_laboratorio`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=91 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_rol` int(11) NOT NULL AUTO_INCREMENT,
  `nombre_rol` varchar(50) NOT NULL,
  `descripcion` text DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `nombre_rol` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `signos_vitales`
--

DROP TABLE IF EXISTS `signos_vitales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `signos_vitales` (
  `id_signo` int(11) NOT NULL AUTO_INCREMENT,
  `id_cita` int(11) NOT NULL,
  `id_paciente` int(11) NOT NULL,
  `id_enfermero` int(11) DEFAULT NULL,
  `temperatura` decimal(4,1) DEFAULT NULL,
  `presion` varchar(20) DEFAULT NULL,
  `frecuencia_cardiaca` int(11) DEFAULT NULL,
  `frecuencia_respiratoria` int(11) DEFAULT NULL,
  `saturacion` int(11) DEFAULT NULL,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  `observaciones` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_signo`),
  UNIQUE KEY `uq_cita` (`id_cita`),
  KEY `idx_sv_paciente` (`id_paciente`),
  KEY `idx_signos_paciente` (`id_paciente`),
  KEY `fk_signos_enfermero` (`id_enfermero`),
  CONSTRAINT `fk_signos_enfermero` FOREIGN KEY (`id_enfermero`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `signos_vitales_ibfk_1` FOREIGN KEY (`id_cita`) REFERENCES `citas` (`id_cita`) ON DELETE CASCADE,
  CONSTRAINT `signos_vitales_ibfk_2` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `id_paciente` int(11) DEFAULT NULL,
  `nombre_usuario` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo` varchar(100) DEFAULT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `area_asignada` varchar(100) DEFAULT NULL,
  `turno` varchar(50) DEFAULT NULL,
  `id_rol` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `nombre_usuario` (`nombre_usuario`),
  UNIQUE KEY `uk_usuarios_nombre` (`nombre_usuario`),
  KEY `id_rol` (`id_rol`),
  KEY `fk_usuario_paciente` (`id_paciente`),
  CONSTRAINT `fk_usuario_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_paciente`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'hospital_final'
--

--
-- Dumping routines for database 'hospital_final'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generar_atenciones` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generar_atenciones`()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 150 DO
        INSERT INTO atenciones (id_paciente, id_cita, id_medico, diagnostico, tratamiento, receta, notas, fecha_atencion)
        VALUES (
            (SELECT id_paciente FROM pacientes ORDER BY RAND() LIMIT 1),
            (SELECT id_cita FROM citas ORDER BY RAND() LIMIT 1),
            (SELECT id_usuario FROM usuarios WHERE id_rol = 2 ORDER BY RAND() LIMIT 1),
            
            CONCAT('Diagnóstico automático ', i),
            CONCAT('Tratamiento sugerido ', i),
            CONCAT('Receta generada ', i),
            CONCAT('Notas médicas adicionales ', i),

            DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND()*365) DAY)
        );

        SET i = i + 1;
    END WHILE;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generar_citas` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generar_citas`()
BEGIN
    DECLARE i INT DEFAULT 1;

    
    
    

    WHILE i <= 300 DO
        INSERT INTO citas (id_paciente, id_usuario, fecha_cita, hora_cita, motivo, estado)
        VALUES (
            (SELECT id_paciente FROM pacientes ORDER BY RAND() LIMIT 1),
            (SELECT id_usuario FROM usuarios WHERE id_rol = 2 ORDER BY RAND() LIMIT 1),
            DATE_ADD('2024-01-01', INTERVAL FLOOR(RAND()*365) DAY),
            SEC_TO_TIME(FLOOR(RAND()*28800)+28800),
            'Consulta general',
            'Pendiente'
        );
        SET i = i + 1;
    END WHILE;


    
    
    

    SET i = 1;
    WHILE i <= 180 DO
        INSERT INTO citas (id_paciente, id_usuario, fecha_cita, hora_cita, motivo, estado)
        VALUES (
            (SELECT id_paciente FROM pacientes ORDER BY RAND() LIMIT 1),
            (SELECT id_usuario FROM usuarios WHERE id_rol = 2 ORDER BY RAND() LIMIT 1),
            DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND()*365) DAY),
            SEC_TO_TIME(FLOOR(RAND()*28800)+28800),
            'Consulta realizada',
            'Realizada'
        );
        SET i = i + 1;
    END WHILE;


    
    
    

    SET i = 1;
    WHILE i <= 20 DO
        INSERT INTO citas (id_paciente, id_usuario, fecha_cita, hora_cita, motivo, estado)
        VALUES (
            (SELECT id_paciente FROM pacientes ORDER BY RAND() LIMIT 1),
            (SELECT id_usuario FROM usuarios WHERE id_rol = 2 ORDER BY RAND() LIMIT 1),
            DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND()*365) DAY),
            SEC_TO_TIME(FLOOR(RAND()*28800)+28800),
            'Cita cancelada por el paciente',
            'Cancelada'
        );
        SET i = i + 1;
    END WHILE;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generar_laboratorios` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generar_laboratorios`()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE estados VARCHAR(30);

    WHILE i <= 100 DO

        SET estados = ELT(FLOOR(1 + RAND()*3), 'Pendiente', 'Completado', 'Entregado');

        INSERT INTO laboratorios (id_paciente, id_usuario, tipo_examen, fecha_solicitud, estado)
        VALUES (
            (SELECT id_paciente FROM pacientes ORDER BY RAND() LIMIT 1),
            (SELECT id_usuario FROM usuarios WHERE id_rol = 2 ORDER BY RAND() LIMIT 1),
            CONCAT('Examen tipo #', i),
            DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND()*365) DAY),
            estados
        );

        SET i = i + 1;
    END WHILE;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generar_resultados` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generar_resultados`()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 80 DO
        INSERT INTO resultado_laboratorio (id_laboratorio, descripcion, valor_resultado, unidad_medida, fecha_registro)
        VALUES (
            (SELECT id_laboratorio FROM laboratorios ORDER BY RAND() LIMIT 1),
            CONCAT('Resultado de examen ', i),
            CONCAT(FLOOR(RAND()*200)),
            'mg/dL',
            NOW()
        );

        SET i = i + 1;
    END WHILE;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generar_signos` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_uca1400_ai_ci */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generar_signos`()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE citaValida INT;

    WHILE i <= 100 DO
        
        
        SELECT id_cita INTO citaValida
        FROM citas
        WHERE id_cita NOT IN (SELECT id_cita FROM signos_vitales)
        ORDER BY RAND()
        LIMIT 1;

        
        IF citaValida IS NULL THEN
            SET i = 101;   
        ELSE

            INSERT INTO signos_vitales
            (id_paciente, id_cita, temperatura, presion, frecuencia_cardiaca, frecuencia_respiratoria, saturacion, fecha_registro)
            VALUES (
                (SELECT id_paciente FROM citas WHERE id_cita = citaValida),
                citaValida,
                ROUND(36 + RAND()*3, 2),
                CONCAT(100 + FLOOR(RAND()*40), '/', 60 + FLOOR(RAND()*20)),
                60 + FLOOR(RAND()*40),
                12 + FLOOR(RAND()*10),
                92 + FLOOR(RAND()*8),
                NOW()
            );

            SET i = i + 1;
        END IF;

    END WHILE;

END ;;
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
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- ============================================================
-- DATOS INICIALES
-- ============================================================

INSERT INTO `roles` (`id_rol`, `nombre_rol`, `descripcion`) VALUES
(1, 'Administrador', 'Acceso total al sistema'),
(2, 'Médico', 'Gestión de citas y atenciones'),
(3, 'Enfermero', 'Registro de signos vitales'),
(4, 'Laboratorista', 'Gestión de laboratorios y resultados'),
(5, 'Paciente', 'Consulta de citas y resultados');

INSERT INTO `usuarios` (`nombre_usuario`, `password`, `nombre`, `apellido`, `telefono`, `correo`, `id_rol`) VALUES
('admin', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Admin', 'Principal', '555-0100', 'admin@hospital.com', 1),
('doc1', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Carlos', 'Médico', '555-0101', 'carlos@hospital.com', 2),
('doc2', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'María', 'García', '555-0102', 'maria@hospital.com', 2),
('doc3', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Pedro', 'López', '555-0103', 'pedro@hospital.com', 2),
('doc4', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Ana', 'Martínez', '555-0104', 'ana@hospital.com', 2),
('doc5', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Luis', 'Ramírez', '555-0105', 'luis@hospital.com', 2),
('enf1', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Laura', 'Enfermera', '555-0106', 'laura@hospital.com', 3),
('lab', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Roberto', 'Laboratorista', '555-0107', 'roberto@hospital.com', 4),
('paciente', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Juan', 'Paciente', '555-0108', 'juan@email.com', 5),
('paciente2', '$2a$10$6iWhM6rn1Wvjsk8MS.7ryu/XxyDQTPmGmYsIV.X6wW9wJQ1CRA4KW', 'Sofía', 'Paciente', '555-0109', 'sofia@email.com', 5);

