# --- !Ups


CREATE TABLE qrcode (
    id integer NOT NULL AUTO_INCREMENT,
    texto varchar(1024),
    tipo varchar(255),
    resposta varchar(255),
    pontuacao INT
);

# --- !Downs

DROP TABLE qrcode;
