insert into
    users (email, password)
values
    ('admin@example.pl', '{noop}adminpass'),   -- 1
    ('user@example.pl', '{noop}userpass'),     -- 2
    ('editor@example.pl', '{noop}editorpass'); -- 3

insert into
    user_role (name, description)
values
    ('ADMIN', 'pełne uprawnienia'),   -- 1
    ('USER', 'podstawowe uprawnienia, możliwość oddawania głosów, dodawania posiłków do kalendarza'),   -- 2
    ('EDITOR', 'podstawowe uprawnienia + możliwość zarządzania treściami');   -- 3

insert into
    user_roles (user_id, role_id)
values
    (1, 1),
    (2, 2),
    (3, 3);