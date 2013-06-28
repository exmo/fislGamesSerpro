# --- !Ups


CREATE TABLE qrcode (
    id integer NOT NULL AUTO_INCREMENT,
    texto TEXT,
    tipo char(255),
    resposta TEXT,
    alternativa1 TEXT,
    alternativa2 TEXT,
    alternativa3 TEXT,
    textoQrCode TEXT,
    pontuacao INT,
    PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE qrcode;
