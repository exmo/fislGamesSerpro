# --- !Ups

CREATE SEQUENCE qrcode_id_seq;
CREATE TABLE qrcode (
    id integer NOT NULL DEFAULT nextval('qrcode_id_seq'),
    texto varchar(1024),
    tipo varchar(255),
    resposta varchar(255),
    pontuacao INT
);

# --- !Downs

DROP TABLE qrcode;
DROP SEQUENCE qrcode_id_seq;