# --- !Ups

CREATE TABLE resposta (
    idQrCode integer NOT NULL,
    email char(255) NOT NULL,
    resposta char(255),
    pontuacao int NOT NULL,
    ultima_atualizacao char(30) NULL,
    PRIMARY KEY (idQrCode,email),
    FOREIGN KEY (idQrCode) REFERENCES qrcode(id),
    FOREIGN KEY (email) REFERENCES usuario(email)
);

# --- !Downs

DROP TABLE resposta;
