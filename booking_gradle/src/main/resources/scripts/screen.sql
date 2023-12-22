CREATE SEQUENCE IF NOT EXISTS ticket_ownr.scn_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.scn_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.screen
(
	scn_id integer NOT NULL DEFAULT nextval('ticket_ownr.scn_seq_num'::regclass),
	lyt_id integer,
	thtr_id integer,
	scn_name character varying(30),
	CONSTRAINT scn_id_pk PRIMARY KEY (scn_id),
	CONSTRAINT id_fk FOREIGN KEY (lyt_id) REFERENCES ticket_ownr.layout (lyt_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
	CONSTRAINT id_fk2 FOREIGN KEY (thtr_id) REFERENCES ticket_ownr.theatre (thtr_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.screen OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.screen TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.screen TO postgres;