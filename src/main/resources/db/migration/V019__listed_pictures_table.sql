CREATE TABLE listed_pictures (
    id SERIAL NOT NULL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    picture_id int NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, picture_id)
);

ALTER TABLE listed_pictures
    ADD CONSTRAINT listed_pictures_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE listed_pictures
    ADD CONSTRAINT listed_pictures_picture_id_fkey FOREIGN KEY (picture_id) REFERENCES pictures(id);
