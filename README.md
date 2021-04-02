# delivery-services-app

docker run --name delivery-service-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql



DROP DATABASE IF EXISTS orderservice;
DROP USER IF EXISTS 'usr_order_service'@'%';
CREATE DATABASE IF NOT EXISTS orderservice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'usr_order_service'@'%' IDENTIFIED WITH mysql_native_password BY 'orderpassword';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON orderservice.* TO 'usr_order_service'@'%';
FLUSH PRIVILEGES;


DROP DATABASE IF EXISTS productcatalogservice;
DROP USER IF EXISTS 'usr_prduct_cat_service'@'%';
CREATE DATABASE IF NOT EXISTS productcatalogservice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'usr_prduct_cat_service'@'%' IDENTIFIED WITH mysql_native_password BY 'prodpassword';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON productcatalogservice.* TO 'usr_prduct_cat_service'@'%';
FLUSH PRIVILEGES;

DROP DATABASE IF EXISTS identitystore;
DROP USER IF EXISTS 'usr_identitystore'@'%';
CREATE DATABASE IF NOT EXISTS identitystore CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;                  
CREATE USER IF NOT EXISTS 'usr_identitystore'@'%' IDENTIFIED WITH mysql_native_password BY 'securepassword';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON identitystore.* TO 'usr_identitystore'@'%';
FLUSH PRIVILEGES;



docker run --name delivery-service-redis -p 6379:6379 -d redis
