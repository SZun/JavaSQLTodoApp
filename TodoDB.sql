DROP DATABASE IF EXISTS TodoDB;

CREATE DATABASE TodoDB;

USE TodoDB;

CREATE TABLE Todos(
	Id INT PRIMARY KEY AUTO_INCREMENT,
    `Name` VARCHAR(50) NOT NULL,
    `Description` VARCHAR(255) NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NULL,
    Finished BOOLEAN NOT NULL DEFAULT 0
);