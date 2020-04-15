INSERT INTO GENRE (GENR_ID, GENR_NAME, GENR_DESCRIPTION)
VALUES (10001, 'Adventure', 'Exploring new places, environments or situations. This is often associated with people on long journeys to places far away encountering amazing things, usually not in an epic but in a rather gripping and interesting way.');

INSERT INTO GENRE_ANIDB (GEAN_ID, GEAN_GENR_ID, GEAN_ANIDB_CODE)
VALUES (10001, 10001, 'adventure');

INSERT INTO GENRE (GENR_ID, GENR_NAME, GENR_DESCRIPTION)
VALUES (10002, 'Space', 'Space powwaaa !');

INSERT INTO GENRE_ANIDB (GEAN_ID, GEAN_GENR_ID, GEAN_ANIDB_CODE)
VALUES (10002, 10002, 'space travel');

INSERT INTO GENRE_ANIDB (GEAN_ID, GEAN_GENR_ID, GEAN_ANIDB_CODE)
VALUES (10003, 10002, 'space opera');
