-- Operaciones CRUD sobre la tabla producto utilizando el nuevo usuario

-- Mostrar todos los productos y productos filtrados
SELECT * FROM producto;
SELECT * FROM producto WHERE categoria = 'Figura';
SELECT * FROM producto WHERE stock < 20;

-- Actualizar productos según condiciones
UPDATE producto
SET precio = 49.99
WHERE nombre = 'Figura de Naruto Uzumaki';
UPDATE producto
SET nombre = 'Figura de Sailor Moon Deluxe'
WHERE nombre = 'Figura de Sailor Moon';

-- Eliminar productos según condiciones
DELETE FROM producto WHERE nombre = 'Póster de Evangelion';
DELETE FROM producto WHERE stock = 0;
