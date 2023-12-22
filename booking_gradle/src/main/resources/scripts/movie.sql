CREATE SEQUENCE IF NOT EXISTS ticket_ownr.movie_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.movie_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.movie
(
	mov_id integer NOT NULL DEFAULT nextval('ticket_ownr.movie_seq_num'::regclass),
	thtr_id integer,
	mov_name character varying(30),
	CONSTRAINT mov_id_pk PRIMARY KEY (mov_id),
	CONSTRAINT id_fk FOREIGN KEY (thtr_id) REFERENCES ticket_ownr.theatre (thtr_id) MATCH SIMPLE
			ON UPDATE NO ACTION
			ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.movie OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.movie TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.movie TO postgres;