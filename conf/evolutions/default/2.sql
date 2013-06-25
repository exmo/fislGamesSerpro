# --- !Ups

CREATE TABLE usuario (
    email char(255) NOT NULL PRIMARY KEY,
    nome char(255)
);

# --- !Downs

DROP TABLE usuario;