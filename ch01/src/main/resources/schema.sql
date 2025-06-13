DROP TABLE IF EXISTS product;

CREATE TABLE product (
  id CHARACTER(9) NOT NULL,
  name VARCHAR(50),
  description VARCHAR(255),
  price FLOAT,
  update_timestamp TIMESTAMP,
  CONSTRAINT product_pkey PRIMARY KEY (id)
);
