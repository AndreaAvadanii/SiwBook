/*-- ===================
-- AUTORI
-- ===================
insert into author (id, name, surname, birthdate, deathdate, nationality, url_image) 
values (nextval('author_id_seq'),'J.K.','Rowling','1965-07-31',null,'British','/images/authors/rowling.jpg');

insert into author (id, name, surname, birthdate, deathdate, nationality, url_image) 
values (nextval('author_id_seq'),'J.R.R.','Tolkien','1892-01-03','1973-09-02','British','/images/authors/tolkien.jpg');

insert into author (id, name, surname, birthdate, deathdate, nationality, url_image) 
values (nextval('author_id_seq'),'George','Orwell','1903-06-25','1950-01-21','British','/images/authors/orwell.jpg');

insert into author (id, name, surname, birthdate, deathdate, nationality, url_image) 
values (nextval('author_id_seq'),'Jane','Austen','1775-12-16','1817-07-18','British','/images/authors/austen.jpg');

insert into author (id, name, surname, birthdate, deathdate, nationality, url_image) 
values (nextval('author_id_seq'),'Dan','Brown','1964-06-22',null,'American','/images/authors/brown.jpg');

-- ===================
-- LIBRI
-- ===================
insert into book (id, title, year, url_image) 
values (nextval('book_id_seq'),'Harry Potter and the Philosopher''s Stone',1997,'/images/books/hp1.jpg');

insert into book (id, title, year, url_image) 
values (nextval('book_id_seq'),'Harry Potter and the Chamber of Secrets',1998,'/images/books/hp2.jpg');

insert into book (id, title, year, url_image) 
values (nextval('book_id_seq'),'The Hobbit',1937,'/images/books/hobbit.jpg');

insert into book (id, title, year, url_image) 
values (nextval('book_id_seq'),'1984',1949,'/images/books/1984.jpg');

insert into book (id, title, year, url_image) 
values (nextval('book_id_seq'),'Pride and Prejudice',1813,'/images/books/pride.jpg');

insert into book (id, title, year, url_image) 
values (nextval('book_id_seq'),'The Da Vinci Code',2003,'/images/books/dvc.jpg');



insert into book_author (book_id, author_id) values (1, 1);
insert into book_author (book_id, author_id) values (2, 1);
insert into book_author (book_id, author_id) values (3, 2);
insert into book_author (book_id, author_id) values (4, 3);
insert into book_author (book_id, author_id) values (5, 4);
insert into book_author (book_id, author_id) values (6, 5);*/
