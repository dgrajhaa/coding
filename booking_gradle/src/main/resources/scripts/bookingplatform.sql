CREATE SEQUENCE IF NOT EXISTS ticket_ownr.plt_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.plt_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.bkg_plt
(
	bkg_plt_id integer NOT NULL DEFAULT nextval('ticket_ownr.plt_seq_num'::regclass),
	thtr_id integer,
	plt_name character varying(30),
	CONSTRAINT bkg_plt_id_pk PRIMARY KEY (bkg_plt_id),
	CONSTRAINT id_fk FOREIGN KEY (thtr_id) REFERENCES ticket_ownr.theatre (thtr_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.bkg_plt OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.bkg_plt TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.bkg_plt TO postgres;