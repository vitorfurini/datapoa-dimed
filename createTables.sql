
create table coordenadas (
                             id bigint NOT NULL AUTO_INCREMENT,
                             latitude double not null,
                             longitude double not null,
                             itinerario bigint,
                             primary key (id)
);
create table itinerario (
                            id bigint not null AUTO_INCREMENT,
                            idlinha bigint,
                            latitude double,
                            longitude double,
                            primary key (id)
);

create table linha (
                       id bigint not null AUTO_INCREMENT,
                       codigo varchar(255),
                       nome varchar(255),
                       primary key (id)
);

create table taxi (
                      id bigint AUTO_INCREMENT,
                      data_hora_cadastro timestamp,
                      latitude double,
                      longitude double,
                      nome_do_ponto varchar(255),
                      primary key (id)
);