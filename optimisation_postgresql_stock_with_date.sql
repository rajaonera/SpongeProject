
-- 1. Créer les index pour accélérer les requêtes fréquentes
CREATE INDEX idx_idmatiere ON stock_matiere(idmatiere);  -- Index sur la colonne 'idmatiere'
CREATE INDEX idx_type_mouv ON stock_matiere(type_mouv);  -- Index sur le type de mouvement
CREATE INDEX idx_date_ajout ON stock_matiere(date_ajout);  -- Index sur la date du mouvement
CREATE INDEX idx_idmatiere_date_ajout ON stock_matiere(idmatiere, date_ajout);  -- Index composite pour optimiser la méthode FIFO

-- 2. Créer la table de stock_matiere (si elle n'existe pas déjà)
-- CREATE TABLE IF NOT EXISTS stock_matiere (
--     id SERIAL PRIMARY KEY,
--     idmatiere int,
--     type_mouv varchar(256),
--     qtt INT,
--     punitaire NUMERIC(10,2),
--     date_mouvement TIMESTAMP
-- );

-- 3. Partitionner la table par date de mouvement (si applicable)
-- Cette étape améliore la gestion des grandes tables en permettant à PostgreSQL de gérer des partitions par date
-- CREATE TABLE IF NOT EXISTS stock_matiere (
--     id SERIAL PRIMARY KEY,
--     idmatiere int,
--     type_mouv VARCHAR(256),
--     qtt INT,
--     punitaire NUMERIC(10,2),
--     date_ajout TIMESTAMP
-- ) PARTITION BY RANGE (date_ajout);

-- Partitionner la table en fonction de l'année (exemple)
CREATE TABLE IF NOT EXISTS stock_matiere_2024 PARTITION OF stock_matiere FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
CREATE TABLE IF NOT EXISTS stock_matiere_2023 PARTITION OF stock_matiere FOR VALUES FROM ('2023-01-01') TO ('2024-01-01');

-- 4. Créer une vue matérialisée pour stocker les résultats fréquemment demandés
-- Cela permet de pré-calculer les coûts totaux des idmatieres, ce qui rendra les lectures ultérieures plus rapides.
CREATE MATERIALIZED VIEW mv_stock_fifo AS
SELECT
    idmatiere,
    SUM(qtt * punitaire) AS coût_total
FROM
    stock_matiere
WHERE
    type_mouv = 'sortie'
GROUP BY
    idmatiere;

-- Rafraîchir la vue matérialisée chaque jour ou à la demande
REFRESH MATERIALIZED VIEW mv_stock_fifo;

-- 5. Créer une fonction pour gérer la sortie des stock_matiere avec la méthode FIFO et la date de mouvement
-- Cette fonction calcule le coût des idmatieres sortis en respectant la méthode FIFO et une date limite de mouvement.
CREATE OR REPLACE FUNCTION calculer_cout_sortie(
    idmatiere_sorti varchar(256),
    quantite_sortie INT, 
    date_limite TIMESTAMP
)
RETURNS NUMERIC AS $$
DECLARE
    cout_total NUMERIC := 0;
    quantite_restante INT := quantite_sortie;
BEGIN
    -- Récupérer les entrées par idmatiere, triées par date de mouvement et respectant la date limite
    FOR r IN 
        SELECT id, idmatiere, type_mouv, qtt, punitaire, date_ajout
        FROM stock_matiere
        WHERE idmatiere = idmatiere_sorti AND type_mouv = 'Entrée' AND date_ajout <= date_limite
        ORDER BY date_ajout
    LOOP
        IF r.qtt >= quantite_restante THEN
            -- Si la qtt de l'entrée est supérieure ou égale à la qtt restante à sortir
            cout_total := cout_total + quantite_restante * r.punitaire;
            EXIT; -- Sortir après avoir trouvé la qtt à sortir
        ELSE
            -- Si la qtt de l'entrée est inférieure à la qtt restante
            cout_total := cout_total + r.qtt * r.punitaire;
            quantite_restante := quantite_restante - r.qtt;
        END IF;
    END LOOP;
    
    -- Retourner le coût total des sorties
    RETURN cout_total;
END;
$$ LANGUAGE plpgsql;

-- 6. Utiliser la fonction pour calculer le coût total des 12 idmatieres sortis avant une certaine date
-- Vous pouvez appeler cette fonction pour obtenir le coût des 12 idmatieres sortis d'un type de idmatiere spécifique avant une date donnée.
SELECT calculer_cout_sortie('A', 12, '2024-05-01 00:00:00') AS cout_total_sortie;

-- 7. Exemple de requête pour voir les coûts totaux des sorties selon FIFO et une date limite
WITH idmatieres_entrants AS (
    SELECT
        idmatiere,
        qtt,
        punitaire,
        SUM(qtt) OVER (PARTITION BY idmatiere ORDER BY date_ajout) AS stock_cumulé
    FROM
        stock_matiere
    WHERE
        type_mouv = 'entree' AND date_ajout <= '2024-11-21 00:00:00'
),
idmatieres_sortants AS (
    SELECT
        idmatiere,
        qtt,
        punitaire,
        SUM(qtt) OVER (PARTITION BY idmatiere ORDER BY date_ajout) AS stock_cumulé
    FROM
        stock_matiere
    WHERE
        type_mouv = 'sortie' AND date_ajout <= '2024-11-21 00:00:00'
)
SELECT
    p.idmatiere,
    SUM(ps.qtt * ps.punitaire) AS coût_total
FROM
    idmatieres_entrants p
JOIN
    idmatieres_sortants ps ON p.idmatiere = ps.idmatiere
WHERE
    ps.stock_cumulé <= 400
GROUP BY
    p.idmatiere;
