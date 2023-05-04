INSERT INTO `country` (`name`)
VALUES ('France'),
       ('Allemagne'),
       ('Espagne');

INSERT INTO `job` (`name`)
VALUES ('Développeur'),
       ('Testeur'),
       ('Chef de projet');

INSERT INTO company (name)
VALUES ('Apple'),
       ('Facebook');

INSERT INTO role (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO user(email, password, firstname, lastname, id_country, id_company, created_at, updated_at)
VALUES ('user@user.com', 'user', 'Ash', 'KETCHUP', 2, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
       ('admin@admin.com', 'admin', 'Bat', 'MAN', 3, 2, UTC_TIMESTAMP(), UTC_TIMESTAMP()),
       ('test@test.com', '$2a$10$OsZ5IsBR44ox9NBtHkZCE.CqOrRk4WzkrOU8fcF2FdlZ8aBQbgMsS', 'Test', 'TEST', 1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO user_roles(user_id, roles_id)
VALUES (1, 1),
       (2, 1),
       (2, 2),
       (3, 1),
       (3, 2);

INSERT INTO user_jobs(user_id, job_id)
VALUES (1, 1),
       (1, 2);
