# --- !Ups

CREATE TABLE usuario (
    email varchar(255) NOT NULL PRIMARY KEY,
    nome varchar(255)
);

# --- !Downs

DROP TABLE usuario;