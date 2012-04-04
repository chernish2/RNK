SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `workmap` DEFAULT CHARACTER SET utf8 ;
USE `workmap` ;

-- -----------------------------------------------------
-- Table `workmap`.`address`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `workmap`.`address` (
  `address_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `address_longitude` DOUBLE NULL DEFAULT NULL ,
  `address_latitude` DOUBLE NULL DEFAULT NULL ,
  `address_city` VARCHAR(256) NULL DEFAULT NULL ,
  `address_street` VARCHAR(256) NULL DEFAULT NULL ,
  `address_building` VARCHAR(256) NULL DEFAULT NULL ,
  PRIMARY KEY (`address_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 610
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `workmap`.`query_table`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `workmap`.`query_table` (
  `query_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `query_str` VARCHAR(256) NULL DEFAULT NULL ,
  `query_count` INT(11) NULL DEFAULT NULL ,
  `query_last_updated` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`query_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = utf8
COMMENT = '	';


-- -----------------------------------------------------
-- Table `workmap`.`salary`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `workmap`.`salary` (
  `salary_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `salary_from` INT(11) NULL DEFAULT NULL ,
  `salary_to` INT(11) NULL DEFAULT NULL ,
  `salary_currency` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`salary_id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 610
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `workmap`.`vacancy`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `workmap`.`vacancy` (
  `vacancy_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `vacancy_name` VARCHAR(256) NULL DEFAULT NULL ,
  `vacancy_address_id` INT(11) NULL DEFAULT NULL ,
  `vacancy_salary_id` INT(11) NULL DEFAULT NULL ,
  `vacancy_xmlid` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`vacancy_id`) ,
  INDEX `vacancy_address_id` (`vacancy_address_id` ASC) ,
  INDEX `vacancy_salary_id` (`vacancy_salary_id` ASC) ,
  CONSTRAINT `vacancy_address_id`
    FOREIGN KEY (`vacancy_address_id` )
    REFERENCES `workmap`.`address` (`address_id` )
    ON DELETE SET NULL
    ON UPDATE NO ACTION,
  CONSTRAINT `vacancy_salary_id`
    FOREIGN KEY (`vacancy_salary_id` )
    REFERENCES `workmap`.`salary` (`salary_id` )
    ON DELETE SET NULL
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 610
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `workmap`.`query_result`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `workmap`.`query_result` (
  `idquery_result` INT(11) NOT NULL AUTO_INCREMENT ,
  `query_id` INT(11) NULL DEFAULT NULL ,
  `vacancy_id` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`idquery_result`) ,
  INDEX `query_id` (`query_id` ASC) ,
  INDEX `vacancy_id` (`vacancy_id` ASC) ,
  CONSTRAINT `query_id`
    FOREIGN KEY (`query_id` )
    REFERENCES `workmap`.`query_table` (`query_id` )
    ON DELETE SET NULL
    ON UPDATE NO ACTION,
  CONSTRAINT `vacancy_id`
    FOREIGN KEY (`vacancy_id` )
    REFERENCES `workmap`.`vacancy` (`vacancy_id` )
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 610
DEFAULT CHARACTER SET = utf8;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
