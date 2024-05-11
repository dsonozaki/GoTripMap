-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema server
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema server
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `server` DEFAULT CHARACTER SET utf8 ;
USE `server` ;

-- -----------------------------------------------------
-- Table `server`.`profiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `server`.`profiles` ;

CREATE TABLE IF NOT EXISTS `server`.`profiles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` MEDIUMTEXT NULL,
  `phone` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `photo` MEDIUMTEXT NULL,
  `hash` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `hash_UNIQUE` (`hash` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `server`.`otp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `server`.`otp` ;

CREATE TABLE IF NOT EXISTS `server`.`otp` (
  `profile_id` INT NOT NULL,
  `otp` VARCHAR(45) NULL,
  PRIMARY KEY (`profile_id`),
  INDEX `fk_codes_profiles_idx` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `fk_codes_profiles`
    FOREIGN KEY (`profile_id`)
    REFERENCES `server`.`profiles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `server`.`entries`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `server`.`entries` ;

CREATE TABLE IF NOT EXISTS `server`.`entries` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `entry` MEDIUMTEXT NULL,
  `dateTime` VARCHAR(45) NULL,
  `transport` VARCHAR(45) NULL,
  `startPointPlace` MEDIUMTEXT NULL,
  `endPointPlace` MEDIUMTEXT NULL,
  `length` VARCHAR(45) NULL,
  `profile_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_entries_profiles1_idx` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `fk_entries_profiles1`
    FOREIGN KEY (`profile_id`)
    REFERENCES `server`.`profiles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `server`.`routes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `server`.`routes` ;

CREATE TABLE IF NOT EXISTS `server`.`routes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `length` VARCHAR(45) NULL,
  `route` MEDIUMTEXT NULL,
  `startPointPlace` MEDIUMTEXT NULL,
  `startPointAddress` MEDIUMTEXT NULL,
  `endPointPlace` MEDIUMTEXT NULL,
  `endPointAddress` MEDIUMTEXT NULL,
  `imageLink` MEDIUMTEXT NULL,
  `timeRequired` VARCHAR(45) NULL,
  `transport` VARCHAR(45) NULL,
  `searchEntry` INT NULL,
  `liked` TINYINT NULL,
  `profile_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_routes_profiles1_idx` (`profile_id` ASC) VISIBLE,
  CONSTRAINT `fk_routes_profiles1`
    FOREIGN KEY (`profile_id`)
    REFERENCES `server`.`profiles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
