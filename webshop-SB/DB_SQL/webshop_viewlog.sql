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
-- Table structure for table `viewlog`
--

DROP TABLE IF EXISTS `viewlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `viewlog` (
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(255) DEFAULT NULL,
  `productid` int DEFAULT NULL,
  `title` varchar(5000) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `release_day` varchar(255) DEFAULT NULL,
  `price` int DEFAULT NULL,
  `genre` varchar(255) DEFAULT NULL,
  `imgLink` varchar(5000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `viewlog`
--

LOCK TABLES `viewlog` WRITE;
/*!40000 ALTER TABLE `viewlog` DISABLE KEYS */;
INSERT INTO `viewlog` VALUES ('2025-06-20 16:16:29','a',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 12:58:39','yy',3,'坊ちゃん - 改訂版','夏目漱石','1907-1-1',1500,'フィクション','https://www.shinchosha.co.jp/images_v2/book/cover/101003/101003_xl.jpg'),('2025-06-20 16:16:29','yy',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 16:16:29','toro',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 16:16:41','wakamatsu',2,'ONE PIECE','尾田栄一郎','1997-12-24',700,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg'),('2025-06-20 16:16:29','wakamatsu',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 15:51:08','wakamatsu',5,'山椒魚 - 特別版','井伏鱒二','1929-1-1',605,'短編小説','https://m.media-amazon.com/images/I/713vPy-M0wL._SY425_.jpg'),('2025-06-20 16:16:29','aa',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 16:16:41','a',2,'ONE PIECE','尾田栄一郎','1997-12-24',700,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg'),('2025-06-20 16:16:41','aaaaaa',2,'ONE PIECE','尾田栄一郎','1997-12-24',700,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg'),('2025-06-20 16:16:41','aaa',2,'ONE PIECE','尾田栄一郎','1997-12-24',700,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg'),('2025-06-20 16:13:09','aaa',12,'ひみつのおすしやさん','黒岩まゆ','2024-11-20',1650,'絵本','https://m.media-amazon.com/images/I/81xVWtOJbEL._SY385_.jpg'),('2025-06-20 14:59:36','aaaaaa',15,'くりかえし計算ドリル','不明','不明',540,'哲学','https://www.sing.co.jp/bwp/wp-content/uploads/2024dkuri-kei-1.jpg'),('2025-06-20 16:13:09','a',12,'ひみつのおすしやさん','黒岩まゆ','2024-11-20',1650,'絵本','https://m.media-amazon.com/images/I/81xVWtOJbEL._SY385_.jpg'),('2025-06-20 15:19:31','a',9,'すし図鑑','著者','2013-4-23',1540,'哲学','https://m.media-amazon.com/images/I/51W349Ag6LL._UF1000,1000_QL80_.jpg'),('2025-06-20 16:16:29','1',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 15:51:00','test',8,'マスカレードホテル','東野圭吾','2014-7-18',1100,'文芸作品','https://m.media-amazon.com/images/I/41Hk2Z2gEHL._SY445_SX342_.jpg'),('2025-06-20 15:25:17','a',6,'読む寿司 オイシイ話108ネタ','河原一久','2019-4-19',1540,'評論','https://m.media-amazon.com/images/I/71RsOkdtPqL._SY425_.jpg'),('2025-06-20 16:16:41','pxtuser',2,'ONE PIECE','尾田栄一郎','1997-12-24',700,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg'),('2025-06-20 15:19:55','1',15,'くりかえし計算ドリル','不明','不明',540,'哲学','https://www.sing.co.jp/bwp/wp-content/uploads/2024dkuri-kei-1.jpg'),('2025-06-20 16:16:29','aii',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 15:20:30','miyu',13,'推しの子','赤坂アカ×横槍メンゴ','2020-7-17',659,'漫画','https://m.media-amazon.com/images/I/71ABgztQ2OL._SY385_.jpg'),('2025-06-20 16:16:29','asa',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 16:16:29','miyu',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 15:51:00','1',8,'マスカレードホテル','東野圭吾','2014-7-18',1100,'文芸作品','https://m.media-amazon.com/images/I/41Hk2Z2gEHL._SY445_SX342_.jpg'),('2025-06-20 15:23:26','a',15,'くりかえし計算ドリル','不明','不明',540,'哲学','https://www.sing.co.jp/bwp/wp-content/uploads/2024dkuri-kei-1.jpg'),('2025-06-20 16:16:29','u',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 16:13:09','miyu',12,'ひみつのおすしやさん','黒岩まゆ','2024-11-20',1650,'絵本','https://m.media-amazon.com/images/I/81xVWtOJbEL._SY385_.jpg'),('2025-06-20 15:25:17','miyu',6,'読む寿司 オイシイ話108ネタ','河原一久','2019-4-19',1540,'評論','https://m.media-amazon.com/images/I/71RsOkdtPqL._SY425_.jpg'),('2025-06-20 15:51:00','cat',8,'マスカレードホテル','東野圭吾','2014-7-18',1100,'文芸作品','https://m.media-amazon.com/images/I/41Hk2Z2gEHL._SY445_SX342_.jpg'),('2025-06-20 16:16:41','admin',2,'ONE PIECE','尾田栄一郎','1997-12-24',700,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg'),('2025-06-20 15:51:00','wakamatsu',8,'マスカレードホテル','東野圭吾','2014-7-18',1100,'文芸作品','https://m.media-amazon.com/images/I/41Hk2Z2gEHL._SY445_SX342_.jpg'),('2025-06-20 15:57:48','admin',16,'角川つばさ文庫版 サマーウォーズ ','細田 守 (原名)','2009-8-9',814,'短編小説','https://m.media-amazon.com/images/I/71+Wd+XcZTL._SY466_.jpg'),('2025-06-20 16:16:29','mailtest',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 16:16:41','mailtest',2,'ONE PIECE','尾田栄一郎','1997-12-24',700,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg'),('2025-06-20 16:06:52','mailtest',4,'平家物語 - 新装版','不明','1917-5-1',1600,'物語詩','https://m.media-amazon.com/images/I/91ZV5-0kA6L._UF1000,1000_QL80_.jpg'),('2025-06-20 16:10:21','wwww',11,'<通常版>はらぺこあおむし','エリック・カール','1976-5-1',1200,'フィクション','https://www.kaiseisha.co.jp/special/ericcarle/harapekoaomushi/img/img_sec3_1.png'),('2025-06-20 16:13:09','wwww',12,'ひみつのおすしやさん','黒岩まゆ','2024-11-20',1650,'絵本','https://m.media-amazon.com/images/I/81xVWtOJbEL._SY385_.jpg'),('2025-06-20 16:16:29','admin',1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg'),('2025-06-20 16:17:00','a',3,'坊ちゃん - 改訂版','夏目漱石','1907-1-1',1500,'フィクション','https://www.shinchosha.co.jp/images_v2/book/cover/101003/101003_xl.jpg'),('2025-06-20 16:17:06','a',5,'山椒魚 - 特別版','井伏鱒二','1929-1-1',605,'短編小説','https://m.media-amazon.com/images/I/713vPy-M0wL._SY425_.jpg'),('2025-06-20 16:17:12','a',8,'マスカレードホテル','東野圭吾','2014-7-18',1100,'文芸作品','https://m.media-amazon.com/images/I/41Hk2Z2gEHL._SY445_SX342_.jpg'),('2025-06-20 16:17:18','a',11,'<通常版>はらぺこあおむし','エリック・カール','1976-5-1',1200,'フィクション','https://www.kaiseisha.co.jp/special/ericcarle/harapekoaomushi/img/img_sec3_1.png'),('2025-06-20 16:29:01','mailtest',14,'時をかける少女 アニメ版 ','筒井 康隆 (原著)','2007-4-1',1320,'短編小説','https://m.media-amazon.com/images/I/81T8VzJ7sGL._SY425_.jpg');
/*!40000 ALTER TABLE `viewlog` ENABLE KEYS */;
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
