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
-- Table structure for table `searchlog`
--

DROP TABLE IF EXISTS `searchlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `searchlog` (
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `id` int DEFAULT NULL,
  `title` varchar(5000) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searchlog`
--

LOCK TABLES `searchlog` WRITE;
/*!40000 ALTER TABLE `searchlog` DISABLE KEYS */;
INSERT INTO `searchlog` VALUES ('2025-06-20 08:58:47','wakamatsu','products',0,'夏目漱石',NULL,NULL),('2025-06-20 11:24:29','u','products',0,'夏目漱石',NULL,NULL),('2025-06-20 09:34:20','s','products',0,'羅生',NULL,NULL),('2025-06-20 09:34:29','s','products',0,'フィ',NULL,NULL),('2025-06-20 09:34:45','s','products',0,'one',NULL,NULL),('2025-06-20 10:03:47','admin','products',0,'夏目漱石',NULL,NULL),('2025-06-20 11:14:05','s','products',0,'夏目',NULL,NULL),('2025-06-20 11:16:16','おすし','products',0,'夏目',NULL,NULL),('2025-06-20 11:31:18','おすし','products',0,'時をかける',NULL,NULL),('2025-06-20 11:32:59','s','products',0,'時をかける',NULL,NULL),('2025-06-20 12:46:44','i','products',0,'時をかける',NULL,NULL),('2025-06-20 13:30:54','a','products',0,'漫画',NULL,NULL),('2025-06-20 13:21:27','a','products',0,'芥川',NULL,NULL),('2025-06-20 13:21:41','a','products',0,'ONe',NULL,NULL),('2025-06-20 14:56:24','aaaaaa','products',0,'夏目漱石',NULL,NULL),('2025-06-20 15:06:04','a','products',0,'夏目漱石',NULL,NULL),('2025-06-20 15:17:48','yuto.ito','products',0,'夏目漱石',NULL,NULL),('2025-06-20 15:19:46','pxtuser','products',0,'ワンピース',NULL,NULL),('2025-06-20 15:19:54','pxtuser','products',0,'尾田栄一郎',NULL,NULL),('2025-06-20 15:20:05','aii','products',0,'夏目漱石',NULL,NULL),('2025-06-20 15:20:13','test','products',0,'夏目漱石',NULL,NULL),('2025-06-20 15:20:15','pxtuser','products',0,'ONE PIECE',NULL,NULL),('2025-06-20 15:20:22','pxtuser','products',0,'夏目',NULL,NULL),('2025-06-20 15:22:16','pxtuser','products',0,'お寿司',NULL,NULL),('2025-06-20 15:26:19','admin','stock',3,NULL,NULL,NULL),('2025-06-20 15:51:19','a','products',0,'推し',NULL,NULL),('2025-06-20 16:10:14','wwww','products',0,'あ',NULL,NULL),('2025-06-20 16:55:44','mailtest','products',0,'漫画',NULL,NULL),('2025-06-20 16:55:55','mailtest','products',0,'はらぺこ',NULL,NULL),('2025-06-20 17:01:12','mailtest','products',0,'夏目漱石',NULL,NULL),('2025-06-20 17:01:31','mailtest','products',0,'絵本',NULL,NULL);
/*!40000 ALTER TABLE `searchlog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-25  9:09:34
