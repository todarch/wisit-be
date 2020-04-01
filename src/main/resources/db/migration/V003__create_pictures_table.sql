CREATE TABLE pictures (
    id SERIAL NOT NULL,
    url VARCHAR(400) NOT NULL UNIQUE,
    city_id smallint NOT NULL
);

INSERT INTO pictures (url, city_id)
VALUES ('https://farm9.staticflickr.com/8667/15491990730_83ce51863a_b.jpg', (select id from wisit.cities where name = 'Istanbul'));
