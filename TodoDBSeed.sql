USE TodoDBTest;

DELETE FROM Users_Roles;
DELETE FROM Roles;

INSERT INTO Users(Username, `Password`) VALUES('Zun','$2a$10$S8nFUMB8YIEioeWyap24/ucX.dC6v9tXCbpHjJVQUkrXlrH1VLaAS');
INSERT INTO Roles(Authority) VALUES('USER'),('ADMIN');
INSERT INTO Users_Roles(User_Id,Role_Id) VALUES(1,1),(1,2);

SELECT * FROM Roles;

