-- Insert initial data

INSERT INTO "user" (id, login, password, role) VALUES
    (1, 'admin', 'admin', 'ADMIN'),
    (2, 'client', 'client', 'CLIENT');

INSERT INTO template (id, title) VALUES
    (1, 'Contract'),
    (2, 'Request');

INSERT INTO template_field (id, template_id, title, label, type, "order") VALUES
    (1, 1, 'surname', 'Surname', 'LINE', 0),
    (2, 1, 'name', 'Name', 'LINE', 1),
    (3, 1, 'description', 'Description', 'AREA', 2),
    (4, 1, 'active', 'Active', 'CHECKBOX', 3),

    (5, 2, 'name', 'Name', 'LINE', 0),
    (6, 2, 'phone', 'Phone', 'LINE', 1),
    (7, 2, 'comment', 'Comments', 'AREA', 2);

INSERT INTO document (id, template_id, title, owner_id) VALUES
    (1, 1, 'Contract 1', 1),
    (2, 2, 'Request 1', 1),

    (3, 1, 'Contract 2', 2),
    (4, 1, 'Contract 3', 2),
    (5, 2, 'Request 2', 2);

INSERT INTO document_field (id, document_id, template_field_id, value) VALUES
    (1, 1, 1, 'Klapatnyuk'),
    (2, 1, 2, 'Vyacheslav'),
    (3, 1, 3, 'Abiding citizen'),
    (4, 1, 4, 'Y'),

    (5, 2, 5, 'Vyacheslav'),
    (6, 2, 6, '+7 (911) 032-36-30'),

    (7, 3, 1, 'Ivanov'),
    (8, 3, 2, 'Ivan'),
    (9, 3, 4, 'N'),

    (10, 4, 1, 'Ivanov'),
    (11, 4, 2, 'Ivan'),
    (12, 4, 3, 'The taxpayer'),
    (13, 4, 4, 'Y'),

    (14, 5, 5, 'Ivan'),
    (15, 5, 6, '+7 (000) 000-00-00'),
    (16, 5, 7, 'Call after 18:00');

-- Rollback all actions

-- DELETE FROM document_field;
-- DELETE FROM document;
-- DELETE FROM template_field;
-- DELETE FROM template;
-- DELETE FROM "user";
