-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema timey
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema timey
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `timey` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `timey` ;

-- -----------------------------------------------------
-- Table `timey`.`calendar_dimension`
-- -----------------------------------------------------
DROP TABLE IF EXISTS calendar_dimension;
CREATE TABLE IF NOT EXISTS `timey`.`calendar_dimension` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `db_date` DATE NOT NULL,
  `year` INT NOT NULL,
  `month` INT NOT NULL,
  `day` INT NOT NULL,
  `quarter` INT NOT NULL,
  `week` INT NOT NULL,
  `day_name` VARCHAR(10) NOT NULL,
  `month_name` VARCHAR(9) NOT NULL,
        holiday_flag            CHAR(1) DEFAULT 'f' CHECK (holiday_flag in ('t', 'f')),
        weekend_flag            CHAR(1) DEFAULT 'f' CHECK (weekday_flag in ('t', 'f')),
        big_event                   VARCHAR(255),
        UNIQUE td_ymd_idx (year,month,day),
        UNIQUE td_dbdate_idx (db_date), 
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `timey`.`time_dimension`
-- -----------------------------------------------------
DROP TABLE IF EXISTS time_dimension;
CREATE TABLE IF NOT EXISTS `timey`.`time_dimension` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_time` TIME NULL,
  `end_time` TIME NULL,
  `total_time` DECIMAL(10,2) NULL,
  `category` VARCHAR(50) NULL,
  `note` VARCHAR(250) NULL,
  `calendar_dimension_ref` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_time_dimension_calendar_dimension_idx` (`calendar_dimension_ref` ASC),
  CONSTRAINT `fk_time_dimension_calendar_dimension`
    FOREIGN KEY (`calendar_dimension_ref`)
    REFERENCES `timey`.`calendar_dimension` (`id`)
    ON DELETE cascade
    ON UPDATE cascade)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

DROP PROCEDURE IF EXISTS fill_date_dimension;
DELIMITER //
CREATE PROCEDURE fill_date_dimension(IN startdate DATE,IN stopdate DATE)
BEGIN
    DECLARE currentdate DATE;
    SET currentdate = startdate;
    WHILE currentdate < stopdate DO
        INSERT INTO calendar_dimension VALUES (
                        YEAR(currentdate)*10000+MONTH(currentdate)*100 + DAY(currentdate),
                        currentdate,
                        YEAR(currentdate),
                        MONTH(currentdate),
                        DAY(currentdate),
                        QUARTER(currentdate),
                        WEEKOFYEAR(currentdate),
                        DATE_FORMAT(currentdate,'%W'),
                        DATE_FORMAT(currentdate,'%M'),
                        'f',
                        CASE DAYOFWEEK(currentdate) WHEN 1 THEN 't' WHEN 7 then 't' ELSE 'f' END,
                        NULL);
                        
        SET currentdate = ADDDATE(currentdate,INTERVAL 1 DAY);
    END WHILE;
END
//
DELIMITER ;

 
CALL fill_date_dimension('1900-01-01','2050-01-01');

