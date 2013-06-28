# --- !Ups

CREATE TABLE usuario (
    email char(255) NOT NULL PRIMARY KEY,
    nome char(255),
    telefone char(30)
);

# --- !Downs

DROP TABLE usuario;