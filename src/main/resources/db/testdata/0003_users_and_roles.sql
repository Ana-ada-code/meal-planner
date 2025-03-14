insert into users (email, password)
values ('admin@example.pl', '{noop}adminpass'),
       ('user@example.pl', '{noop}userpass'),
       ('editor@example.pl', '{noop}editorpass');

insert into user_role (name, description)
values ('ADMIN', 'pełne uprawnienia'),
       ('USER', 'podstawowe uprawnienia, możliwość oddawania głosów, dodawania posiłków do kalendarza'),
       ('EDITOR', 'podstawowe uprawnienia + możliwość zarządzania treściami');

insert into user_roles (user_id, role_id)
values (1, 1),
       (2, 2),
       (3, 3);