# --- !Ups

CREATE TABLE resposta (
    idQrCode integer NOT NULL,
    email varchar(255) NOT NULL,
    resposta varchar(255),
    pontuacao integer,
    ultima_atualizacao varchar(30) NULL,
    PRIMARY KEY (idQrCode,email),
    FOREIGN KEY (idQrCode) REFERENCES qrcode(id),
    FOREIGN KEY (email) REFERENCES usuario(email)
);

# --- !Downs

DROP TABLE resposta;