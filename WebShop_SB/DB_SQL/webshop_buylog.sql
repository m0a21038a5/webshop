-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: webshop
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `buylog`
--

DROP TABLE IF EXISTS `buylog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `buylog` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(255) DEFAULT NULL,
  `productid` int DEFAULT NULL,
  `price` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buylog`
--

LOCK TABLES `buylog` WRITE;
/*!40000 ALTER TABLE `buylog` DISABLE KEYS */;
INSERT INTO `buylog` VALUES (12,'2025-06-20 09:44:01','s',1,1500,1),(13,'2025-06-20 09:48:36','i',3,1500,1),(14,'2025-06-20 09:50:40','wakamatsu',1,1500,2),(15,'2025-06-20 09:50:40','wakamatsu',2,700,4),(16,'2025-06-20 09:50:40','wakamatsu',4,1600,6),(17,'2025-06-20 09:50:40','wakamatsu',8,1100,2),(18,'2025-06-20 09:50:40','wakamatsu',12,1650,3),(19,'2025-06-20 09:52:43','wakamatsu11',1,1500,1),(20,'2025-06-20 09:52:43','wakamatsu11',3,1500,1),(21,'2025-06-20 09:52:43','wakamatsu11',2,700,2),(22,'2025-06-20 09:52:43','wakamatsu11',8,1100,1),(23,'2025-06-20 09:52:43','wakamatsu11',11,1200,1),(24,'2025-06-20 09:52:44','wakamatsu11',1,1500,1),(25,'2025-06-20 09:52:44','wakamatsu11',3,1500,1),(26,'2025-06-20 09:52:44','wakamatsu11',2,700,2),(27,'2025-06-20 09:52:44','wakamatsu11',8,1100,1),(28,'2025-06-20 09:52:44','wakamatsu11',11,1200,1),(29,'2025-06-20 09:53:36','wakamatsu11',5,605,3),(30,'2025-06-20 09:53:36','wakamatsu11',6,1540,1),(31,'2025-06-20 09:53:36','wakamatsu11',7,1334,2),(32,'2025-06-20 09:53:53','wakamatsu11',1,1500,1),(33,'2025-06-20 09:55:27','wakamatsu5',13,659,3),(34,'2025-06-20 09:55:27','wakamatsu5',9,1540,10),(35,'2025-06-20 09:55:27','wakamatsu5',12,1650,1),(36,'2025-06-20 09:55:27','wakamatsu5',2,700,3),(37,'2025-06-20 09:58:37','wakamatsu12',1,1500,1),(38,'2025-06-20 09:58:37','wakamatsu12',10,1540,3),(39,'2025-06-20 09:58:37','wakamatsu12',5,605,1),(40,'2025-06-20 09:58:37','wakamatsu12',14,1320,1),(41,'2025-06-20 09:58:37','wakamatsu12',13,659,1),(42,'2025-06-20 09:58:37','wakamatsu12',3,1500,1),(43,'2025-06-20 10:04:46','i',14,1320,2),(44,'2025-06-20 10:06:04','admin',2,700,3),(45,'2025-06-20 10:06:04','admin',10,1540,1),(46,'2025-06-20 10:06:04','admin',8,1100,1),(47,'2025-06-20 10:06:04','admin',11,1200,1),(48,'2025-06-20 10:23:44','wakamatsu',15,540,1),(49,'2025-06-20 10:23:44','wakamatsu',16,814,1),(50,'2025-06-20 10:24:08','wakamatsu',15,540,1),(51,'2025-06-20 10:24:08','wakamatsu',8,1100,1),(52,'2025-06-20 10:25:30','wakamatsu12',15,540,3),(53,'2025-06-20 10:25:30','wakamatsu12',2,700,1),(54,'2025-06-20 10:25:30','wakamatsu12',16,814,3),(55,'2025-06-20 10:25:30','wakamatsu12',3,1500,4),(56,'2025-06-20 10:41:11','a',1,1500,135),(57,'2025-06-20 10:44:24','wakamatsu',8,1100,1),(58,'2025-06-20 10:44:24','wakamatsu',2,700,1),(59,'2025-06-20 11:16:36','おすし',3,1500,2),(60,'2025-06-20 11:23:50','admin',2,700,1),(61,'2025-06-20 11:27:36','wakamatsu',2,700,1),(62,'2025-06-20 11:33:19','s',14,1320,1),(63,'2025-06-20 11:42:34','u',2,700,14),(64,'2025-06-20 12:26:07','a',2,700,1),(65,'2025-06-20 12:45:32','wakamatsu',2,700,1),(66,'2025-06-20 12:45:32','wakamatsu',4,1600,1),(67,'2025-06-20 12:47:32','i',14,1320,1),(68,'2025-06-20 12:48:38','a',2,700,1),(69,'2025-06-20 12:48:38','a',3,1500,1),(70,'2025-06-20 12:52:51','u',5,605,1),(71,'2025-06-20 12:53:19','u',8,1100,990),(72,'2025-06-20 13:08:28','wakamatsu',1,1500,1),(73,'2025-06-20 13:08:28','wakamatsu',2,700,1),(74,'2025-06-20 13:09:09','wakamatsu',1,1500,1),(75,'2025-06-20 14:56:16','aaaaaa',2,700,500),(76,'2025-06-20 14:57:23','aaaaaa',12,1650,997),(77,'2025-06-20 15:01:05','me',6,1540,3),(78,'2025-06-20 15:15:20','a',6,1540,422),(79,'2025-06-20 15:16:16','a',9,1540,500),(80,'2025-06-20 15:19:01','a',6,1540,75),(81,'2025-06-20 15:19:41','1',1,1500,450),(82,'2025-06-20 15:20:33','a',1,1500,50),(83,'2025-06-20 15:21:10','u',3,1500,1),(84,'2025-06-20 15:21:46','a',3,1500,499),(85,'2025-06-20 15:21:46','u',5,605,1),(86,'2025-06-20 15:21:48','u',5,605,1),(87,'2025-06-20 15:22:44','u',5,605,2),(88,'2025-06-20 15:22:52','a',4,1600,500),(89,'2025-06-20 15:36:34','cat',8,1100,1),(90,'2025-06-20 15:44:25','admin',1,1500,2),(91,'2025-06-20 15:46:52','admin',16,814,500),(92,'2025-06-20 15:53:09','admin',1,1500,1),(93,'2025-06-20 15:53:16','a',13,659,500),(94,'2025-06-20 15:54:17','admin',1,1500,1356),(95,'2025-06-20 15:59:30','mailtest',1,1500,1),(96,'2025-06-20 16:10:49','wwww',11,1200,6),(97,'2025-06-20 16:32:56','mailtest',14,1320,300);
/*!40000 ALTER TABLE `buylog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-25  9:09:33
