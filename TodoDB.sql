DROP DATABASE IF EXISTS TodoDB;

CREATE DATABASE TodoDB;

USE TodoDB;

CREATE TABLE Users(
	Id BINARY(16) PRIMARY KEY,
    Username VARCHAR(100) UNIQUE NOT NULL,
    `Password` VARCHAR(250) NOT NULL
);

CREATE TABLE Todos(
	Id BINARY(16)  PRIMARY KEY,
    `Name` VARCHAR(50) NOT NULL,
    `Description` VARCHAR(255) NULL,
    User_Id VARCHAR(36) NOT NULL,
    Start_Date DATE NOT NULL,
    End_Date DATE NULL,
    Finished BOOLEAN NOT NULL DEFAULT 0,
   FOREIGN KEY(User_Id) REFERENCES Users(Id)
);

CREATE TABLE Roles(
	Id BINARY(16)  PRIMARY KEY,
	Authority VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Users_Roles(
	User_Id BINARY(16)  NOT NULL,
    Role_Id BINARY(16)  NOT NULL,
    PRIMARY KEY(User_Id,Role_Id),
	FOREIGN KEY(User_Id) REFERENCES Users(Id),
	FOREIGN KEY(Role_Id) REFERENCES Roles(Id)
);

