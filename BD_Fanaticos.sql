--
-- Author :     Rodrigo Martínez Zambrano
-- Proyecto:    Fanáticos FC. Gestión de ventas de playeras de fútbol.
--

-- DROP DATABASE IF EXISTS FANATICOS_DB;
-- CREATE DATABASE FANATICOS_DB;
   USE FANATICOS_DB;

--
-- TABLE: MARCAS
-- Se guardarán las marcas de los fabricantes de las playeras.
--

CREATE TABLE MARCA(
    ID_MARCA    INT              NOT NULL   AUTO_INCREMENT,
    NOMBRE      VARCHAR(40)      NOT NULL,
    -- LOGO        BLOB             NOT NULL   DEFAULT "Sin Logo",

    PRIMARY KEY (ID_MARCA),
    CONSTRAINT UK_NOMBRE UNIQUE (NOMBRE)
);


--
-- TABLE: DEUDORES
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
-- TABLE: EQUIPOS
-- Se guardarán los datos de los equipos.
--

CREATE TABLE EQUIPO(
    ID_EQUIPO    INT             NOT NULL   AUTO_INCREMENT,
    NOMBRE       VARCHAR(40)     NOT NULL,
    PAIS         VARCHAR(40)     NOT NULL,
    LIGA        VARCHAR(40)      NOT NULL,
    -- ESCUDO       BLOB            NOT NULL   DEFAULT "Sin escudo",
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
    CONTRASEÑA  			VARCHAR(15)     NOT NULL,
    -- ES_ADMIN            BIT         NOT NULL CHECK (ES_ADMIN = 1 OR ES_ADMIN = 0),
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
    CANTIDAD_PLAYERAS    INT     NOT NULL      CHECK (CANTIDAD_PLAYERAS > 0),
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
  ('José', 'Pérez', 'Sánchez', '5554321098', 'joseperez@protonmail.com'),
  ('Laura', 'Ramírez', 'Torres', '5543210987', 'lauraramirez@icloud.com'),
  ('Miguel', 'Torres', 'Ruiz', '5532109876', 'migueltorres@mail.com'),
  ('Sofía', 'Flores', 'Vargas', '5521098765', 'sofia_flores83@correo.com'),
  ('David', 'Vargas', 'Jiménez', '5510987654', 'david.vargas@empresa.mx'),
  ('Carmen', 'Jiménez', 'Moreno', '5587654321', 'carmen.jimenez@trabajo.com');


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
('River Plate', 'Argentina', 'Primera División');



INSERT INTO PLAYERA (COLOR, TALLA, TIPO_MANGA, PRECIO_REAL, STOCK, PRECIO_VENTA, ID_MARCA, ID_EQUIPO) VALUES
('Azul', 'CH', 'CORTA', 800.0, 10, 1299.0, 6, 1), -- Prima, Cruz Azul
('Azul', 'M', 'CORTA', 800.0, 10, 1299.0, 6, 1),
('Blanco', 'CH', 'CORTA', 700.0, 15, 1199.0, 1, 2),  -- Nike, Pumas
('Vino', 'XG', 'CORTA', 500.00, 20, 1499.00, 1, 3), -- Nike, Barcelona
('Blanco', 'M', 'LARGA', 900.00, 25, 1699.0, 2, 4),   -- Adidas, Real Madrid
('Blanco', 'G', 'LARGA', 900.00, 25, 1699.0, 2, 4),
('Rojo', 'CH', 'LARGA', 999.0, 12, 1599.0,2,5),  -- Adidas, Man Uni
('Rojo', 'M', 'CORTA', 999.0, 9, 1499.0,1,6),  -- Nike, Liverpool
('Negro','G','CORTA',1099.0,18,1649.0,2,7),  -- Adidas, Bayern Mun
('Amarillo','XG','CORTA',1099.0,5,1349.0,3,8),  -- Puma, Borussia
('Azul','CH','CORTA',450.0,20,989.0,2,9), -- Adidas, Boca J
('Blanco', 'M', 'LARGA', 650.0, 13, 1099.0, 2, 10); -- Adidas, River

INSERT INTO USUARIO (NOMBRE, APELLIDO_PAT, APELLIDO_MAT, FECHA_NAC, RFC, USERNAME, CONTRASEÑA, MAIL, ROL, VENTAS_REALIZADAS) VALUES
('Ana', 'García', 'López', '1990-01-01', 'GALA9001017D5', 'ana_garcia', 'contrasena123', 'ana_garcia@correo.com', "ADMIN", 3),  -- ADMIN
('Juan', 'Pérez', 'Martínez', '1985-07-15', 'PEMJ8507159O4', 'juan_perez', 'contrasena456', 'juan_perez@correo.com', "USER", 2),
('María', 'Gómez', 'Hernández', '2000-12-24', 'GOHM001224R2A', 'maria_gomez', 'contrasena789', 'maria_gomez@correo.com', "USER", 2);


INSERT INTO ESTATUS_VENTA(ESTATUS,DESCRIPCION) VALUES
('PAGADO','LA VENTA SE PAGÓ POR COMPLETO'),
('PENDIENTE', 'LA VENTA SE ENCUENTRA EN PROCESO DE PAGO'),
('CANCELADA', 'LA VENTA FUE CANCELADA');

INSERT INTO VENTA (MONTO_TOTAL, FECHA_VENTA, ES_VENTA_CREDITO,ID_USUARIO,ID_ESTATUS_VENTA) VALUES
(3897.0, '2024-06-01 10:00:00', 0, 1,1),
(1199.0, '2024-06-02 11:30:00', 1, 3,2),
(3398.0, '2024-06-03 14:45:00', 0, 1,1),
(5996.0, '2024-06-04 16:15:00', 1, 2,2),
(5026.0,'2024-09-28 11:11:11',0,2,1),
(4297.0,'2024-09-28 14:15:10',1,1,2),
(5996.0,'2024-09-29 10:20:00',0,3,1),
(5546.0,'2024-09-30 11:35:20',0,3,1);


INSERT INTO DETALLE_VENTA (CANTIDAD_PLAYERAS, ID_PLAYERA, ID_VENTA) VALUES
(3, 1, 1),
(1, 3, 2),
(2, 5, 3),
(4, 4, 4),
(1, 7, 5),
(1, 10, 5),
(1, 11, 5),
(1, 12, 5),
(2, 1, 6),
(1, 5, 6),
(3, 7, 7),
(1, 3, 7),
(3, 2, 8),
(1, 9, 8);

INSERT INTO VENTA_CREDITO (MONTO_RESTANTE, PAGOS_REALIZADOS, ID_DEUDOR, ID_VENTA) VALUES
(799.0,1,1,2),
(5496.0,1,2,4),
(4297.0,0,4,6);

INSERT INTO PAGO_CREDITO (FECHA_PAGO, MONTO_PAGO, ID_VENTA_CREDITO) VALUES
('2024-06-02 11:30:20', 400.00, 1),
('2024-06-04 15:14:13', 500.00, 2);