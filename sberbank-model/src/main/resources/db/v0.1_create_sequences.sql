-- Create sequences

CREATE SEQUENCE user_ids;
CREATE SEQUENCE template_ids;
CREATE SEQUENCE template_field_ids;
CREATE SEQUENCE document_ids;
CREATE SEQUENCE document_field_ids;

-- Rollback all actions

-- DROP SEQUENCE IF EXISTS document_field_ids;
-- DROP SEQUENCE IF EXISTS document_ids;
-- DROP SEQUENCE IF EXISTS template_field_ids;
-- DROP SEQUENCE IF EXISTS template_ids;
-- DROP SEQUENCE IF EXISTS user_ids;
