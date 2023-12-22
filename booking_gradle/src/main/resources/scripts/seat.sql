CREATE SEQUENCE IF NOT EXISTS ticket_ownr.seat_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.seat_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.seat
(
	seat_id integer NOT NULL DEFAULT nextval('ticket_ownr.seat_seq_num'::regclass),
	shw_id integer,
	rw_nam character varying(30),
	seat_no integer,
	locked character varying(30),
	lck_exp_on TIMESTAMP WITHOUT TIME ZONE,
	sts character varying(30),
	ver integer,
	CONSTRAINT seat_id_pk PRIMARY KEY (seat_id),
	CONSTRAINT id_fk FOREIGN KEY (shw_id) REFERENCES ticket_ownr.shows (shw_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.seat OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.seat TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.seat TO postgres;