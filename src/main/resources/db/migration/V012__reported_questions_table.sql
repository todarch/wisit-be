CREATE TABLE reported_questions (
    id SERIAL NOT NULL PRIMARY KEY,
    question_id VARCHAR(36) NOT NULL REFERENCES questions(id),
    reporting_reason_id int NOT NULL REFERENCES reporting_reasons(id),
    detail VARCHAR(256) NOT NULL,
    resolved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE pictures
    ADD COLUMN active BOOLEAN DEFAULT TRUE;

ALTER TABLE questions
    ADD COLUMN active BOOLEAN DEFAULT TRUE;
