SET SESSION lc_time_names = 'de_DE'; -- set language to get names of days and months in that language

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema timey_wimey
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema timey_wimey
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `timey_wimey`;
CREATE SCHEMA IF NOT EXISTS `timey_wimey` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `timey_wimey` ;

-- -----------------------------------------------------
-- Table `timey_wimey`.`user`calendar_dimension
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timey_wimey`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `timey_wimey`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timey_wimey`.`category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(75) NOT NULL,
  `user_ref` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_category_user_idx` (`user_ref` ASC),
  CONSTRAINT `fk_category_user`
    FOREIGN KEY (`user_ref`)
    REFERENCES `timey_wimey`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `timey_wimey`.`calendar_dimension`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timey_wimey`.`calendar_dimension` (
  `id` INT NOT NULL,
  `user_calendar_ref` INT NOT NULL,
  `db_date` DATE NOT NULL,
  `year` INT NOT NULL,
  `month` INT NOT NULL,
  `day` INT NOT NULL,
  `quarter` INT NOT NULL,
  `week` INT NOT NULL,
  `day_name` VARCHAR(10) NOT NULL,
  `month_name` VARCHAR(9) NOT NULL,
        weekend_flag            CHAR(1) DEFAULT 'f' CHECK (weekday_flag in ('t', 'f')),
        UNIQUE td_ymd_idx (year,month,day),
        UNIQUE td_dbdate_idx (db_date),
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '		';


-- -----------------------------------------------------
-- Table `timey_wimey`.`user_calendar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timey_wimey`.`user_calendar` (
  `user_ref` INT NOT NULL,
  `calendar_dimension_ref` INT NOT NULL,
  `holiday_flag` VARCHAR(1) NULL DEFAULT 'f',
  `event` VARCHAR(255) NULL,
  PRIMARY KEY (`user_ref`, `calendar_dimension_ref`),
  INDEX `fk_user_has_calendar_dimension_calendar_dimension1_idx` (`calendar_dimension_ref` ASC),
  INDEX `fk_user_has_calendar_dimension_user1_idx` (`user_ref` ASC),
  CONSTRAINT `fk_user_calendar_user`
    FOREIGN KEY (`user_ref`)
    REFERENCES `timey_wimey`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_calendar_dimension`
    FOREIGN KEY (`calendar_dimension_ref`)
    REFERENCES `timey_wimey`.`calendar_dimension` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `timey_wimey`.`time_dimension`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `timey_wimey`.`time_dimension` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  `total_time` DOUBLE NOT NULL,
  `note` VARCHAR(255) NULL,
  `user_calendar_user_ref` INT NOT NULL,
  `user_calendar_calendar_dimension_ref` INT NOT NULL,
  `category_ref` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_time_dimension_user_has_calendar_dimension1_idx` (`user_calendar_user_ref` ASC, `user_calendar_calendar_dimension_ref` ASC),
  INDEX `fk_time_dimension_category1_idx` (`category_ref` ASC),
  CONSTRAINT `fk_time_dimension_user_has_calendar_dimension1`
    FOREIGN KEY (`user_calendar_user_ref` , `user_calendar_calendar_dimension_ref`)
    REFERENCES `timey_wimey`.`user_calendar` (`user_ref` , `calendar_dimension_ref`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_time_dimension_category1`
    FOREIGN KEY (`category_ref`)
    REFERENCES `timey_wimey`.`category` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

DROP PROCEDURE IF EXISTS fill_date_dimension;
DELIMITER //
CREATE PROCEDURE fill_date_dimension(IN startdate DATE,IN stopdate DATE)
BEGIN
    DECLARE currentdate DATE;
    DECLARE user_calendar_ref INT;
    SET user_calendar_ref = 1;
    SET currentdate = startdate;
    WHILE currentdate < stopdate DO
        INSERT INTO calendar_dimension VALUES (
                        YEAR(currentdate)*10000+MONTH(currentdate)*100 + DAY(currentdate),
                        user_calendar_ref,
                        currentdate,
                        YEAR(currentdate),
                        MONTH(currentdate),
                        DAY(currentdate),
                        QUARTER(currentdate),
                        WEEKOFYEAR(currentdate),
                        DATE_FORMAT(currentdate,'%W'),
                        DATE_FORMAT(currentdate,'%M'),
                        CASE DAYOFWEEK(currentdate) WHEN 1 THEN 't' WHEN 7 then 't' ELSE 'f' END);
		SET user_calendar_ref = user_calendar_ref + 1;
        SET currentdate = ADDDATE(currentdate,INTERVAL 1 DAY);
    END WHILE;
END
//
DELIMITER ;

DROP PROCEDURE IF EXISTS fill_user_calendar;
DELIMITER //
CREATE PROCEDURE fill_user_calendar(IN startid INT,IN stopid INT, IN userid INT)
BEGIN
    DECLARE currentid INT;
    DECLARE counter INT;
    SET counter = 1;
    SET currentid = startid;
    
    WHILE currentid < stopid DO
        INSERT INTO user_calendar (user_ref, calendar_dimension_ref)   
        VALUES (userid, currentid);
        SET counter = counter + 1;
        SET currentid = (SELECT id FROM calendar_dimension WHERE user_calendar_ref = counter);
        
    END WHILE;
END
//
DELIMITER ;

 
CALL fill_date_dimension('2000-01-01','2020-01-01');
INSERT INTO user (name, password) VALUES ('rene', 'test');
INSERT INTO user (name, password) VALUES ('simon', 'test');
CALL fill_user_calendar (20000101, 20200101, 1);
CALL fill_user_calendar (20000101, 20200101, 2);

