ALTER TABLE asked_questions
    ADD COLUMN score_delta SMALLINT;

UPDATE asked_questions SET score_delta = 10 WHERE knew = true;
UPDATE asked_questions SET score_delta = -5 WHERE knew = false;

ALTER TABLE asked_questions
    ALTER COLUMN score_delta SET NOT NULL;

