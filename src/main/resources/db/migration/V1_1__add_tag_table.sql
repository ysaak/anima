CREATE TABLE TAG (
    TAG_ID VARCHAR(30) NOT NULL,
    TAG_NAME VARCHAR(250) NOT NULL,
    TAG_DESCRIPTION TEXT
);

ALTER TABLE TAG ADD PRIMARY KEY (TAG_ID);

CREATE TABLE TAG_EQUIVALENCE (
    TAEQ_ID VARCHAR(30) NOT NULL,
    TAEQ_TAG_ID VARCHAR(30) NOT NULL,
    TAEQ_ORIGIN VARCHAR(20) NOT NULL,
    TAEQ_EQUIVALENCE VARCHAR(50) NOT NULL
);

ALTER TABLE TAG_EQUIVALENCE ADD PRIMARY KEY (TAEQ_ID);
ALTER TABLE TAG_EQUIVALENCE ADD CONSTRAINT FK_TAEQ_TAG FOREIGN KEY (TAEQ_TAG_ID) REFERENCES TAG (TAG_ID);