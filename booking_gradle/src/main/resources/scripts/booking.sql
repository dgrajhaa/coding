CREATE SEQUENCE IF NOT EXISTS ticket_ownr.bkg_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.bkg_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.booking
(
	bkg_id integer NOT NULL DEFAULT nextval('ticket_ownr.bkg_seq_num'::regclass),
	bkg_plt_id integer,
	usr_id integer,
	bkg_date TIMESTAMP WITHOUT TIME ZONE,
	bkg_sts character varying(30),
	CONSTRAINT bkg_id_pk PRIMARY KEY (bkg_id),
	CONSTRAINT id_fk FOREIGN KEY (bkg_plt_id) REFERENCES ticket_ownr.bkg_plt (bkg_plt_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
	CONSTRAINT id_fk2 FOREIGN KEY (usr_id) REFERENCES ticket_ownr.customer (usr_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
	CONSTRAINT id_fk3 FOREIGN KEY (pmt_id) REFERENCES ticket_ownr.payment () MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.booking OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.booking TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.booking TO postgres;