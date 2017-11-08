CREATE TABLE author (
	id SERIAL NOT NULL,
	name varchar NOT NULL,
	date_of_birth date NOT NULL,
	date_of_death date,
	gender_id int4 NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE gender (
	id SERIAL NOT NULL,
	name varchar NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE book (
	id SERIAL NOT NULL,
	name varchar NOT NULL,
	year_of_publication int4 NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE book_author (
	id SERIAL NOT NULL,
	book_id int4 NOT NULL,
	author_id int4 NOT NULL
);

CREATE TABLE publisher (
	id SERIAL NOT NULL,
	name varchar NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE publisher_book (
	id SERIAL NOT NULL,
	publisher_id int4 NOT NULL,
	book_id int4 NOT NULL
);

CREATE TABLE dataset (
	id SERIAL NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE datasetsAuthors (
	id SERIAL NOT NULL,
	datasetId int4 NOT NULL,
	author_id int4 NOT NULL
);

CREATE TABLE datasetsBooks (
	id SERIAL NOT NULL,
	datasetId int4 NOT NULL,
	book_id int4 NOT NULL
);

CREATE TABLE datasetsPublishers (
	id SERIAL NOT NULL,
	datasetId int4 NOT NULL,
	publisher_id int4 NOT NULL
);


ALTER TABLE author
    ADD CONSTRAINT author_uq
    UNIQUE (name, date_of_birth, gender_id);

ALTER TABLE book
    ADD CONSTRAINT book_uq
    UNIQUE (name, year_of_publication);

ALTER TABLE publisher
    ADD CONSTRAINT publisher_uq
    UNIQUE (name);

ALTER TABLE book_author
    ADD CONSTRAINT book_author_uq
    UNIQUE (book_id, author_id);

ALTER TABLE publisher_book
    ADD CONSTRAINT publisher_book_uq
    UNIQUE (publisher_id, book_id);

ALTER TABLE author ADD CONSTRAINT Ref_Author_to_Gender FOREIGN KEY (gender_id)
	REFERENCES gender(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE book_author ADD CONSTRAINT Ref_Book_has_Author_to_Book FOREIGN KEY (book_id)
	REFERENCES book(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE book_author ADD CONSTRAINT Ref_Book_has_Author_to_Author FOREIGN KEY (author_id)
	REFERENCES author(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE publisher_book ADD CONSTRAINT Ref_Publisher_has_Book_to_Publisher FOREIGN KEY (publisher_id)
	REFERENCES publisher(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE publisher_book ADD CONSTRAINT Ref_Publisher_has_Book_to_Book FOREIGN KEY (book_id)
	REFERENCES book(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE datasetsAuthors ADD CONSTRAINT Ref_Dataset_has_Author_to_Dataset FOREIGN KEY (datasetId)
	REFERENCES dataset(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE datasetsAuthors ADD CONSTRAINT Ref_Dataset_has_Author_to_Author FOREIGN KEY (author_id)
	REFERENCES author(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE datasetsBooks ADD CONSTRAINT Ref_Dataset_has_Book_to_Dataset FOREIGN KEY (datasetId)
	REFERENCES dataset(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE datasetsBooks ADD CONSTRAINT Ref_Dataset_has_Book_to_Book FOREIGN KEY (book_id)
	REFERENCES book(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE datasetsPublishers ADD CONSTRAINT Ref_Dataset_has_Publisher_to_Dataset FOREIGN KEY (datasetId)
	REFERENCES dataset(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE datasetsPublishers ADD CONSTRAINT Ref_Dataset_has_Publisher_to_Publisher FOREIGN KEY (publisher_id)
	REFERENCES publisher(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;


INSERT INTO gender (id, name)
    VALUES (1, 'MALE'), (2, 'FEMALE');