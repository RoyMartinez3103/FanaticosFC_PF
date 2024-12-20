--
-- Author :     Rodrigo Martínez Zambrano
-- Proyecto:    Fanáticos FC. Gestión de ventas de playeras de fútbol.
--

-- DROP DATABASE IF EXISTS FANATICOS_DB;
 CREATE DATABASE FANATICOS_DB;
   USE FANATICOS_DB;

--
-- TABLE: MARCA
-- Se guardarán las marcas de los fabricantes de las playeras.
--

CREATE TABLE MARCA(
    ID_MARCA    INT              NOT NULL   AUTO_INCREMENT,
    NOMBRE      VARCHAR(40)      NOT NULL,

    PRIMARY KEY (ID_MARCA),
    CONSTRAINT UK_NOMBRE UNIQUE (NOMBRE)
);


--
-- TABLE: DEUDOR
-- Se guardarán los datos de los deudores de las ventas a crédito.
--

CREATE TABLE DEUDOR(
    ID_DEUDOR       INT             NOT NULL   AUTO_INCREMENT,
    NOMBRE          VARCHAR(40)     NOT NULL,
    APELLIDO_PAT    VARCHAR(40)     NOT NULL,
    APELLIDO_MAT    VARCHAR(40)     NOT NULL,
    TELEFONO        VARCHAR(10)     NOT NULL,
    MAIL            VARCHAR(40)     NOT NULL,

    PRIMARY KEY (ID_DEUDOR),
    CONSTRAINT UK_TELEFONO UNIQUE (TELEFONO),
    CONSTRAINT UK_MAIL UNIQUE (MAIL)
);



--
-- TABLE: EQUIPO
-- Se guardarán los datos de los equipos.
--

CREATE TABLE EQUIPO(
    ID_EQUIPO    INT             NOT NULL   AUTO_INCREMENT,
    NOMBRE       VARCHAR(40)     NOT NULL,
    PAIS         VARCHAR(40)     NOT NULL,
    LIGA        VARCHAR(40)      NOT NULL,
    PRIMARY KEY (ID_EQUIPO),
    CONSTRAINT UK_NOMBRE UNIQUE (NOMBRE)
);



--
-- TABLE: PLAYERA
-- Se guardarán las características de las playeras.
--

CREATE TABLE PLAYERA(
    ID_PLAYERA      INT             NOT NULL    AUTO_INCREMENT,
    COLOR           VARCHAR(40)     NOT NULL,
    TALLA           VARCHAR(3)      NOT NULL CHECK (TALLA = 'XCH' OR TALLA = 'CH' OR TALLA = 'M' OR TALLA = 'G' OR TALLA = 'XG'),
    TIPO_MANGA      VARCHAR(10)     NOT NULL CHECK (TIPO_MANGA = 'CORTA' OR TIPO_MANGA = 'LARGA'),
    PRECIO_REAL     FLOAT           NOT NULL CHECK (PRECIO_REAL > 0),
    STOCK           INT             NOT NULL CHECK (STOCK > 0),
    PRECIO_VENTA    FLOAT           NOT NULL CHECK (PRECIO_VENTA > 0),
    IMAGEN_RUTA	  VARCHAR(255)		NOT NULL DEFAULT "Sin_ruta",
    ID_MARCA        INT             NOT NULL,
    ID_EQUIPO       INT             NOT NULL,
    PRIMARY KEY (ID_PLAYERA),
    CONSTRAINT FK_PLAYERA_MARCA FOREIGN KEY (ID_MARCA)
    REFERENCES MARCA(ID_MARCA),
    CONSTRAINT FK_PLAYERA_EQUIPO FOREIGN KEY (ID_EQUIPO)
    REFERENCES EQUIPO(ID_EQUIPO)
);

--
-- TABLE: USUARIO
-- Se guardarán los usuarios del sistema.
--

CREATE TABLE USUARIO(
    ID_USUARIO          INT             NOT NULL    AUTO_INCREMENT,
    NOMBRE              VARCHAR(40)     NOT NULL,
    APELLIDO_PAT        VARCHAR(40)     NOT NULL,
    APELLIDO_MAT        VARCHAR(40)     NOT NULL,
    FECHA_NAC           DATE            NOT NULL,
    RFC                 VARCHAR(13)     NOT NULL,
    MAIL                VARCHAR(40)     NOT NULL,
    USERNAME            VARCHAR(20)     NOT NULL,
    PASSWORD  				VARCHAR(255)     NOT NULL,
    ROL						VARCHAR(10)		 NOT NULL,
    VENTAS_REALIZADAS   INT             NOT NULL   DEFAULT 0,
    PRIMARY KEY (ID_USUARIO),
    CONSTRAINT UK_NOMBRE UNIQUE (NOMBRE, APELLIDO_PAT, APELLIDO_MAT),
    CONSTRAINT UK_MAIL UNIQUE (MAIL),
    CONSTRAINT UK_USR UNIQUE (USERNAME)
);

CREATE UNIQUE INDEX IDX_USUARIO_MAIL ON USUARIO(USERNAME,MAIL);


--
-- TABLE: ESTATUS_VENTA
--

CREATE TABLE ESTATUS_VENTA(
   ID_ESTATUS_VENTA    INTEGER         NOT NULL		AUTO_INCREMENT,
   ESTATUS             VARCHAR(20)     NOT NULL,
   DESCRIPCION         VARCHAR(100)    NOT NULL,
   PRIMARY KEY (ID_ESTATUS_VENTA),
	CONSTRAINT UK_STATUS UNIQUE (ESTATUS)
);

--
-- TABLE: VENTA
-- Se guardarán detalles de la transacción de una venta.
--

CREATE TABLE VENTA(
    ID_VENTA             INT              NOT NULL      AUTO_INCREMENT,
    MONTO_TOTAL          FLOAT            NOT NULL      CHECK (MONTO_TOTAL > 0),
    FECHA_VENTA          DATETIME         NOT NULL,
    ES_VENTA_CREDITO     BIT              NOT NULL,     CHECK (ES_VENTA_CREDITO = 1 OR ES_VENTA_CREDITO = 0),
    ID_USUARIO           INT              NOT NULL,
    ID_ESTATUS_VENTA     INTEGER          NOT NULL,

    PRIMARY KEY (ID_VENTA),
    CONSTRAINT FK_VENTA_USUARIO FOREIGN KEY (ID_USUARIO)
    REFERENCES USUARIO(ID_USUARIO),
    FOREIGN KEY (ID_ESTATUS_VENTA)
    REFERENCES ESTATUS_VENTA(ID_ESTATUS_VENTA)

);

--
-- TABLE: DETALLE_VENTA
-- Se guardan los detalles de la venta.
--

CREATE TABLE DETALLE_VENTA(
    ID_DETALLE_VENTA     INT     NOT NULL  AUTO_INCREMENT,
    CANTIDAD_PLAYERAS    INT     NOT NULL  CHECK (CANTIDAD_PLAYERAS > 0),
    ID_PLAYERA           INT     NOT NULL,
    ID_VENTA             INT     NOT NULL,
    PRIMARY KEY (ID_DETALLE_VENTA),
    CONSTRAINT FK_VENTA_PLAYERA FOREIGN KEY (ID_PLAYERA)
    REFERENCES PLAYERA(ID_PLAYERA),
    CONSTRAINT FK_VENTA_DETALLE FOREIGN KEY (ID_VENTA)
    REFERENCES VENTA(ID_VENTA)
);


--
-- TABLE: VENTA_CREDITO
-- Se guardan los datos de una venta a crédito.
--

CREATE TABLE VENTA_CREDITO(
    ID_VENTA_CREDITO    INT              NOT NULL	AUTO_INCREMENT,
    MONTO_RESTANTE	   FLOAT            NOT NULL,
    PAGOS_REALIZADOS    INT              NOT NULL DEFAULT 0,
    ID_DEUDOR           INT              NOT NULL,
    ID_VENTA            INT              NOT NULL,
    PRIMARY KEY (ID_VENTA_CREDITO),
    CONSTRAINT FK_VENTA_VCREDITO FOREIGN KEY (ID_VENTA)
    REFERENCES VENTA(ID_VENTA),
    CONSTRAINT FK_VENTA_DEUDOR FOREIGN KEY (ID_DEUDOR)
    REFERENCES DEUDOR(ID_DEUDOR)
);



--
-- TABLE: PAGOS_CREDITO
-- Se guardan los pagos realizados por los deudores.

CREATE TABLE PAGO_CREDITO(
    ID_PAGO             INT              NOT NULL   AUTO_INCREMENT,
    FECHA_PAGO          DATE             NOT NULL,
    MONTO_PAGO          FLOAT            NOT NULL CHECK (MONTO_PAGO > 0),
    ID_VENTA_CREDITO    INT              NOT NULL,
    PRIMARY KEY (ID_PAGO),
    CONSTRAINT FK_PAGOS_VENTA FOREIGN KEY (ID_VENTA_CREDITO)
    REFERENCES VENTA_CREDITO(ID_VENTA_CREDITO)
);

-- *********************************************************************
-- *************          INSERCIÓN DE DATOS      **********************
-- *********************************************************************


INSERT INTO MARCA (NOMBRE) VALUES
  ('Nike'),
  ('Adidas'),
  ('Puma'),
  ('Under Armour'),
  ('Charly'),
  ('Pirma'),
  ('Joma');


INSERT INTO DEUDOR (NOMBRE, APELLIDO_PAT, APELLIDO_MAT, TELEFONO, MAIL) VALUES
  ('Pedro', 'Rodríguez', 'Flores', '5566778832', 'pedro_rodriguez@correo.com'),
  ('Isabel', 'Sánchez', 'López', '5532125678', 'isabel_sanchez@correo.com'),
  ('Carlos', 'Martínez', 'Gómez', '5576543210', 'carlosmartinez1990@yahoo.com'),
  ('Ana', 'González', 'Pérez', '5565432109', 'ana.gonzalez@outlook.com'),
  ('José', 'Pérez', 'Sánchez', '5554321098', 'joseperez@protonmail.com');


INSERT INTO EQUIPO (NOMBRE, PAIS, LIGA) VALUES
('Cruz Azul FC', 'México', 'Liga MX'),
('Pumas', 'México', 'Liga MX'),
('Barcelona', 'España', 'LaLiga'),
('Real Madrid', 'España', 'LaLiga'),
('Manchester United', 'Inglaterra', 'Premier League'),
('Liverpool FC', 'Inglaterra', 'Premier League'),
('Bayern Munich', 'Alemania', 'Bundesliga'),
('Borussia Dortmund', 'Alemania', 'Bundesliga'),
('Boca Juniors', 'Argentina', 'Primera División'),
('River Plate', 'Argentina', 'Primera División'),
('Feyenoord','Paises Bajos','Eredivisie');



INSERT INTO PLAYERA (COLOR, TALLA, TIPO_MANGA, PRECIO_REAL, STOCK, PRECIO_VENTA, IMAGEN_RUTA, ID_MARCA, ID_EQUIPO) VALUES
('Azul', 'CH', 'CORTA', 800.0, 10, 1299.0, '/uploads/cruzazul.jpg', 6, 1), -- Prima, Cruz Azul
('Azul', 'M', 'CORTA', 800.0, 10, 1299.0, '/uploads/cruzazul.jpg', 6, 1),
('Blanco', 'CH', 'CORTA', 700.0, 15, 1199.0, '/uploads/pumas.jpg', 1, 2),  -- Nike, Pumas
('Vino', 'XG', 'CORTA', 500.00, 20, 1499.00, '/uploads/barcelona.jpg', 1, 3), -- Nike, Barcelona
('Blanco', 'M', 'LARGA', 900.00, 25, 1699.0, '/uploads/realmadrid.jpg', 2, 4),   -- Adidas, Real Madrid
('Blanco', 'G', 'LARGA', 900.00, 25, 1699.0, '/uploads/realmadrid.jpg', 2, 4),
('Rojo', 'CH', 'LARGA', 999.0, 12, 1599.0,'/uploads/manunited.jpg', 2,5),  -- Adidas, Man Uni
('Rojo', 'M', 'CORTA', 999.0, 9, 1499.0,'/uploads/liverpool.jpg', 1,6),  -- Nike, Liverpool
('Negro','G','CORTA',1099.0,18,1649.0,'/uploads/bmunich.jpg', 2,7),  -- Adidas, Bayern Mun
('Amarillo','XG','CORTA',1099.0,5,1349.0,'/uploads/borussiad.jpg', 3,8),  -- Puma, Borussia
('Azul','CH','CORTA',450.0,20,989.0,'/uploads/bocajrs.jpg', 2,9), -- Adidas, Boca J
('Blanco', 'M', 'LARGA', 650.0, 13, 1099.0, '/uploads/riverp.jpg', 2, 10), -- Adidas, River
('Dorado','CH','CORTA',699.0,12,1199,'/uploads/a510f710-21bd-439d-9f49-14ced2047930_pumas2.jpg', 1,2),
('Rojo','M','CORTA',999.0,5,1699.0,'/uploads/c5878885-e0cd-4527-bca0-f757b0311c9b_cruzazulr.jpg', 6,1),
('Negro', 'XG','CORTA', 899,5,1599.0,'/uploads/34d2323a-6c3f-4396-ba54-07c25acbe8db_feyenoord.jpg',2,11);


INSERT INTO USUARIO (NOMBRE, APELLIDO_PAT, APELLIDO_MAT, FECHA_NAC, RFC, USERNAME, PASSWORD, MAIL, ROL, VENTAS_REALIZADAS) VALUES
('Ana', 'García', 'López', '1990-01-01', 'GALA9001017D5', 'ana_garcia', '$2a$12$JVZLD0ZCvKjiveT4.Z/tW.ilsO.AgNd0p4NTlO7UjDXWrVrCJTZk6', 'ana_garcia@correo.com', "ADMIN", 3),  -- ADMIN
('Juan', 'Pérez', 'Martínez', '1985-07-15', 'PEMJ8507159O4', 'juan_perez', '$2a$12$MHclC1krGAgPUHwNdGr7uOqP1VcCf8LX8ZPCOV0FYzEfiXptoxo6C', 'juan_perez@correo.com', "USER", 5),
('María', 'Gómez', 'Hernández', '2000-12-24', 'GOHM001224R2A', 'maria_gomez', '$2a$12$ublIab/ToBmgeAD3qqL66OLX19p0f/ZdRSx7eKAvaxpI9ggEXscGC', 'maria_gomez@correo.com', "USER", 2);


INSERT INTO ESTATUS_VENTA(ESTATUS,DESCRIPCION) VALUES
('PAGADO','LA VENTA SE PAGÓ POR COMPLETO'),
('PENDIENTE', 'LA VENTA SE ENCUENTRA EN PROCESO DE PAGO'),
('CANCELADA', 'LA VENTA FUE CANCELADA');

INSERT INTO VENTA (MONTO_TOTAL, FECHA_VENTA, ES_VENTA_CREDITO,ID_USUARIO,ID_ESTATUS_VENTA) VALUES
(3897.0, '2024-06-01 10:00:00', 0, 1, 1), -- 1
(1199.0, '2024-06-02 11:30:00', 1, 3, 2), -- 2
(3398.0, '2024-06-03 14:45:00', 0, 1, 1), -- 3
(5996.0, '2024-06-04 16:15:00', 1, 2, 2), -- 4
(2498.0, '2024-07-11 10:15:00', 0, 2, 1), -- 5
(5026.0, '2024-09-28 11:11:11', 0, 2, 1), -- 6
(4297.0, '2024-09-28 14:15:10', 1, 1, 2), -- 7
(5996.0, '2024-09-29 10:20:00', 0, 3, 1), -- 8
(5546.0, '2024-09-30 11:35:20', 0, 2, 1), -- 9
(1599.0, '2024-12-10 18:35:20', 0, 2, 1); -- 10


INSERT INTO DETALLE_VENTA (CANTIDAD_PLAYERAS, ID_PLAYERA, ID_VENTA) VALUES
(3, 1, 1),
(1, 3, 2),
(2, 5, 3),
(4, 4, 4),
(1, 3, 5),
(1, 13, 5),
(1, 7, 6),
(1, 10, 6),
(1, 11, 6),
(1, 12, 6),
(2, 1, 7),
(1, 5, 7),
(3, 7, 8),
(1, 3, 8),
(3, 2, 9),
(1, 9, 9),
(1, 15, 10);

INSERT INTO VENTA_CREDITO (MONTO_RESTANTE, PAGOS_REALIZADOS, ID_DEUDOR, ID_VENTA) VALUES
(799.0,1,1,2),
(5496.0,1,2,4),
(4297.0,0,4,6);

INSERT INTO PAGO_CREDITO (FECHA_PAGO, MONTO_PAGO, ID_VENTA_CREDITO) VALUES
('2024-06-02 11:30:20', 400.00, 1),
('2024-06-04 15:14:13', 500.00, 2);

-- *************** TRIGGER **********************

DROP TRIGGER IF EXISTS trg_estatus_update;
DELIMITER $$
CREATE TRIGGER trg_estatus_update
BEFORE UPDATE ON VENTA_CREDITO
FOR EACH ROW
BEGIN
    IF NEW.MONTO_RESTANTE = 0 THEN
        UPDATE VENTA SET ID_ESTATUS_VENTA = (SELECT ID_ESTATUS_VENTA FROM estatus_venta WHERE ESTATUS = 'PAGADO')
		  WHERE ID_VENTA = NEW.ID_VENTA;
    ELSE
        UPDATE VENTA SET ID_ESTATUS_VENTA = (SELECT ID_ESTATUS_VENTA FROM estatus_venta WHERE ESTATUS = 'PENDIENTE')
        WHERE ID_VENTA = NEW.ID_VENTA;
    END IF;
END$$
DELIMITER ;