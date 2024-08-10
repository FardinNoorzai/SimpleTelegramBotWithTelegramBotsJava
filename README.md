beside this telegram bot i also included a simple java swing file explorer github repository because I thought this telegram bot might be rejected because it’s not a swing application 
the quality of screenshots might be low because you put a 10 mb limit and i had to compress the pictures

i used state pattern to keep track of user states and along with that i used builder pattern for creating objects which needed to be created repeatedly singleton pattern for database connection log4j(logging for java) for logging features and TelegramBots java framework for accessing telegram bots api

To use the telegram bot first of all open the project using an IDE (Intellij ide recomended) and then go to the resources folder open the config.properties file then open the telegram and open the bot father https://t.me/BotFather create a bot after creating the bot the official telegram bot I mean bot father will give you a unique token copy that and assign that to bot.token in config.properties the result should be like this
bot.token=6892695515:AAF2akxbykTWyDX-TufRO9RbeytalCYv4bM
then assign the username which you assigned to your bot in the bot father to bot.username result should be like this
bot.username=myjavaclassprojectbot

after that find your userid using user info bot and assign it to admin.id using userinfo bot you can find you id https://t.me/userinfobot this bot will respond like this 
@Fardin_Noorzai
Id: 485898907
First: Fardin
Lang: en
Copy the id and assign it to the admin.id like this
admin.id=485898907
you can have many admins just by providing a list of userids instead of a single one like this
admin.id=485898907,55534245,6243534657,6586976435
remember not to add space after or before ,

then you have to provide a channel id to channel.id in the config.properties this should be the channel which you want bot to force the users to join to this channel before using the bot also it only force the users not admins you can find your channel id using telegram web open telegram in browser and sign in to your account open your channel chat in telegram web and the url should be like this https://web.telegram.org/a/#-1002179775394 after the # it’s your telegram channel id -1002179775394 assign it to the 
channel.id=-1002179775394
and assign the telegram channel username to the channel.username
channel.username=https://t.me/classProjectChannel

then assign your mysql database username and password to the config.properties file like this
database.username=root
database.password=12345
then open mysql workbench or shell and run these queris
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema telegrambot
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema telegrambot
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `telegrambot` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `telegrambot` ;

-- -----------------------------------------------------
-- Table `telegrambot`.`files`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `telegrambot`.`files` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `file_id` VARCHAR(255) NULL DEFAULT NULL,
  `file_size` VARCHAR(32) NULL DEFAULT NULL,
  `file_name` VARCHAR(128) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `telegrambot`.`logs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `telegrambot`.`logs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `level` VARCHAR(32) NULL DEFAULT NULL,
  `date` VARCHAR(128) NULL DEFAULT NULL,
  `logger` VARCHAR(128) NULL DEFAULT NULL,
  `message` VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 1426
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `telegrambot`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `telegrambot`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NULL DEFAULT NULL,
  `username` VARCHAR(64) NULL DEFAULT NULL,
  `userid` VARCHAR(32) NULL DEFAULT NULL,
  `role` VARCHAR(32) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


I hardcoded database schema into the Database.java class cause I had limited time (6 hourse) so the database name and tables name should be exactly like in the script i provided but with a little bit of refactoring you can change the class and add database name and table names into the properties file

After that open the pom.xml file and add these dependencies to the pom.xml file and let maven to download the required dependencies from maven central repository and reload the project
<dependencies>
    <dependency>
        <groupId>log4j</groupId>
        <artifactId>log4j</artifactId>
        <version>1.2.17</version>
    </dependency>

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>9.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.telegram</groupId>
        <artifactId>telegrambots</artifactId>
        <version>6.5.0</version>
    </dependency>
</dependencies>

after the dependencies got downloaded you can run the project

Also the log4j.properties is a configuration file for log4j(logging for java) library which provide advance logging features for logging application activities to the console and jdbc in this case(my project) you can modify that if you would like to i provided screen shots of console logs



 
