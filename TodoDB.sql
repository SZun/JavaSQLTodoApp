DROP DATABASE IF EXISTS TodoDB;

CREATE DATABASE TodoDB;

USE TodoDB;

CREATE TABLE Users(
	Id INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(100) UNIQUE NOT NULL,
    `Password` VARCHAR(250) NOT NULL
);

CREATE TABLE Todos(
	Id INT PRIMARY KEY AUTO_INCREMENT,
    `Name` VARCHAR(50) NOT NULL,
    `Description` VARCHAR(255) NULL,
    Username VARCHAR(100) NOT NULL,
    Start_Date DATE NOT NULL,
    End_Date DATE NULL,
    Finished BOOLEAN NOT NULL DEFAULT 0,
   FOREIGN KEY(Username) REFERENCES Users(Username)
);

CREATE TABLE Roles(
	Id INT PRIMARY KEY AUTO_INCREMENT,
	Authority VARCHAR(50) NULL
);

CREATE TABLE Users_Roles(
	User_Id INT NOT NULL,
    Role_Id INT NOT NULL,
    PRIMARY KEY(User_Id,Role_Id),
	FOREIGN KEY(User_Id) REFERENCES Users(Id),
	FOREIGN KEY(Role_Id) REFERENCES Roles(Id)
);

INSERT INTO Users(Username, `Password`) VALUES('Sam','password');
INSERT INTO Roles(Authority) VALUES('USER'),('ADMIN');
UPDATE Users SET `Password` = '$2a$10$S8nFUMB8YIEioeWyap24/ucX.dC6v9tXCbpHjJVQUkrXlrH1VLaAS' WHERE id = 1;
INSERT INTO Users_Roles(User_Id,Role_Id) VALUES(1,1);
INSERT INTO Todos(`Name`, Username, Start_Date) VALUES('Walk Dog', 'Sam', '2020-07-28');

Select * from todos;

