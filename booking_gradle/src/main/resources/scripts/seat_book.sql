CREATE SEQUENCE IF NOT EXISTS ticket_ownr.st_bkg_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.st_bkg_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.seat_book
(
	st_bk_id integer NOT NULL DEFAULT nextval('ticket_ownr.st_bkg_seq_num'::regclass),
	bkg_id integer,
	seat_id integer,
	CONSTRAINT st_bk_id_pk PRIMARY KEY (st_bk_id),
	CONSTRAINT id_fk FOREIGN KEY (bkg_id) REFERENCES ticket_ownr.booking (bkg_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION,
	CONSTRAINT id_fk1 FOREIGN KEY (seat_id) REFERENCES ticket_ownr.seat (seat_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.seat_book OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.seat_book TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.seat_book TO postgres;