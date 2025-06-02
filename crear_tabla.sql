-- Crear la base de datos akihabara_db
CREATE DATABASE IF NOT EXISTS akihabara_db;
USE akihabara_db;

-- Crear la tabla producto
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    categoria VARCHAR(100) CHECK (categoria IN ('Figura', 'Manga', 'Póster', 'Llavero', 'Ropa')),
    precio DECIMAL(10, 2),
    stock INT
);

-- Crear el usuario userAkihabara
CREATE USER IF NOT EXISTS 'userAkihabara'@'localhost' IDENTIFIED BY 'curso';

-- Otorgar permisos CRUD (SELECT, INSERT, UPDATE, DELETE) sobre la tabla producto
GRANT SELECT, INSERT, UPDATE, DELETE ON akihabara_db.producto TO 'userAkihabara'@'localhost';

-- Aplicar los permisos
FLUSH PRIVILEGES;

-- Insertar productos de distintas categorías
INSERT INTO producto (nombre, categoria, precio, stock) VALUES
('Figura de Naruto Uzumaki', 'Figura', 45.99, 15),
('Manga One Piece Vol. 1', 'Manga', 12.50, 30),
('Póster de Attack on Titan', 'Póster', 8.99, 50),
('Llavero de Pikachu', 'Llavero', 5.99, 100),
('Camiseta de Dragon Ball Z', 'Ropa', 19.99, 25),
('Figura de Sailor Moon', 'Figura', 39.99, 10),
('Manga Demon Slayer Vol. 3', 'Manga', 11.75, 20),
('Póster de My Hero Academia', 'Póster', 7.50, 40),
('Llavero de Totoro', 'Llavero', 6.50, 80),
('Sudadera de Studio Ghibli', 'Ropa', 34.99, 15),
('Figura de Goku Super Saiyan', 'Figura', 50.00, 8),
('Manga Jujutsu Kaisen Vol. 5', 'Manga', 13.25, 25),
('Póster de Evangelion', 'Póster', 9.25, 30),
('Llavero de Chainsaw Man', 'Llavero', 5.75, 60),
('Pantalones de Haikyuu', 'Ropa', 29.99, 12);