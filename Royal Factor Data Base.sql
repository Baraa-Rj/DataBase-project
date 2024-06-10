USE royal;


CREATE TABLE supplier
(
    supplier_id      INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name    VARCHAR(50) NOT NULL,
    supplier_address VARCHAR(50) NOT NULL,
    supplier_email   VARCHAR(50) NOT NULL
);

CREATE TABLE customer
(
    customer_id      INT AUTO_INCREMENT PRIMARY KEY,
    customer_name    VARCHAR(50) NOT NULL,
    customer_email   VARCHAR(50) NOT NULL,
    customer_address VARCHAR(50) NOT NULL
);

CREATE TABLE employee
(
    employee_id       INT AUTO_INCREMENT PRIMARY KEY,
    employee_name     VARCHAR(50) NOT NULL,
    employee_email    VARCHAR(50) NOT NULL,
    employee_address  VARCHAR(50) NOT NULL,
    employee_position VARCHAR(50) NOT NULL,
    storage_id        INT,
    FOREIGN KEY (storage_id) REFERENCES storage (storage_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE storage
(
    storage_id       INT AUTO_INCREMENT PRIMARY KEY,
    storage_name     VARCHAR(50) NOT NULL,
    storage_location VARCHAR(50) NOT NULL,
    storage_capacity INT         NOT NULL
);

CREATE TABLE equipment
(
    equipment_id   INT AUTO_INCREMENT PRIMARY KEY,
    equipment_name VARCHAR(50) NOT NULL,
    cost           INT         NOT NULL,
    storage_id     INT,
    FOREIGN KEY (storage_id) REFERENCES storage (storage_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE product
(
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    cost         INT         NOT NULL,
    storage_id   INT,
    FOREIGN KEY (storage_id) REFERENCES storage (storage_id)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE supplier_equipment
(
    supplier_id  INT NOT NULL,
    equipment_id INT NOT NULL,
    PRIMARY KEY (supplier_id, equipment_id),
    FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (equipment_id) REFERENCES equipment (equipment_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE customer_product
(
    customer_id INT NOT NULL,
    product_id  INT NOT NULL,
    PRIMARY KEY (customer_id, product_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE product_employee
(
    product_id  INT NOT NULL,
    employee_id INT NOT NULL,
    PRIMARY KEY (product_id, employee_id),
    FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE supplier_phone
(
    phone_id     INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id  INT         NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    phone_type   VARCHAR(20),
    FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE customer_phone
(
    phone_id     INT AUTO_INCREMENT PRIMARY KEY,
    customer_id  INT         NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    phone_type   VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE employee_phone
(
    phone_id     INT AUTO_INCREMENT PRIMARY KEY,
    employee_id  INT         NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    phone_type   VARCHAR(20),
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO storage (storage_name, storage_location, storage_capacity)
VALUES ('Main Warehouse', '101 Warehouse Lane', 10000),
       ('Secondary Storage', '102 Storage Parkway', 5000);
INSERT INTO supplier (supplier_name, supplier_address, supplier_email)
VALUES ('Acme Supplies', '123 Acme Way', 'contact@acmesupplies.com'),
       ('Global Parts', '456 Global Ave', 'info@globalparts.com');

INSERT INTO equipment (equipment_name, cost, storage_id)
VALUES ('Hydraulic Press', 5000, 1),
       ('CNC Machine', 25000, 1);

INSERT INTO customer (customer_name, customer_email, customer_address)
VALUES ('John Doe', 'john.doe@example.com', '789 Elm St'),
       ('Jane Smith', 'jane.smith@example.com', '456 Maple St');

INSERT INTO employee (employee_name, employee_email, employee_address, employee_position)
VALUES ('Alice Johnson', 'alice.johnson@example.com', '123 Birch Rd', 'Manager'),
       ('Bob Williams', 'bob.williams@example.com', '234 Cedar Rd', 'Technician');

INSERT INTO product (product_name, cost, storage_id)
VALUES ('Widget', 15, 1),
       ('Gadget', 25, 1);
INSERT INTO supplier_equipment (supplier_id, equipment_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO customer_product (customer_id, product_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO product_employee (product_id, employee_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO supplier_phone (supplier_id, phone_number, phone_type)
VALUES (1, '800-123-4567', 'Work'),
       (2, '800-234-5678', 'Work');
INSERT INTO customer_phone (customer_id, phone_number, phone_type)
VALUES (1, '555-678-1234', 'Mobile'),
       (2, '555-789-2345', 'Mobile');
INSERT INTO employee_phone (employee_id, phone_number, phone_type)
VALUES (1, '555-123-4567', 'Mobile'),
       (2, '555-234-5678', 'Mobile');

create table customer_employee
(
    customer_id INT NOT NULL,
    employee_id INT NOT NULL,
    PRIMARY KEY (customer_id, employee_id),
    FOREIGN KEY (customer_id) REFERENCES customer (customer_id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee (employee_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

alter table employee add column salary INT NOT NULL;
select * from employee;
alter table product add column quantity decimal(10,2);
alter table equipment
    add column quantity int;
    select * from supplier;
select * from supplier_phone;
alter table employee add column salary int NOT NULL;
alter table supplier_phone drop column phone_type;
alter table employee_phone drop column phone_type;
alter table customer_phone drop column phone_type;
DELETE FROM supplier WHERE supplier_id = 8;
alter table employee add column employee_password VARCHAR(50) NOT NULL;
alter table employee drop column employee_password;
alter table employee add column employee_gender VARCHAR(20) NOT NULL;
select * from employee;
ALTER TABLE employee MODIFY COLUMN salary DECIMAL(10,2) DEFAULT 1000;


DELETE FROM supplier;
DELETE FROM customer;
alter table employee add column employee_password VARCHAR(50) NOT NULL;
alter table employee drop column employee_password;
alter table employee add column employee_gender VARCHAR(20) NOT NULL;
select * from employee;
select * from employee_phone;
alter table employee
    drop column storage_id;
    alter table employee
    drop foreign key employee_ibfk_1;