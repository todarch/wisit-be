ALTER TABLE asked_questions
    ADD COLUMN answered_in_seconds INT NOT NULL DEFAULT 0;

ALTER TABLE asked_questions
    ADD CONSTRAINT asked_questions_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE asked_questions
    ADD CONSTRAINT asked_questions_question_id_fkey FOREIGN KEY (question_id) REFERENCES questions(id);
