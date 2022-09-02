CREATE TABLE Persons
(
person_id serial PRIMARY KEY,
username varchar(100) NOT NULL,
added_date timestamp NOT NULL,
password varchar NOT NULL
);

CREATE TABLE coffee_orders
(
	coffee_order_id serial PRIMARY KEY,
	drink_name varchar(50) NOT NULL,
	date timestamp NOT NULL,
	person_id int NOT NULL REFERENCES persons (person_id)
);

CREATE TABLE Roles
(
	role_id serial PRIMARY KEY,
	role varchar NOT NULL UNIQUE
);

CREATE TABLE Person_role
(
	person_id int NOT NULL REFERENCES persons (person_id)
	ON DELETE CASCADE
	ON UPDATE CASCADE,
	role_id int NOT NULL REFERENCES roles(role_id)
	ON DELETE CASCADE
	ON UPDATE CASCADE
);