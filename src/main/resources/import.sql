INSERT INTO books (authors, created_at, isbn, copies, publisher, synopsis, title, `year`) VALUES('J.K. Rowling', '2023-03-29 12:00:00', '9788831003384', 10, 'Salani', 'Harry Potter first volume', 'Harry Potter and the Sorcerer stone', 1999);
INSERT INTO books (authors, created_at, isbn, copies, publisher, synopsis, title, `year`) VALUES('J.K. Rowling', '2023-03-29 12:00:00', '9788831003385', 10, 'Salani', 'Harry Potter second volume', 'Harry Potter and the chamber of secrets', 2000);
INSERT INTO books (authors, created_at, isbn, copies, publisher, synopsis, title, `year`) VALUES('J.K. Rowling', '2023-03-29 12:00:00', '9788831003386', 10, 'Salani', 'Harry Potter third volume', 'Harry Potter and the Azkaban prisoner', 2001);
INSERT INTO books (authors, created_at, isbn, copies, publisher, synopsis, title, `year`) VALUES('Frank Herbert', '2023-03-30 10:45:00', '1088831003386', 3, 'Einaudi', null, 'Dune', 1969);

INSERT INTO borrowings (borrowing_date, expire_date, return_date, book_id) VALUES('2023-01-01', '2023-02-01', '2023-01-15', 1);
INSERT INTO borrowings (borrowing_date, expire_date, return_date, book_id) VALUES('2023-03-01', '2023-04-01', null, 1);
INSERT INTO borrowings (borrowing_date, expire_date, return_date, book_id) VALUES('2023-01-01', '2023-02-01', '2023-01-15', 2);
INSERT INTO borrowings (borrowing_date, expire_date, return_date, book_id) VALUES('2023-01-01', '2023-02-01', '2023-01-15', 3);

INSERT INTO categories (description, name) VALUES('Novel', 'Novel');
INSERT INTO categories (description, name) VALUES('Fantasy', 'Fantasy');
INSERT INTO categories (description, name) VALUES('Coding', 'Coding');
INSERT INTO categories (description, name) VALUES('Age 11-18', 'Teen');

INSERT INTO book_category (book_id, category_id) VALUES(1, 1);
INSERT INTO book_category (book_id, category_id) VALUES(1, 2);

INSERT INTO users (email, first_name, last_name, password) VALUES('john@email.it', 'John', 'Doe', '{noop}john');
INSERT INTO users (email, first_name, last_name, password) VALUES('jane@email.it', 'Jane', 'Smith','{noop}jane');

INSERT INTO roles (id, name) VALUES(1, 'ADMIN');
INSERT INTO roles (id, name) VALUES(2, 'USER');

INSERT into users_roles(user_id, roles_id) VALUES(1, 1);
INSERT into users_roles(user_id, roles_id) VALUES(2, 2);
