CREATE TABLE ELEMENT_REMOTE_ID (
    ELRI_ID VARCHAR(30) NOT NULL,
    ELRI_ELEM_ID VARCHAR(30) NOT NULL,
    ELRI_EXSI_ID VARCHAR(30) NOT NULL,
    ELRI_REMOTE_ID VARCHAR(50) NOT NULL
);

ALTER TABLE ELEMENT_REMOTE_ID ADD PRIMARY KEY (ELRI_ID);