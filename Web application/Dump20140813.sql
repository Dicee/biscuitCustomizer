CREATE DATABASE  IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `test`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.6.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `batch_customization`
--

DROP TABLE IF EXISTS `batch_customization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch_customization` (
  `Batch_ID` bigint(20) NOT NULL,
  `customizations_ID` int(11) NOT NULL,
  PRIMARY KEY (`Batch_ID`,`customizations_ID`),
  KEY `FK_BATCH_CUSTOMIZATION_customizations_ID` (`customizations_ID`),
  CONSTRAINT `FK_BATCH_CUSTOMIZATION_Batch_ID` FOREIGN KEY (`Batch_ID`) REFERENCES `batch` (`ID`),
  CONSTRAINT `FK_BATCH_CUSTOMIZATION_customizations_ID` FOREIGN KEY (`customizations_ID`) REFERENCES `customization` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch_customization`
--

LOCK TABLES `batch_customization` WRITE;
/*!40000 ALTER TABLE `batch_customization` DISABLE KEYS */;
INSERT INTO `batch_customization` VALUES (4,0),(4,1);
/*!40000 ALTER TABLE `batch_customization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientorder_batch`
--

DROP TABLE IF EXISTS `clientorder_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clientorder_batch` (
  `ClientOrder_ID` bigint(20) NOT NULL,
  `batches_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ClientOrder_ID`,`batches_ID`),
  KEY `FK_CLIENTORDER_BATCH_batches_ID` (`batches_ID`),
  CONSTRAINT `FK_CLIENTORDER_BATCH_batches_ID` FOREIGN KEY (`batches_ID`) REFERENCES `batch` (`ID`),
  CONSTRAINT `FK_CLIENTORDER_BATCH_ClientOrder_ID` FOREIGN KEY (`ClientOrder_ID`) REFERENCES `clientorder` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientorder_batch`
--

LOCK TABLES `clientorder_batch` WRITE;
/*!40000 ALTER TABLE `clientorder_batch` DISABLE KEYS */;
/*!40000 ALTER TABLE `clientorder_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clientorder`
--

DROP TABLE IF EXISTS `clientorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clientorder` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATIONDATE` bigint(20) DEFAULT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `OWNER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_CLIENTORDER_OWNER_ID` (`OWNER_ID`),
  CONSTRAINT `FK_CLIENTORDER_OWNER_ID` FOREIGN KEY (`OWNER_ID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clientorder`
--

LOCK TABLES `clientorder` WRITE;
/*!40000 ALTER TABLE `clientorder` DISABLE KEYS */;
INSERT INTO `clientorder` VALUES (1,1406933908451,'En cours de traitement',1),(2,1406934504678,'En cours de traitement',1),(3,1406934504688,'Créée',1);
/*!40000 ALTER TABLE `clientorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(255) DEFAULT NULL,
  `NOM` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `PRENOM` varchar(255) DEFAULT NULL,
  `DEFAULTADDRESS_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_USER_DEFAULTADDRESS_ID` (`DEFAULTADDRESS_ID`),
  CONSTRAINT `FK_USER_DEFAULTADDRESS_ID` FOREIGN KEY (`DEFAULTADDRESS_ID`) REFERENCES `address` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'a@b.cd','ez','64ce9a60d86c591bbcbcfa869daa23c2b864ea5a','fze',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customization`
--

DROP TABLE IF EXISTS `customization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customization` (
  `ID` int(11) NOT NULL,
  `DATA` varchar(255) DEFAULT NULL,
  `MODE` int(11) DEFAULT NULL,
  `SIZE` int(11) DEFAULT NULL,
  `X` float DEFAULT NULL,
  `Y` float DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customization`
--

LOCK TABLES `customization` WRITE;
/*!40000 ALTER TABLE `customization` DISABLE KEYS */;
INSERT INTO `customization` VALUES (0,'Votre texte',1,10,48,46.5),(1,'Sofia',0,40,60,60);
/*!40000 ALTER TABLE `customization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CODEPOSTAL` varchar(255) DEFAULT NULL,
  `LABEL` varchar(255) DEFAULT NULL,
  `NOM` varchar(255) DEFAULT NULL,
  `PRENOM` varchar(255) DEFAULT NULL,
  `RAISONSOCIALE` varchar(255) DEFAULT NULL,
  `VILLE` varchar(255) DEFAULT NULL,
  `VOIE` varchar(255) DEFAULT NULL,
  `OWNER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ADDRESS_OWNER_ID` (`OWNER_ID`),
  CONSTRAINT `FK_ADDRESS_OWNER_ID` FOREIGN KEY (`OWNER_ID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'fz','Adresse par défaut','ez','fze','fez','fz','fz',1);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (33,'Kikirirkili'),(35,'Couou'),(37,'courinou'),(38,'coucouille');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_clientorder`
--

DROP TABLE IF EXISTS `user_clientorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_clientorder` (
  `User_ID` bigint(20) NOT NULL,
  `orders_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`User_ID`,`orders_ID`),
  KEY `FK_USER_CLIENTORDER_orders_ID` (`orders_ID`),
  CONSTRAINT `FK_USER_CLIENTORDER_orders_ID` FOREIGN KEY (`orders_ID`) REFERENCES `clientorder` (`ID`),
  CONSTRAINT `FK_USER_CLIENTORDER_User_ID` FOREIGN KEY (`User_ID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_clientorder`
--

LOCK TABLES `user_clientorder` WRITE;
/*!40000 ALTER TABLE `user_clientorder` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_clientorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `batch`
--

DROP TABLE IF EXISTS `batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `batch` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `QT` int(11) DEFAULT NULL,
  `BISCUIT_REF` varchar(255) DEFAULT NULL,
  `ORDER_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_BATCH_ORDER_ID` (`ORDER_ID`),
  KEY `FK_BATCH_BISCUIT_REF` (`BISCUIT_REF`),
  CONSTRAINT `FK_BATCH_BISCUIT_REF` FOREIGN KEY (`BISCUIT_REF`) REFERENCES `biscuit` (`REF`),
  CONSTRAINT `FK_BATCH_ORDER_ID` FOREIGN KEY (`ORDER_ID`) REFERENCES `clientorder` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `batch`
--

LOCK TABLES `batch` WRITE;
/*!40000 ALTER TABLE `batch` DISABLE KEYS */;
INSERT INTO `batch` VALUES (1,5,'prince',NULL),(2,1,'prince',2),(3,1,'prince',2),(4,1,'prince',2);
/*!40000 ALTER TABLE `batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_address` (
  `User_ID` bigint(20) NOT NULL,
  `addresses_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`User_ID`,`addresses_ID`),
  KEY `FK_USER_ADDRESS_addresses_ID` (`addresses_ID`),
  CONSTRAINT `FK_USER_ADDRESS_addresses_ID` FOREIGN KEY (`addresses_ID`) REFERENCES `address` (`ID`),
  CONSTRAINT `FK_USER_ADDRESS_User_ID` FOREIGN KEY (`User_ID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

LOCK TABLES `user_address` WRITE;
/*!40000 ALTER TABLE `user_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usermetadata`
--

DROP TABLE IF EXISTS `usermetadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usermetadata` (
  `FIELD` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `USER_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`FIELD`,`USER_ID`),
  KEY `FK_USERMETADATA_USER_ID` (`USER_ID`),
  CONSTRAINT `FK_USERMETADATA_USER_ID` FOREIGN KEY (`USER_ID`) REFERENCES `user` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usermetadata`
--

LOCK TABLES `usermetadata` WRITE;
/*!40000 ALTER TABLE `usermetadata` DISABLE KEYS */;
/*!40000 ALTER TABLE `usermetadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customizationmetadata`
--

DROP TABLE IF EXISTS `customizationmetadata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customizationmetadata` (
  `FIELD` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `CUSTOM_ID` int(11) NOT NULL,
  PRIMARY KEY (`FIELD`,`CUSTOM_ID`),
  KEY `FK_CUSTOMIZATIONMETADATA_CUSTOM_ID` (`CUSTOM_ID`),
  CONSTRAINT `FK_CUSTOMIZATIONMETADATA_CUSTOM_ID` FOREIGN KEY (`CUSTOM_ID`) REFERENCES `customization` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customizationmetadata`
--

LOCK TABLES `customizationmetadata` WRITE;
/*!40000 ALTER TABLE `customizationmetadata` DISABLE KEYS */;
/*!40000 ALTER TABLE `customizationmetadata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `biscuit`
--

DROP TABLE IF EXISTS `biscuit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biscuit` (
  `REF` varchar(255) NOT NULL,
  `EDGELENGTH` int(11) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PIECESPERBATCH` int(11) DEFAULT NULL,
  `PRICE` float DEFAULT NULL,
  `XTOP` int(11) DEFAULT NULL,
  `YTOP` int(11) DEFAULT NULL,
  PRIMARY KEY (`REF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `biscuit`
--

LOCK TABLES `biscuit` WRITE;
/*!40000 ALTER TABLE `biscuit` DISABLE KEYS */;
INSERT INTO `biscuit` VALUES ('heart',100,'Coeur sablé',14,3.55,400,349),('prince',140,'Fourrés au chocolat',13,1.55,840,617);
/*!40000 ALTER TABLE `biscuit` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-08-13 13:46:00
