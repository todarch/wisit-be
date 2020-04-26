CREATE TABLE question_reactions (
  id SERIAL NOT NULL PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  question_id VARCHAR(36) NOT NULL,
  liked BOOLEAN NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (user_id, question_id)
);

ALTER TABLE question_reactions
    ADD CONSTRAINT question_reactions_user_id_fkey FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE question_reactions
    ADD CONSTRAINT question_reactions_question_id_fkey FOREIGN KEY (question_id) REFERENCES questions(id);
