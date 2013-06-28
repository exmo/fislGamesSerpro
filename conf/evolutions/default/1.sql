# --- !Ups


CREATE TABLE qrcode (
    id integer NOT NULL AUTO_INCREMENT,
    texto TEXT,
    tipo char(255),
    resposta char(255),
    alternativa1 char(255),
    alternativa2 char(255),
    alternativa3 char(255),
    textoQrCode char(255),
    pontuacao INT,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE qrcode;
