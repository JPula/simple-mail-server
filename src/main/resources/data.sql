ALTER SEQUENCE mail_user_seq INCREMENT BY 1 NO MAXVALUE;

INSERT INTO mail_user(id, user_name, first_name, last_name, email_address, is_deleted) VALUES (1, 'betty.boop', 'Betty', 'Boop', 'betty.boop@email.com', FALSE);
INSERT INTO mail_user(id, user_name, first_name, last_name, email_address, is_deleted) VALUES (2, 'bugs.bunny', 'Bugs', 'Bunny', 'bugs.bunny@email.com', FALSE);
INSERT INTO mail_user(id, user_name, first_name, last_name, email_address, is_deleted) VALUES (3, 'charlie.brown', 'Charlie', 'Brown', 'charlie.brown@email.com', FALSE);

ALTER SEQUENCE mail_user_seq RESTART WITH 4;

ALTER SEQUENCE email_seq INCREMENT BY 1 NO MAXVALUE;

INSERT INTO email(id, user_id, recipient, sender, subject, text) VALUES (1, 1, 'betty.boop@email.com', 'no-reply@mailserver.com', 'Welcome betty.boop!', 'Thanks for Subscribing!');
INSERT INTO email(id, user_id, recipient, sender, subject, text) VALUES (2, 2, 'bugs.bunny@email.com', 'no-reply@mailserver.com', 'Welcome bugs.bunny!', 'Thanks for Subscribing!');
INSERT INTO email(id, user_id, recipient, sender, subject, text) VALUES (3, 3, 'charlie.brown@email.com', 'no-reply@mailserver.com', 'Welcome charlie.brown!', 'Thanks for Subscribing!');

ALTER SEQUENCE email_seq RESTART WITH 4;
