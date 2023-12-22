CREATE SEQUENCE IF NOT EXISTS ticket_ownr.cty_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.cty_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.city
(
	cty_id integer NOT NULL DEFAULT nextval('ticket_ownr.cty_seq_num'::regclass),
	cty_name character varying(30),
	CONSTRAINT cty_id_pk PRIMARY KEY (cty_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.city OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.city TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.city TO postgres;