CREATE SEQUENCE IF NOT EXISTS ticket_ownr.lyt_seq_num
	INCREMENT 1
	START 1
	MINVALUE 1
	MAXVALUE 2147483647
	CACHE 1;
	
ALTER SEQUENCE ticket_ownr.lyt_seq_num OWNER TO postgres;

CREATE TABLE IF NOT EXISTS ticket_ownr.layout
(
	lyt_id integer NOT NULL DEFAULT nextval('ticket_ownr.lyt_seq_num'::regclass),
	ttl_row integer,
	ttl_clmn integer,
	CONSTRAINT lyt_id_pk PRIMARY KEY (lyt_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS ticket_ownr.layout OWNER to postgres;

GRANT SELECT ON TABLE ticket_ownr.layout TO pgread_user;

GRANT ALL ON TABLE ticket_ownr.layout TO postgres;