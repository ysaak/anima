CREATE TABLE SEASON (
    SEAS_ID VARCHAR(30) NOT NULL PRIMARY KEY,
    SEAS_ELEM_ID VARCHAR(30) NOT NULL,
    SEAS_NUMBER SMALLINT UNSIGNED,
    SEAS_TITLE VARCHAR(250) NOT NULL
);

ALTER TABLE SEASON ADD CONSTRAINT FK_SEAS_ELEM FOREIGN KEY (SEAS_ELEM_ID) REFERENCES ELEMENT (ELEM_ID);

CREATE TABLE EPISODE (
    EPIS_ID VARCHAR(30) NOT NULL PRIMARY KEY,
    EPIS_SEAS_ID VARCHAR(30) NOT NULL,
    EPIS_NUMBER VARCHAR(5) NOT NULL,
    EPIS_TITLE VARCHAR(250) NOT NULL
);

ALTER TABLE EPISODE ADD CONSTRAINT FK_SEAS_EPIS FOREIGN KEY (EPIS_SEAS_ID) REFERENCES SEASON (SEAS_ID);
