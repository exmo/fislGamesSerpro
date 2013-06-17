# --- !Ups

CREATE TABLE resposta (
    idQrCode integer NOT NULL,
    email varchar(255) NOT NULL,
    resposta varchar(255),
    pontuacao integer,
    PRIMARY KEY (idQrCode,email),
    FOREIGN KEY (idQrCode) REFERENCES qrcode(id),
    FOREIGN KEY (email) REFERENCES usuario(email)
);

# --- !Downs

DROP TABLE resposta;