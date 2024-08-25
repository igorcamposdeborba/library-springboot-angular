CREATE DATABASE library;
USE library;

CREATE TABLE library (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255)
);

CREATE TABLE book (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  year_publication DATE,
  price DOUBLE NOT NULL,
  library_id INT,
  FOREIGN KEY (library_id) REFERENCES library(id)
);

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  library_id INT,
  FOREIGN KEY (library_id) REFERENCES library(id)
);

CREATE TABLE associate (
  id VARCHAR(255) PRIMARY KEY,  -- Adjust length if necessary
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE,  -- Assuming email is unique for associates
  active BOOLEAN NOT NULL DEFAULT true,
  department VARCHAR(255),
  specialty VARCHAR(255)
);

CREATE TABLE student (
  id VARCHAR(255) PRIMARY KEY,  -- Adjust length if necessary
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE,  -- Assuming email is unique for students
  active BOOLEAN NOT NULL DEFAULT true,
  pending_penalties_amount DECIMAL(10,2) DEFAULT 0.00,  -- Allows for 2 decimal places
  course_name VARCHAR(255)
);


CREATE TABLE loan (
  user_id INT NOT NULL,
  book_id INT NOT NULL,
  effective_from DATE NOT NULL,
  effective_to DATE,
  is_delivered BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (user_id, book_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (book_id) REFERENCES book(id)
);

SELECT * FROM library;
SELECT * FROM book;
SELECT * FROM loan;
SELECT * FROM users;
SELECT * FROM student;
SELECT * FROM associate;


