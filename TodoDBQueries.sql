USE TodoDB;

-- get all todos
SELECT * FROM Todos;
-- get one todo by id and user id
SELECT * FROM Todos WHERE Id = 1 AND User_Id = 1;
-- get one todo by id
SELECT * FROM Todos WHERE Id = 1 ;

-- get all users
SELECT * FROM Users;
-- get user by id
SELECT * FROM Users WHERE Id = 1;
-- get all users with certain role by role id
SELECT u .* FROM Users u 
JOIN Users_Roles ur ON u.Id = ur.User_Id
WHERE ur.Role_Id = 1;

-- get all roles
SELECT * FROM Roles;
-- get role by id
SELECT * FROM Roles WHERE Id = 1;
-- get all roles with certain user by user id
SELECT r.* FROM Roles r
JOIN Users_Roles ur ON r.Id = ur.Role_Id
WHERE ur.User_Id = 1;

-- get all Users_Roles
SELECT * FROM Users_Roles;
-- get all users in Users_Roles
SELECT u .* FROM Users_Roles ur
JOIN Users u ON u.Id = ur.User_Id;
-- get all roles in Users_Roles
SELECT r.* FROM Users_Roles ur
JOIN Roles r ON r.Id = ur.Role_Id


