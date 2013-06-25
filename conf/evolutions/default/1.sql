# --- !Ups


CREATE TABLE qrcode (
    id integer NOT NULL AUTO_INCREMENT,
    texto TEXT,
    tipo char(255),
    resposta char(255),
    pontuacao INT,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE qrcode;
