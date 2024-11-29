CREATE TABLE tbl_users (
    id INT(11) AUTO_INCREMENT PRIMARY KEY,
    nama_user TEXT,
    kontak_email TEXT ,
    user_name TEXT,
    user_password TEXT 
)

SHOW TABLES;

SELECT * FROM tbl_users;

INSERT INTO tbl_users 
(nama_user, kontak_email, user_name, user_password)
VALUES 
('fajar alfaris', 'fajaraje00@gmail.com', 'fajaraje', '12345678');

SELECT COUNT(id) as user_exist FROM tbl_user WHERE user_name = 'fajaraje' AND user_password = '12345678';

SELECT * FROM tbl_users WHERE user_name = 'fajaraje' AND user_password = '12345678';