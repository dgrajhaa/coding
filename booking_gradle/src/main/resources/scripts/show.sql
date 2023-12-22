CREATE SEQUENCE IF NOT EXISTS ticket_ownr.show_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.show_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.shows
(
	shw_id integer NOT NULL DEFAULT nextval('ticket_ownr.show_seq_num'::regclass),
	scn_id integer,
	shw_date TIMESTAMP WITHOUT TIME ZONE,
	strt_time TIMESTAMP WITHOUT TIME ZONE,
	end_time TIMESTAMP WITHOUT TIME ZONE,
	CONSTRAINT shw_id_pk PRIMARY KEY (shw_id),
	CONSTRAINT id_fk FOREIGN KEY (scn_id) REFERENCES ticket_ownr.screen (scn_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.shows OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.shows TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.shows TO postgres;