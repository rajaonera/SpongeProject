create table block(
    id SERIAL primary key,
    idSource int default 0,
    long decimal(10,2),
    large decimal(10,2),
    hauteur decimal(10,2),
    date_ajout date,
    status varchar(256) check ( block.status in  ('usefull','useless') ),
    volume decimal(10,2),
    unit_price decimal(10,2)
);

create table forme_usuel(
    id SERIAL primary key,
    nom varchar(265),
    long decimal(10,2),
    large decimal(10,2),
    hauteur decimal(10,2),
    volume decimal(10,2),
    date_ajout date
);

create table Prix_usuel(
    id SERIAL primary key,
    idForme int references forme_usuel(id) on delete  cascade on update cascade ,
    unit_price decimal(10,2),
    type_price varchar(256) check ( type_price in  ('vente','achat') ),
    date_modif date
);

CREATE TABLE  MouvStock(
    id SERIAL primary key ,
    idForme int references forme_usuel(id) on delete  cascade  on update cascade ,
    type_mouv varchar(256) check ( MouvStock.type_mouv in  ('entree','sortie') ),
    qtt decimal(10,2),
    date_ajout date,
    idBlock int references block(id)  on delete  cascade  on update cascade
);

ALTER TABLE block DISABLE TRIGGER ALL;  -- Désactiver tous les triggers, y compris ceux des clés étrangères

DELETE FROM block where id<100;

ALTER TABLE block ENABLE TRIGGER ALL;  -- Réactiver tous les triggers

ALTER SEQUENCE block_id_seq RESTART WITH 1;  -- Remplacez 1 par le nombre souhaité


ALTER TABLE prix_usuel DISABLE TRIGGER ALL;  -- Désactiver tous les triggers, y compris ceux des clés étrangères

DELETE FROM prix_usuel where id<100;

ALTER TABLE prix_usuel ENABLE TRIGGER ALL;  -- Réactiver tous les triggers

ALTER SEQUENCE prix_usuel_id_seq RESTART WITH 1;  -- Remplacez 1 par le nombre souhaité



ALTER TABLE forme_usuel DISABLE TRIGGER ALL;  -- Désactiver tous les triggers, y compris ceux des clés étrangères

DELETE FROM forme_usuel where id<100;

ALTER TABLE forme_usuel ENABLE TRIGGER ALL;  -- Réactiver tous les triggers

ALTER SEQUENCE forme_usuel_id_seq RESTART WITH 1;  -- Remplacez 1 par le nombre souhaité


ALTER TABLE mouvstock DISABLE TRIGGER ALL;  -- Désactiver tous les triggers, y compris ceux des clés étrangères

DELETE FROM mouvstock where id<100;

ALTER TABLE mouvstock ENABLE TRIGGER ALL;  -- Réactiver tous les triggers

ALTER SEQUENCE mouvstock_id_seq RESTART WITH 1;  -- Remplacez 1 par le nombre souhaité


/*
        PARTIE 2
*/
-- ok
CREATE TABLE  formule (
                          id SERIAL PRIMARY KEY ,
                          nom VARCHAR(265)
);
-- ok
create table unite (
                       id  SERIAL PRIMARY KEY ,
                       nom VARCHAR(265)
);
-- ok
CREATE TABLE Matiere(
                        id SERIAL PRIMARY KEY ,
                        nom VARCHAR(265),
                        idUnite INT REFERENCES unite(id) ON DELETE  CASCADE  ON UPDATE CASCADE
);
-- ok
CREATE  TABLE details_formule(
                                 id SERIAL PRIMARY KEY ,
                                 idFormule int references formule(id) ON DELETE CASCADE ON UPDATE CASCADE,
                                 idMatiere int references Matiere(id) ON DELETE  CASCADE ON UPDATE CASCADE ,
                                 quantite decimal(10,2)
);

-- ok
create table machine (
                         id SERIAL PRIMARY KEY ,
                         nom VARCHAR(265),
                         idFormule INT REFERENCES formule(id) ON DELETE  CASCADE  ON UPDATE CASCADE
);

drop table  block cascade ;
create table block(
                      id SERIAL primary key,
                      idSource int default 0,
                      long decimal(10,2),
                      large decimal(10,2),
                      hauteur decimal(10,2),
                      date_ajout date,
                      status varchar(256) check ( block.status in  ('usefull','useless') ),
                      volume decimal(10,2),
                      unit_price decimal(10,2),
                        idMachine int references machine(id) on delete cascade on update cascade
);

CREATE  TABLE details_fabrication(
                            id SERIAL PRIMARY KEY ,
                            idBlock int references block(id) on delete  cascade  on update cascade ,
                            theorique decimal(10,2),
                            machiniste decimal(10,2)
);

CREATE  TABLE info_block(
                            id SERIAL PRIMARY KEY ,
                            idBlock int references block(id) on delete  cascade  on update cascade ,
                            theorique decimal(10,2),
                            machiniste decimal(10,2),
                            lastest_modif date default now()
);
drop  table  MouvStock;
CREATE TABLE  MouvStock(
                           id SERIAL primary key ,
                           idForme int references forme_usuel(id) on delete  cascade  on update cascade ,
                           type_mouv varchar(256) check ( MouvStock.type_mouv in  ('entree','sortie') ),
                           qtt decimal(10,2),
                           date_ajout date,
                           idBlock int references block(id)  on delete  cascade  on update cascade
);

-- get all matiere in details formule
SELECT  * FROM details_formule where idFormule = 1;

CREATE TABLE  stock_matiere(
                           id SERIAL primary key ,
                           idMatiere int references Matiere(id) on delete  cascade  on update cascade ,
                           type_mouv varchar(256) check ( stock_matiere.type_mouv in  ('entree','sortie') ),
                           qtt decimal(10,2),
                           date_ajout date
);




create or replace  view reste_stock as
select
    idmatiere,sum(
        CASE  when type_mouv = 'entree'
                  then qtt when type_mouv  = 'sorite'
                  then  -qtt else  0 end ) as reste
from stock_matiere group by idmatiere ;

create table fabrication(
    id SERIAL PRIMARY KEY,
    idBlock int references  block(id),
    idmatiere int references  matiere(id),
    punitaire decimal(10,2),
    date_ajout timestamp,
    qtt decimal(10,2)
);

INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (4, 1, 'entree', 224727345.00, '2024-01-01 00:00:00.000000', 600.00, 224727345.00);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (6, 1, 'entree', 224974632.00, '2022-01-01 00:00:00.000000', 400.00, 224974632.00);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (5, 1, 'entree', 225233313.00, '2023-01-01 00:00:00.000000', 500.00, 225233313.00);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (2, 2, 'entree', 225233313.00, '2023-01-01 00:00:00.000000', 5950.00, 225233313.00);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (1, 2, 'entree', 224727345.00, '2024-01-01 00:00:00.000000', 6000.00, 224727345.00);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (3, 2, 'entree', 224974632.00, '2022-01-01 00:00:00.000000', 5900.00, 224974632.00);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (9, 3, 'entree', 112487316.00, '2022-01-01 00:00:00.000000', 450.00, 112487316.00);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (7, 3, 'entree', 112363672.50, '2024-01-01 00:00:00.000000', 550.00, 112363672.50);
INSERT INTO public.stock_matiere (id, idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) VALUES (8, 3, 'entree', 112616656.50, '2023-01-01 00:00:00.000000', 500.00, 112616656.50);
