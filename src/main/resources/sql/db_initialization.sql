CREATE TABLE author (
	id SERIAL NOT NULL,
	name varchar NOT NULL,
	dateOfBirth date NOT NULL,
	dateOfDeath date,
	genderId int4 NOT NULL,
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
	yearOfPublication int4 NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE book_author (
	id SERIAL NOT NULL,
	bookId int4 NOT NULL,
	authorId int4 NOT NULL
);

CREATE TABLE publisher (
	id SERIAL NOT NULL,
	name varchar NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE publisher_book (
	id SERIAL NOT NULL,
	publisherId int4 NOT NULL,
	bookId int4 NOT NULL
);

CREATE TABLE dataset (
	id SERIAL NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE datasetsAuthors (
	id SERIAL NOT NULL,
	datasetId int4 NOT NULL,
	authorId int4 NOT NULL
);

CREATE TABLE datasetsBooks (
	id SERIAL NOT NULL,
	datasetId int4 NOT NULL,
	bookId int4 NOT NULL
);

CREATE TABLE datasetsPublishers (
	id SERIAL NOT NULL,
	datasetId int4 NOT NULL,
	publisherId int4 NOT NULL
);


ALTER TABLE author
    ADD CONSTRAINT author_uq
    UNIQUE (name, dateOfBirth, genderId);

ALTER TABLE book
    ADD CONSTRAINT book_uq
    UNIQUE (name, yearOfPublication);

ALTER TABLE publisher
    ADD CONSTRAINT publisher_uq
    UNIQUE (name);

ALTER TABLE book_author
    ADD CONSTRAINT book_author_uq
    UNIQUE (bookId, authorId);

ALTER TABLE publisher_book
    ADD CONSTRAINT publisher_book_uq
    UNIQUE (publisherId, bookId);

ALTER TABLE author ADD CONSTRAINT Ref_Author_to_Gender FOREIGN KEY (genderId)
	REFERENCES gender(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE book_author ADD CONSTRAINT Ref_Book_has_Author_to_Book FOREIGN KEY (bookId)
	REFERENCES book(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE book_author ADD CONSTRAINT Ref_Book_has_Author_to_Author FOREIGN KEY (authorId)
	REFERENCES author(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE publisher_book ADD CONSTRAINT Ref_Publisher_has_Book_to_Publisher FOREIGN KEY (publisherId)
	REFERENCES publisher(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;

ALTER TABLE publisher_book ADD CONSTRAINT Ref_Publisher_has_Book_to_Book FOREIGN KEY (bookId)
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

ALTER TABLE datasetsAuthors ADD CONSTRAINT Ref_Dataset_has_Author_to_Author FOREIGN KEY (authorId)
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

ALTER TABLE datasetsBooks ADD CONSTRAINT Ref_Dataset_has_Book_to_Book FOREIGN KEY (bookId)
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

ALTER TABLE datasetsPublishers ADD CONSTRAINT Ref_Dataset_has_Publisher_to_Publisher FOREIGN KEY (publisherId)
	REFERENCES publisher(id)
	MATCH SIMPLE
	ON DELETE NO ACTION
	ON UPDATE NO ACTION
	NOT DEFERRABLE;


INSERT INTO gender (id, name)
    VALUES (1, 'MALE'), (2, 'FEMALE');