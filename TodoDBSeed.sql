USE TodoDB;

INSERT INTO Users(Username, `Password`) VALUES('Zun','$2a$10$S8nFUMB8YIEioeWyap24/ucX.dC6v9tXCbpHjJVQUkrXlrH1VLaAS');
INSERT INTO Roles(Authority) VALUES('USER'),('ADMIN');
INSERT INTO Users_Roles(User_Id,Role_Id) VALUES(1,1),(1,2);

INSERT INTO Todos(`Name`,Start_Date,User_Id) VALUES('Walk Dog','2020-07-29',1);