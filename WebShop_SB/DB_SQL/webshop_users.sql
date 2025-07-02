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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `mailaddress` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `alert` int DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT NULL,
  `point` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `mailaddress` (`mailaddress`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin@persol.co.jp','新宿',40,'$2a$10$KJMCZn0roUKcloYht1wode/4wr2L6EWyr4l.9BKZMgqH8S5fsBJlW','ADMIN',0,0,204876),(2,'u','u@u','u',1,'$2a$10$75fmMSVwgdMsYLLEK1Oam.M.BOxT43JMpeHy7JuSncm6jKLaj2aiG','USER',1,0,109566),(3,'a','a@a','a',2,'$2a$10$sjwTZVAbmKJhmElMMz1J3OS/Lrkog1AbhpQo2LCjZSJyMTunf1DoW','USER',0,0,1581),(4,'mailtest','springtest8900@gmail.com','東京',22,'$2a$10$gCX2eNx.S9sePS4udesH5Ob/vYBtzWArd.k9ZmDf21zJ6lq7z22Cy','USER',0,0,39910),(5,'yy','y@y','y',1,'$2a$10$FzbU3vM70xj2NdYuGvrke.79d80BobPFlp1AffUlP.9Sv.NUc.X8m','USER',3,0,0),(8,'wakamatsu','dsaf@sadfgs','awefd',123,'$2a$10$uGvmGDfsmKR7HSiVExjnje8a6Ei.HfqEl6OiGN0uMYS8Sykccyu4.','USER',0,0,3488),(9,'s','s@s','s',13,'$2a$10$VWg2/../bQxrO47TJH7EXuAWNh8UmsHKSwEAbj/dY/KYiFapQSNDG','USER',0,0,275),(10,'v','v@v','v',34,'$2a$10$W9XJltkPNvgG3Q974JJQhOR0tMiQaExV5qezHhS460I3xSxQVA9BC','USER',0,0,0),(11,'i','i@i','i',56,'$2a$10$frwK0sUg0RJm7s41vImEqOnPSSIcg6iJyM9j7C3YZSGtYU9uSdQby','USER',0,0,546),(12,'wakamatsu11','refsgdb@ewqrstgd','werf',1,'$2a$10$RbnlIH9W/DabU4clBpHpDeaXgJjng/FJEG/6TwXkOhwk2QC3ZR3ha','USER',0,0,1422),(13,'wakamatsu5','sadfgh@iuhygfd','11',11,'$2a$10$9lUtsY8Ek87kJbL12z0jleLbOw73ujt1klxq3DRZ5SJIeegzMj4Zq','USER',0,0,2112),(14,'wakamatsu22','wqerafsdgh@rwefgsd','1',1,'$2a$10$Kb1w.2QdnL0rSUfs2A64VOd1Npaqc5D5U7oVkjVCEAfmV9i3yBwpK','USER',0,0,0),(15,'wakamatsu12','dsf@dasf','1',1,'$2a$10$KUpIhBH7sg14Y.uk5HI3dufpsdbAHsFDKrY0PazqR4WfiApzRFz7a','USER',0,0,2096),(16,'おすし','osushi@osushi','osushi',22,'$2a$10$vD.vF0SI8FLVokCCV85YSesuSxa/rnKRGvAPibCK49WR2YoR7tztK','USER',0,0,300),(18,'hamati','hamati@hamti','hamati',22,'$2a$10$3eKH8bQoFaa8c2yMauNDmeYR8VgP2n7hdlNaZy3e4Z4ukvSo.HnJC','USER',0,0,0),(19,'toro','toro@toro','toro',22,'$2a$10$uKAQ..TfuGVHkWShiIciBOW97iShUytLf4Tk993QgZ1j7X/T5Ta9u','USER',0,0,0),(20,'naoki','aaa@aaa.aaa','sada',1234,'$2a$10$NVoN9eV85IgbkKS0hW7E/O3Aajl1nxQp0cVYbdLCFD9C6g06Olg/m','USER',0,0,0),(21,'admin2','aaa@aaaaa','新宿',1000,'$2a$10$nOqyvROkTnz7ERiMJjdOPuR00Farg28ZhT.XFAW9QFtxiupgY.RSO','ADMIN',0,0,0),(22,'aa','a@aaa','aaa',1,'$2a$10$7ARC1GnXrKABC.7MAfZhBe1fk6q60P0q4qn33NLXebxO6j41RgpqO','USER',1,0,0),(23,'kuri','aaa@aaa','aaaa',22,'$2a$10$82EmIgK3kbjIBCVJaWvJQeMuFW6WAa2IX.8rBnpCiEy/lf7L97wc6','USER',0,0,0),(24,'niko','niko@niko','niko',25,'$2a$10$mR0ueYGBjZctqKaAmZNTDeEAm4MoH8ud2PGIEa6z7zAArQk/0qRD6','USER',0,0,0),(25,'wwwww','wwww@www','wwwww',222,'$2a$10$auGqsGp680Jq9rOJaJUucua967NSMzARen42S3nAcXTmv738jCz.q','USER',0,0,0),(26,'asa','asa@asa','asa',2222,'$2a$10$d/PADCSniHDCuaB4DZcu1Op5UUgS9s6h6TBM1vfh5G6Hbrkh35aS6','USER',2,0,0),(28,'aaaaaa','aa@aaaaa','東京都',22222,'$2a$10$hlFotSOwR04RdDf/NX5TS.9IT1JDiMnfJipnMp7xNV623xZOhIyOW','USER',0,0,199505),(30,'test','test@test','test',12,'$2a$10$bmzfPgIV6IjsycxO23BQcepGyrlJq.F.qbr949QFoQkvCw7QGJXxe','USER',0,0,0),(32,'aaa','aa@aa','aaa',12,'$2a$10$Xqytol0chpNhEsdHwD/wxOhJTAwRMy./o3EnFRWoo9gJGxeYMPa.2','USER',1,0,0),(33,'me','meado@1','a',49,'$2a$10$kO2/4W/o0V/vlucOg56Lo.Y.UzXDdyGprKDhUsjBZP/f.8h9rSa9y','USER',0,0,462),(34,'miyu','miyu@miyu','東京都',10,'$2a$10$n.aybWxWEstuA5BdRe0phepv0RjdiqoRIGqAxHF1XW9cveFqxI4z6','USER',0,0,0),(35,'aii','a@user','123',22,'$2a$10$cEZsKEZCACyIaKC3UWuD4.ykTsssWH6Pd3HiqNyU1RmYgzTj9vHxS','USER',0,0,0),(38,'1','1@1','1',1,'$2a$10$h1rw9uudxf19JlpOwazopuAtjjXEctfXD6qCumIGrkLBgSIGPlF9e','USER',2,0,67500),(40,'yuto.ito','eee@eee','eee',20,'$2a$10$lLT9v5YPzB1rW2psoYGmZOlgkyzHMaNFWZugNt06XiOq8lvhSF34C','USER',0,0,0),(42,'ryo','test@test.com','神奈川',22,'$2a$10$89h3xMiURL4fxsUThnUXbe73VGUV5BP4JxcBQjL5riqDS5SiZ9C.u','USER',0,0,0),(43,'pxtuser','pxt@aaa','大阪府',20,'$2a$10$89LY2HplWWMX50H5r0ch3On9/5cnykepaFekK/2GhIGInXFRvFmCS','USER',0,0,0),(44,'asdfghjkl','rk@com','東京',22,'$2a$10$kCQkeByQho4igJj.SaCqb.23kmTxG/9jMH8Xm6els9v8HTI9IvGra','USER',0,0,0),(45,'hiro','hiro@hiro','東京都',13,'$2a$10$Ktgnlt1/TNWpEl8abSn47O5D8y0nr1ZrZs/lVFt5TGRoOMpRmuMY6','USER',0,0,0),(48,'abc','abc@a','abc',123,'$2a$10$ZbvtseTjaaqj.7XQCSJ62euJlAUwgbyDnUenLxj50j9IS71SpeRoa','USER',0,0,0),(50,'cat','cat@cat','cat',1,'$2a$10$JuXjS6M5f8TmQUu5OqmQYO9TVWjN8MvBEKOkRtKd2FfRtZYKQHm26','USER',0,0,110),(51,'wwww','aaa@qqq','tokyo',22,'$2a$10$F8/aycKmwgw1RXI/VYoeIurBbmaTxR/PNijgNYdhaCaYHUZ8s9uCy','USER',0,0,720);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
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
