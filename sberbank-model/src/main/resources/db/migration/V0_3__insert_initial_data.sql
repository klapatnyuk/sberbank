-- Insert initial data

INSERT INTO "user" (id, login, password, role) VALUES
    (1, 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'ADMIN'),
    (2, 'client', 'd2a04d71301a8915217dd5faf81d12cffd6cd958', 'CLIENT');

INSERT INTO template (id, title) VALUES
    (1, 'Контракт'),
    (2, 'Заявка');

INSERT INTO template_field (id, template_id, title, label, type, index) VALUES
    (1, 1, 'surname', 'Фамилия', 'LINE', 0),
    (2, 1, 'name', 'Имя', 'LINE', 1),
    (3, 1, 'description', 'Описание', 'AREA', 2),
    (4, 1, 'active', 'Активен', 'CHECKBOX', 3),

    (5, 2, 'name', 'Имя', 'LINE', 0),
    (6, 2, 'phone', 'Телефон', 'LINE', 1),
    (7, 2, 'comment', 'Комментарий', 'AREA', 2);

INSERT INTO document (id, template_id, title, owner_id) VALUES
    (1, 1, 'Контракт №1', 1),
    (2, 2, 'Запрос №1', 1),

    (3, 1, 'Контракт №2', 2),
    (4, 1, 'Контракт №3', 2),
    (5, 2, 'Запрос №2', 2);

INSERT INTO document_field (id, document_id, template_field_id, value) VALUES
    (1, 1, 1, 'Клапатнюк'),
    (2, 1, 2, 'Вячеслав'),
    (3, 1, 3, 'Законопослушный гражданин'),
    (4, 1, 4, 'Y'),

    (5, 2, 5, 'Вячеслав'),
    (6, 2, 6, '+7 (911) 032-36-30'),

    (7, 3, 1, 'Иванов'),
    (8, 3, 2, 'Иван'),
    (9, 3, 4, 'N'),

    (10, 4, 1, 'Иванов'),
    (11, 4, 2, 'Иван'),
    (12, 4, 3, 'Налогоплательщик'),
    (13, 4, 4, 'Y'),

    (14, 5, 5, 'Иван'),
    (15, 5, 6, '+7 (000) 000-00-00'),
    (16, 5, 7, 'Звонить после 18:00');

-- Rollback all actions

-- DELETE FROM document_field;
-- DELETE FROM document;
-- DELETE FROM template_field;
-- DELETE FROM template;
-- DELETE FROM "user";
