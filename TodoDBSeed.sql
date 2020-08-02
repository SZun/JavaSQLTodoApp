USE TodoDB;	

SET @Zun_Id  = (UUID_TO_BIN('6ccd780c-baba-1026-9564-5b8c656024db', true));
SET @Admin_Role_Id  = (UUID_TO_BIN('3eda4540-ffe3-4083-8cbd-78955baa81e6', true));
SET @User_Role_Id  = (UUID_TO_BIN('2bd87c4e-39ac-41fd-854d-b480af2374e4', true));

INSERT INTO Users(Id, Username, `Password`) VALUES(@Zun_Id, 'Zun','$2a$10$S8nFUMB8YIEioeWyap24/ucX.dC6v9tXCbpHjJVQUkrXlrH1VLaAS');	
INSERT INTO Roles(Id, Authority) VALUES(@User_Role_Id, 'USER'),(@Admin_Role_Id,'ADMIN');
INSERT INTO Users_Roles(User_Id,Role_Id) VALUES(@Zun_Id,@Admin_Role_Id),(@Zun_Id,@User_Role_Id);