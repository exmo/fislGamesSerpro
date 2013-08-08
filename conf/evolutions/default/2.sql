# --- !Ups


######################### EVENTO
CREATE TABLE evento (
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
CREATE TABLE qrcode (
    id INT NOT NULL AUTO_INCREMENT,
    evento_id integer NOT NULL,
    texto TEXT,
    tipo char(255),
    resposta TEXT,
    alternativas TEXT,
    pontuacao INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_qrcode_evento
        FOREIGN KEY (evento_id) REFERENCES evento(id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);


insert into qrcode (evento_id,texto,tipo,resposta,alternativas,textoQrCode,pontuacao)
    values(1,
    "Qual a empresa, vinculada ao Ministério da Fazenda...?",
    "DESAFIO",
    "Serpro","N/A",
    5);

insert into qrcode (evento_id,texto,tipo,resposta,alternativas,textoQrCode,pontuacao)
    values(1,
    "Qual a empresa, vinculada ao Ministério da Fazenda...?",
    "DESAFIOME",
    "Serpro","Google",
    10);

insert into qrcode (evento_id,texto,tipo,resposta,alternativas,textoQrCode,pontuacao)
    values(1,
    "O SERPRO Games vai começar! Fique atento às novas orientações!",
    "INFO",
    "N/A","N/A",
    0);

insert into qrcode (evento_id,texto,tipo,resposta,alternativas,textoQrCode,pontuacao)
    values(1,
    "http://www.serpro.gov.br",
    "URL",
    "N/A","N/A",
    0);

################################# USUARIO
CREATE TABLE usuario (
    email char(255) NOT NULL PRIMARY KEY,
    nome char(255),
    telefone char(30)
);


################################# RESPOSTA
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
DROP TABLE usuario;
DROP TABLE qrcode;
DROP TABLE evento;
