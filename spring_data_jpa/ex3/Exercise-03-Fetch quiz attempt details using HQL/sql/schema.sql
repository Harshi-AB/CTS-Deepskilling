-- ============================================================
-- schema.sql
-- Core MySQL schema + sample data for the quiz-attempt exercise
-- (Hands-on 3). Mirrors the schema described in the hands-on document
-- (quiz.mwb -> forward-engineered SQL), built here directly as plain SQL.
--
-- Run once:  mysql -u root -p < schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS ormlearn_quiz;
USE ormlearn_quiz;

DROP TABLE IF EXISTS attempt_option;
DROP TABLE IF EXISTS attempt_question;
DROP TABLE IF EXISTS attempt;
DROP TABLE IF EXISTS options;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS `user`;

-- ---------------------------------------------------------
-- user: master data (registered quiz takers)
-- ---------------------------------------------------------
CREATE TABLE `user` (
    user_id  INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL
);

-- ---------------------------------------------------------
-- question: master data
-- ---------------------------------------------------------
CREATE TABLE question (
    question_id   INT PRIMARY KEY AUTO_INCREMENT,
    question_text VARCHAR(500) NOT NULL
);

-- ---------------------------------------------------------
-- options: master data, score = points awarded if this option is chosen
-- ---------------------------------------------------------
CREATE TABLE options (
    option_id   INT PRIMARY KEY AUTO_INCREMENT,
    option_text VARCHAR(255) NOT NULL,
    score       DOUBLE NOT NULL DEFAULT 0,
    question_id INT NOT NULL,
    CONSTRAINT fk_options_question FOREIGN KEY (question_id) REFERENCES question(question_id)
);

-- ---------------------------------------------------------
-- attempt: one row per quiz attempt made by a user
-- ---------------------------------------------------------
CREATE TABLE attempt (
    attempt_id     INT PRIMARY KEY AUTO_INCREMENT,
    attempted_date DATETIME NOT NULL,
    user_id        INT NOT NULL,
    CONSTRAINT fk_attempt_user FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

-- ---------------------------------------------------------
-- attempt_question: which questions were part of a given attempt
-- ---------------------------------------------------------
CREATE TABLE attempt_question (
    attempt_question_id INT PRIMARY KEY AUTO_INCREMENT,
    attempt_id           INT NOT NULL,
    question_id          INT NOT NULL,
    CONSTRAINT fk_aq_attempt  FOREIGN KEY (attempt_id)  REFERENCES attempt(attempt_id),
    CONSTRAINT fk_aq_question FOREIGN KEY (question_id) REFERENCES question(question_id)
);

-- ---------------------------------------------------------
-- attempt_option: the option(s) the user actually selected
-- ---------------------------------------------------------
CREATE TABLE attempt_option (
    attempt_option_id    INT PRIMARY KEY AUTO_INCREMENT,
    attempt_question_id  INT NOT NULL,
    option_id            INT NOT NULL,
    CONSTRAINT fk_ao_attempt_question FOREIGN KEY (attempt_question_id) REFERENCES attempt_question(attempt_question_id),
    CONSTRAINT fk_ao_option           FOREIGN KEY (option_id)           REFERENCES options(option_id)
);

-- ============================================================
-- Sample data - reproduces the exact example walked through in the
-- hands-on document (4 HTML/JS quiz questions, one attempt by 'john_doe').
-- ============================================================

INSERT INTO `user` (user_id, username) VALUES (1, 'john_doe');

INSERT INTO question (question_id, question_text) VALUES
    (1, 'What is the extension of the hyper text markup language file?'),
    (2, 'What is the maximum level of heading tag can be used in a HTML page?'),
    (3, 'The HTML document itself begins with <html> and ends </html>. State True or False'),
    (4, 'Choose the right option to store text value in a variable');

-- Question 1 options
INSERT INTO options (option_id, option_text, score, question_id) VALUES
    (1, '.xhtm', 0.0, 1),
    (2, '.ht',   0.0, 1),
    (3, '.html', 1.0, 1),
    (4, '.htmx', 0.0, 1);

-- Question 2 options
INSERT INTO options (option_id, option_text, score, question_id) VALUES
    (5, '5', 0.0, 2),
    (6, '3', 0.0, 2),
    (7, '4', 0.0, 2),
    (8, '6', 1.0, 2);

-- Question 3 options
INSERT INTO options (option_id, option_text, score, question_id) VALUES
    (9,  'false', 0.0, 3),
    (10, 'true',  1.0, 3);

-- Question 4 options
INSERT INTO options (option_id, option_text, score, question_id) VALUES
    (11, '''John''', 0.5, 4),
    (12, 'John',     0.0, 4),
    (13, '"John"',   0.5, 4),
    (14, '/John/',   0.0, 4);

-- One attempt by john_doe (attempt_id = 1) covering all 4 questions
INSERT INTO attempt (attempt_id, attempted_date, user_id) VALUES
    (1, '2026-06-01 10:15:00', 1);

INSERT INTO attempt_question (attempt_question_id, attempt_id, question_id) VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 1, 3),
    (4, 1, 4);

-- Options actually selected by john_doe for this attempt:
--   Q1 -> option 3 (.html)   correct
--   Q2 -> option 6 (3)       incorrect (correct was option 8 = '6')
--   Q3 -> option 10 (true)   correct
--   Q4 -> option 11 ('John') partially correct (score 0.5)
INSERT INTO attempt_option (attempt_option_id, attempt_question_id, option_id) VALUES
    (1, 1, 3),
    (2, 2, 6),
    (3, 3, 10),
    (4, 4, 11);
