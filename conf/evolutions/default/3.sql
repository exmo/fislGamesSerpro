# --- !Ups

################################# USUARIO
CREATE TABLE palestra (
    id integer NOT NULL,
    descricao char(255) NOT NULL,
    data char(16),
    palestrante char(50) NOT NULL,    
    PRIMARY KEY (id),
    CONSTRAINT fk_palestra_evento
    FOREIGN KEY (idEvento) REFERENCES evento(id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);


/palestras/@eventoid
{id,descricao,data,palestrante,qtdcheckins,qtdcheckouts}*

/palestra/@palestraid
descricao,data,palestrante,qtdcheckins,qtdcheckouts

/palestra/@palestraid/checkin/@idsuario
status

/palestra/@palestraid/checkout/@idsuario
status


tabela registros_palestra
idpalestra,idusuario,horario_checkin,horario_checkout


################################# RESPOSTA
CREATE TABLE participacao_palestra (
    idPalestra integer NOT NULL,
    email char(255) NOT NULL,
    tipo char(1), #(I)n (O)ut
    data_hora char(16)
    PRIMARY KEY (idPalestra,email),
    FOREIGN KEY (idPalestra) REFERENCES palestra(id),
    FOREIGN KEY (email) REFERENCES usuario(email)
);

# --- !Downs

DROP TABLE palestra;
DROP TABLE participacao_palestra;


