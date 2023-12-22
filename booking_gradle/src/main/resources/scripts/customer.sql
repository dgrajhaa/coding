CREATE SEQUENCE IF NOT EXISTS ticket_ownr.usr_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.usr_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.customer
(
	usr_id integer NOT NULL DEFAULT nextval('ticket_ownr.usr_seq_num'::regclass),
	first_name character varying(30),
	last_name character varying(30),
	email character varying(30),
	mobile integer,
	CONSTRAINT usr_id_pk PRIMARY KEY (usr_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.customer OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.customer TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.customer TO postgres;