/* Пароль: Iskander2001*/
INSERT INTO persons ( username, added_date, "password")
VALUES
('Iskander', now(), '$2a$10$j0um2TY3iymtEzJzobI04umDLklfPtBXEBszbtIia60.4wJ43r5ZG')

INSERT INTO roles (role)
VALUES
('ROLE_ADMIN'),
('ROLE_USER')

INSERT INTO coffee_orders
(coffee_order_id,drink_name,date,person_id)
VALUES
(1,'Coffee with milk', now(), 1);

INSERT INTO person_role
(person_id, role_id)
VALUES
(1,1),
(1,2)