
ALTER TABLE users
    ADD COLUMN username varchar(100) UNIQUE;

UPDATE users
SET username = COALESCE(id);

ALTER TABLE users
ALTER COLUMN username SET NOT NULL;
