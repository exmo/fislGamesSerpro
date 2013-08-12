# --- !Ups

######################### EVENTO
CREATE TABLE IF NOT EXISTS evento (
    id INT NOT NULL AUTO_INCREMENT,
    nome TEXT,
    descricao TEXT,
    PRIMARY KEY (id)
);


insert into evento (nome,descricao)
    values("CONSEGI 2013","Evento criado apenas para testes");

insert into evento (nome,descricao)
    values("Evento de teste","Evento criado apenas para testes");


######################### QRCODE
CREATE TABLE IF NOT EXISTS qrcode (
    id INT NOT NULL AUTO_INCREMENT,
    evento_id integer NOT NULL,
    texto TEXT,
    tipo char(255),
    resposta TEXT,
    alternativas TEXT,
    pontuacao INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_qrcode_evento1
        FOREIGN KEY (evento_id) REFERENCES evento(id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);


insert into qrcode (evento_id,texto,tipo,resposta,alternativas,pontuacao)
    values(1,
    "Qual a empresa, vinculada ao Ministério da Fazenda...?",
    "DESAFIO",
    "Serpro","N/A",
    5);

insert into qrcode (evento_id,texto,tipo,resposta,alternativas,pontuacao)
    values(1,
    "Qual a empresa, vinculada ao Ministério da Fazenda...?",
    "DESAFIOME",
    "Serpro","Google#Microsoft#Serpro",
    10);

insert into qrcode (evento_id,texto,tipo,resposta,alternativas,pontuacao)
    values(1,
    "O SERPRO Games vai começar! Fique atento às novas orientações!",
    "INFO",
    "N/A","N/A",
    0);

insert into qrcode (evento_id,texto,tipo,resposta,alternativas,pontuacao)
    values(1,
    "http://www.serpro.gov.br",
    "URL",
    "N/A","N/A",
    0);

################################# USUARIO
CREATE TABLE IF NOT EXISTS usuario (
    email char(255) NOT NULL PRIMARY KEY,
    nome char(255),
    telefone char(30)
);


################################# RESPOSTA
CREATE TABLE IF NOT EXISTS resposta (
    idQrCode integer NOT NULL,
    email char(255) NOT NULL,
    resposta char(255),
    pontuacao int NOT NULL,
    ultima_atualizacao char(30) NULL,
    PRIMARY KEY (idQrCode,email),
    FOREIGN KEY (idQrCode) REFERENCES qrcode(id),
    FOREIGN KEY (email) REFERENCES usuario(email)
);


################################# PALESTRA
CREATE TABLE palestra (
    id integer NOT NULL AUTO_INCREMENT,
    descricao char(255) NOT NULL,
    data char(16),
    palestrante char(50) NOT NULL,
    evento_id integer,
    PRIMARY KEY (id),
    CONSTRAINT fk_palestra_evento,
    FOREIGN KEY (evento_id) REFERENCES evento(id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);


################################# REGISTRO DE UM USUARIO EM UMA PALESTRA
CREATE TABLE participacao_palestra (
    idPalestra integer NOT NULL,
    email char(255) NOT NULL,
    tipo char(1), #(I)n (O)ut
    data_hora char(16),
    PRIMARY KEY (idPalestra,email,tipo),
    FOREIGN KEY (idPalestra) REFERENCES palestra(id),
    FOREIGN KEY (email) REFERENCES usuario(email)
);

# --- !Downs

DROP TABLE IF EXISTS resposta;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS qrcode;
DROP TABLE IF EXISTS evento;
DROP TABLE IF EXISTS palestra;
DROP TABLE IF EXISTS participacao_palestra;
