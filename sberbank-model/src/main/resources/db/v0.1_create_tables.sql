-- Create tables

CREATE TABLE IF NOT EXISTS "user" (
  id       SERIAL,
  login    VARCHAR NOT NULL,
  password VARCHAR NOT NULL,
  role     VARCHAR NOT NULL DEFAULT 'CLIENT',
  CONSTRAINT user_pk PRIMARY KEY (id),
  CONSTRAINT user_login_uk UNIQUE (login),
  CONSTRAINT user_login_match CHECK (login ~* '^[a-z0-9._%-]+$'),
  CONSTRAINT user_role_in CHECK (role IN ('ADMIN', 'CLIENT'))
);

CREATE TABLE IF NOT EXISTS template (
  id     SERIAL,
  title  VARCHAR NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT template_pk PRIMARY KEY (id),
  CONSTRAINT template_title_uk UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS template_field (
  id          SERIAL,
  template_id INTEGER NOT NULL,
  title       VARCHAR NOT NULL,
  label       VARCHAR NOT NULL,
  type        VARCHAR NOT NULL DEFAULT 'LINE',
  "order"     INTEGER NOT NULL DEFAULT 0,
  active      BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT template_field_pk PRIMARY KEY (id),
  CONSTRAINT template_field_template_fk FOREIGN KEY (template_id) REFERENCES template (id),
  CONSTRAINT template_field_title_uk UNIQUE (template_id, title),
  CONSTRAINT template_field_type_in CHECK (type IN ('LINE', 'AREA', 'CHECKBOX')),
  CONSTRAINT template_field_order_uk UNIQUE (template_id, "order")
);

CREATE TABLE IF NOT EXISTS document (
  id          SERIAL,
  template_id INTEGER NOT NULL,
  title       VARCHAR NOT NULL,
  owner_id    INTEGER NOT NULL,
  active      BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT document_pk PRIMARY KEY (id),
  CONSTRAINT document_template_fk FOREIGN KEY (template_id) REFERENCES template (id),
  CONSTRAINT document_title_uk UNIQUE (template_id, title, owner_id)
);

CREATE TABLE IF NOT EXISTS document_field (
  id                SERIAL,
  document_id       INTEGER NOT NULL,
  template_field_id INTEGER NOT NULL,
  value             VARCHAR NOT NULL,
  active            BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT document_field_pk PRIMARY KEY (id),
  CONSTRAINT document_field_document_fk FOREIGN KEY (document_id) REFERENCES document (id),
  CONSTRAINT document_field_template_field_fk FOREIGN KEY (template_field_id) REFERENCES template_field (id),
  CONSTRAINT document_field_document_uk UNIQUE (document_id, template_field_id)
);

-- Rollback all actions

DROP TABLE IF EXISTS document_field CASCADE;
DROP TABLE IF EXISTS document CASCADE;
DROP TABLE IF EXISTS template_field CASCADE;
DROP TABLE IF EXISTS template CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
