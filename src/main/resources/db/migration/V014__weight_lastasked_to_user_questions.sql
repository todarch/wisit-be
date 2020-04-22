
ALTER TABLE user_questions
    ADD COLUMN weight INT NOT NULL DEFAULT 11;

ALTER TABLE user_questions
    ADD COLUMN last_asked_at TIMESTAMP WITH TIME ZONE;
