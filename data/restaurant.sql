-- IMPORTANT: DO NOT CHANGE THE GIVEN SCHEMA UNLESS YOU HAVE A GOOD REASON
-- IF YOU DO CHANGE IT WRITE THE JUSTIFICATION IN A COMMENT ABOVE THE CHANGE

drop database if exists restaurant;

create database restaurant;

use restaurant;

create table customers (
  username varchar(64) not null,
  password varchar(128) not null,

  -- made username the primary key so that (1) place_orders table can reference username as foreign key
  -- and (2) the username is unique -> 2 people can't have the same username
  CONSTRAINT PK_username PRIMARY KEY (username)
);

insert into customers(username, password) values
  ('fred', sha2('fred', 224)),
  ('barney', sha2('barney', 224)),
  ('wilma', sha2('wilma', 224)),
  ('betty', sha2('betty', 224)),
  ('pebbles', sha2('pebbles', 224));

-- TODO: Task 1.2
-- Write your task 1.2 below
CREATE TABLE place_orders (
	order_id CHAR(8) NOT NULL,
	payment_id varchar(128) NOT NULL,
	order_date date NOT NULL,
	total decimal(8, 2) NOT NULL,
	username varchar(128) NOT NULL,
	
	CONSTRAINT PK_order_id PRIMARY KEY (order_id),
	CONSTRAINT UC_payment_id UNIQUE (payment_id),
	CONSTRAINT FK_username FOREIGN KEY (username) REFERENCES customers(username),
	CONSTRAINT CHK_total CHECK (total <= 999999.99)
);
