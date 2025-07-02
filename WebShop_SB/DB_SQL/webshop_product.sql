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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(5000) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `release_day` varchar(255) DEFAULT NULL,
  `stock` int DEFAULT '0',
  `quantity` int DEFAULT '0',
  `price` int DEFAULT '0',
  `sales` int DEFAULT '0',
  `genre` varchar(255) DEFAULT NULL,
  `imgLink` varchar(5000) DEFAULT NULL,
  `recommand` tinyint(1) DEFAULT '0',
  `notice` varchar(5000) DEFAULT NULL,
  `view` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'羅生門 - 新版','芥川龍之介','1917-5-1',1500,0,1500,2009,'フィクション','https://m.media-amazon.com/images/I/71G17az7Y-L._SY425_.jpg',1,'『羅生門』は、芥川龍之介による日本文学の名作です。この新版では、物語の深いテーマと独特の視点がさらに引き立てられています。人間の本質や道徳の曖昧さを描いたストーリーは、今なお多くの読者に感動を与えています。美しい装丁と共に、ぜひ手に取ってみてください。',1),(2,'ONE PIECE','尾田栄一郎','1997-12-24',1500,0,700,535,'漫画','https://m.media-amazon.com/images/I/71kvo+fijnL._UF1000,1000_QL80_.jpg',1,'『ONE PIECE』は、尾田栄一郎による人気漫画で、冒険と友情の物語が展開されます。海賊王を目指すルフィと仲間たちの壮大な旅を描いた作品で、アニメ化もされており、世界中のファンに愛されています。感動的なストーリーと魅力的なキャラクターが魅力です。',1),(3,'坊ちゃん - 改訂版','夏目漱石','1907-1-1',1500,0,1500,512,'フィクション','https://www.shinchosha.co.jp/images_v2/book/cover/101003/101003_xl.jpg',1,'『坊ちゃん』は、夏目漱石による名作で、明治時代の教育現場を舞台にした物語です。主人公の坊ちゃんの奮闘や成長を描いたこの作品は、当時の社会情勢や人間関係を鋭く描写しています。改訂版では、読みやすさが向上しており、初めて読む方にもおすすめです。',1),(4,'平家物語 - 新装版','不明','1917-5-1',1500,0,1600,507,'物語詩','https://m.media-amazon.com/images/I/91ZV5-0kA6L._UF1000,1000_QL80_.jpg',1,'『平家物語』は、日本の歴史を背景にした物語で、平家一族の栄光と滅亡を描いています。この新装版では、物語の重要なエピソードを厳選し、読みやすい形で再構成されています。歴史に興味がある方や、日本文学を学ぶ方にとって、必読の一冊です。',1),(5,'山椒魚 - 特別版','井伏鱒二','1929-1-1',496,0,605,8,'短編小説','https://m.media-amazon.com/images/I/713vPy-M0wL._SY425_.jpg',1,'『山椒魚』は、井伏鱒二による短編小説で、独特の視点から描かれた物語です。特別版では、著者の解説や背景情報が追加され、より深く作品を理解できる内容になっています。文学を愛する方にとって、楽しめる一冊です。',1),(6,'読む寿司 オイシイ話108ネタ','河原一久','2019-4-19',1500,0,1540,501,'評論','https://m.media-amazon.com/images/I/71RsOkdtPqL._SY425_.jpg',1,'日本が世界に誇る健康食、お寿司。 そのお寿司がもっと美味しく、もっと楽しくなるコラムが108本も詰まった本書は、 150冊に及ぶ参考文献を読み込み、全国津々浦々を飛び回って職人に話を聞きまくった 著者の「お寿司研究」の集大成です。  「昭和天皇はコハダがお好み?」 「江戸無血開城、勝海舟と西郷隆盛をお寿司を食べていた」 「寿司屋の湯呑みが大きいのは職人が手抜きするため?」 「ワサビを醤油に溶かすのは、ほんとうにマナー違反?」 「寿司屋の修行に10年は本当に必要なのか?」 「変わり種寿司だって面白い。うなぎバターやキャビア軍艦?」 「江戸時代、マグロは人気がない『下魚』だった」 「関東大震災で起きた物資不足が寿司ネタの幅を広げた」 「江戸末期のグルメブームが握り寿司誕生の背景にあった」 「江戸時代の握り寿司は、おにぎり並みの大きさだった?」  などなど、歴史、作り方、流儀、高級店から回転寿司まで厳選トリビア、よいネタ揃ってます!',1),(7,'君たちはどう生きるか','吉野源三郎','2017-8-24',500,0,1334,2,'哲学','https://lh6.googleusercontent.com/proxy/hTKxefHpOjJnlODOtQmqQKt6nVd9zpzxRrLnMHrLelPcEIfRpkNG-WzTs_mw54oaFx4KmkE3fRCH0I6ueiXsVO2ldfMOUNIkfFLQQSGNpr_k9P2k36WkpwQIX-UPT1l5FzWxSbv50LYz2_hKu5Ukznn6XJOCBRrJwL9Y6m1Ary4',1,'「ヒューマニズムに根差した良い本は、 時代を超えて人々の心をつかむのです」 (ジャーナリスト/池上彰さん) 1937年に出版されて以来、数多くの人に読み継がれてきた、 吉野源三郎さんの名作「君たちはどう生きるか」。 今回前書きを書いてくださった池上さんも、 小学生時代に、父親から渡された当初は 読もうとしなかったのですが、気がつくと夢中になって どんどん読み進んでいたと言います。 人間としてどう生きればいいのか、楽しく読んでいるうちに 自然と考えるように書かれた本書は、子供はもちろん 多くの大人たちにも共感をもって迎えられてきました。 勇気、いじめ、貧困、格差、教養、、、 昔も今も変わらない人生のテーマに真摯に向き合う 主人公のコペル君と叔父さん。 二人の姿勢には、数多くの生き方の指針となる言葉が示されています。 活字も大きくなった読みやすい新装版で、 ぜひ、色褪せない名作の面白さを堪能してください。 《全国学校図書館協議会選定図書》',1),(8,'マスカレードホテル','東野圭吾','2014-7-18',499,0,1100,997,'文芸作品','https://m.media-amazon.com/images/I/41Hk2Z2gEHL._SY445_SX342_.jpg',1,'都内で起きた不可解な連続殺人事件。容疑者もターゲットも不明。残された暗号から判明したのは、次の犯行場所が一流ホテル・コルテシア東京ということのみ。若き刑事・新田浩介は、ホテルマンに化けて潜入捜査に就くことを命じられる。彼を教育するのは、女性フロントクラークの山岸尚美。次から次へと怪しげな客たちが訪れる中、二人は真相に辿り着けるのか!? 大人気シリーズ第1弾のミリオンセラー。  登場人物紹介 山岸尚美 ホテル・コルテシアのフロントクローク。人の役に立ちたいと思い、昔からホテルで働くことを夢見ていた。徹底したプロ意識を持っており、努力を惜しまない。並外れた観察力と記憶力でお客様をもてなし、お客様の“仮面\"を守ることを信念としている。 新田浩介 警視庁捜査一課所属の若きエリート刑事。国際弁護士の息子で、十代の前半の二年あまりをロサンゼルスで過ごす。知能犯との対決を夢見てこの世界に入った。生意気なところがあり、先輩刑事と衝突することも。大胆な発想力と行動力で事件解決に貢献する。',1),(9,'すし図鑑','著者','2013-4-23',1500,0,1540,510,'哲学','https://m.media-amazon.com/images/I/51W349Ag6LL._UF1000,1000_QL80_.jpg',1,'高級店から回転寿司まで、代表的なおすし約320貫とネタ元の魚を写真で解説する、おすしの図鑑です。',1),(10,'パンどろぼう','柴田ケイコ','2020-4-16',996,0,1540,4,'絵本','https://m.media-amazon.com/images/I/71uSL6n0WKL._SY342_.jpg',0,'”パンどろぼう”って、なにもの!? 読み聞かせが楽しいユーモア絵本  まちのパンやから サササッと とびだす ひとつのかげ。 パンが パンをかついで にげていきます。 「おれは パンどろぼう。おいしいパンを さがしもとめる おおどろぼうさ」  パンに包まれた、その正体とは――!? お茶目で憎めないパンどろぼうが、今日も事件をまきおこす!',1),(11,'<通常版>はらぺこあおむし','エリック・カール','1976-5-1',494,0,1200,7,'フィクション','https://www.kaiseisha.co.jp/special/ericcarle/harapekoaomushi/img/img_sec3_1.png',0,'一般的な大きさの元祖『はらぺこあおむし』。',1),(12,'ひみつのおすしやさん','黒岩まゆ','2024-11-20',1500,0,1650,1000,'絵本','https://m.media-amazon.com/images/I/81xVWtOJbEL._SY385_.jpg',0,'まちで人気のおすしやさん。じつは特別なおすしがまわってくるという噂が…  ■令和の恵比寿天 「すしざんまい」社長 木村 清さんもオススメ! のりちゃん、素晴らしい海の世界のお寿司屋さんを教えてくれて、ありがとう! 次は私が、『マグロ大王』の国をご案内しましょう! (令和の恵比寿天 「すしざんまい」社長 木村 清)  ■あらすじ 今日はのりちゃんの誕生日。家族でおすしやさんにやってきました。このお店には、ときどき特別なおすしがまわってくるというウワサがあります。 「あっ!」。食事中にお箸を落としてしまったのりちゃん。お箸を拾おうとテーブルの下にもぐり込むと、もうひとつのレーンを見つけます。 「よおし、いってみよう!」好奇心旺盛なのりちゃんは、大きなお皿にのって進みます。 たどり着いたのは海の神様たちがいる世界。実は、何者かがのりちゃんのあとをつけていて…!?',1),(13,'推しの子','赤坂アカ×横槍メンゴ','2020-7-17',496,0,659,504,'漫画','https://m.media-amazon.com/images/I/71ABgztQ2OL._SY385_.jpg',1,'「この芸能界（せかい）において嘘は武器だ」　地方都市で、産婦人科医として働くゴロー。芸能界とは無縁の日々。一方、彼の“推し”のアイドル・星野アイは、スターダムを上り始めていた。そんな二人が“最悪”の出会いを果たし、運命が動き出す…!?　“赤坂アカ×横槍メンゴ”の豪華タッグが全く新しい切り口で“芸能界”を描く衝撃作開幕!!',1),(14,'時をかける少女 アニメ版 ','筒井 康隆 (原著)','2007-4-1',200,0,1320,305,'短編小説','https://m.media-amazon.com/images/I/81T8VzJ7sGL._SY425_.jpg',0,'タイムリープという能力を手にした女子高校生の真琴は、「人生のかけがえのない時間」の意味を見つけ出していく。瑞々しいひと夏を描いた青春物語。',1),(15,'くりかえし計算ドリル','不明','不明',495,0,540,6,'哲学','https://www.sing.co.jp/bwp/wp-content/uploads/2024dkuri-kei-1.jpg',0,'くりかえし練習で、基礎基本を確実に定着！',1),(16,'角川つばさ文庫版 サマーウォーズ ','細田 守 (原名)','2009-8-9',1500,0,814,504,'短編小説','https://m.media-amazon.com/images/I/71+Wd+XcZTL._SY466_.jpg',0,'健二は、高校2年の夏休み、あこがれの先輩・夏希にフィアンセのふりをしてくれという奇妙なアルバイトを頼まれて長野に来ていた。健二がなんとかバイトをこなしている間に、ネットの世界では異変が起こっていた!',1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
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
