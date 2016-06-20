-- Create tables

CREATE TABLE IF NOT EXISTS "user" (
    id       INTEGER NOT NULL DEFAULT NEXTVAL('user_ids'),
    login    VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    role     VARCHAR NOT NULL DEFAULT 'CLIENT',
    CONSTRAINT user_pk PRIMARY KEY (id),
    CONSTRAINT user_login_uk UNIQUE (login),
    CONSTRAINT user_login_match CHECK (login ~* '^[a-z0-9._%-]+$'),
    CONSTRAINT user_role_in CHECK (role IN ('ADMIN', 'CLIENT'))
);

CREATE TABLE IF NOT EXISTS template (
    id     INTEGER   NOT NULL DEFAULT NEXTVAL('template_ids'),
    title  VARCHAR   NOT NULL,
    edited TIMESTAMP NOT NULL DEFAULT NOW(),
    active BOOLEAN   NOT NULL DEFAULT TRUE,
    CONSTRAINT template_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS template_field (
    id          INTEGER NOT NULL DEFAULT NEXTVAL('template_field_ids'),
    template_id INTEGER NOT NULL,
    title       VARCHAR NOT NULL,
    label       VARCHAR NOT NULL,
    type        VARCHAR NOT NULL DEFAULT 'LINE',
    "order"     INTEGER NOT NULL DEFAULT 0,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT template_field_pk PRIMARY KEY (id),
    CONSTRAINT template_field_template_fk FOREIGN KEY (template_id) REFERENCES template (id),
    CONSTRAINT template_field_type_in CHECK (type IN ('LINE', 'AREA', 'CHECKBOX'))
);

CREATE TABLE IF NOT EXISTS document (
    id          INTEGER   NOT NULL DEFAULT NEXTVAL('document_ids'),
    template_id INTEGER   NOT NULL,
    title       VARCHAR   NOT NULL,
    owner_id    INTEGER   NOT NULL,
    edited      TIMESTAMP NOT NULL DEFAULT NOW(),
    active      BOOLEAN   NOT NULL DEFAULT TRUE,
    CONSTRAINT document_pk PRIMARY KEY (id),
    CONSTRAINT document_template_fk FOREIGN KEY (template_id) REFERENCES template (id)
);

CREATE TABLE IF NOT EXISTS document_field (
    id                INTEGER NOT NULL DEFAULT NEXTVAL('document_field_ids'),
    document_id       INTEGER NOT NULL,
    template_field_id INTEGER NOT NULL,
    value             VARCHAR NOT NULL,
    active            BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT document_field_pk PRIMARY KEY (id),
    CONSTRAINT document_field_document_fk FOREIGN KEY (document_id) REFERENCES document (id),
    CONSTRAINT document_field_template_field_fk FOREIGN KEY (template_field_id) REFERENCES template_field (id)
);

-- Rollback all actions

-- DROP TABLE IF EXISTS document_field CASCADE;
-- DROP TABLE IF EXISTS document CASCADE;
-- DROP TABLE IF EXISTS template_field CASCADE;
-- DROP TABLE IF EXISTS template CASCADE;
-- DROP TABLE IF EXISTS "user" CASCADE;
