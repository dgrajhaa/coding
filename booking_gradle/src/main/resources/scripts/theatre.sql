CREATE SEQUENCE IF NOT EXISTS ticket_ownr.thre_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.thre_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.theatre
(
	thtr_id integer NOT NULL DEFAULT nextval('ticket_ownr.thre_seq_num'::regclass),
	cty_id integer,
	thtr_name character varying(30),
	CONSTRAINT thre_id_pk PRIMARY KEY (thtr_id),
	CONSTRAINT id_fk FOREIGN KEY (cty_id) REFERENCES ticket_ownr.city (cty_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.theatre OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.theatre TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.theatre TO postgres;