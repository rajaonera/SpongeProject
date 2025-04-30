package annexe;

import connexion.Base;

import java.sql.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BlocEpongeInserterOptimized {

    // Méthode pour insérer les blocs dans la base de données en batch
    public static void insererBlocsEnBatch(int nombreDeBlocs, double volumeInitial, double prixReelInitial, double prixTheoriqueInitial,
                                           double margeVolume, double margePrixReel, double margePrixTheorique, Connection conn) throws SQLException {

        // Préparer la requête SQL pour l'insertion
        String sql = "INSERT INTO block_test (volume, prix_reel, prix_theorique) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Désactiver le commit automatique pour mieux contrôler les transactions
            conn.setAutoCommit(false);

            // Insérer les blocs en parallèle (par exemple via un ExecutorService dans un autre contexte)
            for (int i = 0; i < nombreDeBlocs; i++) {
                BlocEponge bloc = genererBloc(volumeInitial, prixReelInitial, prixTheoriqueInitial,
                        margeVolume, margePrixReel, margePrixTheorique);

                // Ajouter les valeurs dans le batch
                pstmt.setDouble(1, bloc.getVolume());
                pstmt.setDouble(2, bloc.getPrixReel());
                pstmt.setDouble(3, bloc.getPrixTheorique());
                pstmt.addBatch();

                // Commit par batch après 1000 blocs
                if ((i + 1) % 1000 == 0) {
                    pstmt.executeBatch(); // Exécuter le batch
                    conn.commit(); // Commit des changements
                }
            }

            // Exécuter le dernier batch
            pstmt.executeBatch();
            conn.commit(); // Commit final après tous les insertions

            // Réactiver le commit automatique
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Fonction pour générer un bloc avec marges de manière rapide
    private static BlocEponge genererBloc(double volumeInitial, double prixReelInitial, double prixTheoriqueInitial,
                                          double margeVolume, double margePrixReel, double margePrixTheorique) {
        double volumeMax = volumeInitial * (1 + margeVolume / 100);
        double volumeMin = volumeInitial * (1 - margeVolume / 100);

        double prixReelMax = prixReelInitial * (1 + margePrixReel / 100);
        double prixReelMin = prixReelInitial * (1 - margePrixReel / 100);

        double prixTheoriqueMax = prixTheoriqueInitial * (1 + margePrixTheorique / 100);
        double prixTheoriqueMin = prixTheoriqueInitial * (1 - margePrixTheorique / 100);

        double volume = ThreadLocalRandom.current().nextDouble(volumeMin, volumeMax);
        double prixReel = ThreadLocalRandom.current().nextDouble(prixReelMin, prixReelMax);
        double prixTheorique = ThreadLocalRandom.current().nextDouble(prixTheoriqueMin, prixTheoriqueMax);

        return new BlocEponge(volume, prixReel, prixTheorique);
    }

    // Fonction pour calculer la somme des prix théoriques et réels à partir d'une liste de blocs
    public static double[] sommePrix(List<BlocEponge> blocs) {
        double sommePrixReel = 0;
        double sommePrixTheorique = 0;

        // Utilisation d'un stream pour additionner les prix rapidement
        for (BlocEponge bloc : blocs) {
            sommePrixReel += bloc.getPrixReel();
            sommePrixTheorique += bloc.getPrixTheorique();
        }

        return new double[]{sommePrixReel, sommePrixTheorique};
    }

    // Classe représentant un Bloc d'Eponge
    public static class BlocEponge {
        private final double volume;
        private final double prixReel;
        private final double prixTheorique;

        public BlocEponge(double volume, double prixReel, double prixTheorique) {
            this.volume = volume;
            this.prixReel = prixReel;
            this.prixTheorique = prixTheorique;
        }

        public double getVolume() {
            return volume;
        }

        public double getPrixReel() {
            return prixReel;
        }

        public double getPrixTheorique() {
            return prixTheorique;
        }
    }
//
//    public static void main(String[] args) throws SQLException {
//        // Exemple d'utilisation pour insertion et calcul de la somme
//
//        double volumeInitial = 50.0;
//        double prixReelInitial = 3000.0;
//        double prixTheoriqueInitial = 3000.0;
//
//        double margeVolume = 10.0;
//        double margePrixReel = 10.0;
//        double margePrixTheorique = 10.0;
//
//        int nombreDeBlocs = 1000000;
//
//        // Insertion optimisée des blocs
//        Connection conn  = Base.PsqlConnect();
//            insererBlocsEnBatch(nombreDeBlocs, volumeInitial, prixReelInitial, prixTheoriqueInitial,
//                    margeVolume, margePrixReel, margePrixTheorique, conn);
//
//        // Simuler une liste de blocs générés (remplacer par une vraie liste dans un cas réel)
//        List<BlocEponge> blocs = List.of(new BlocEponge(volumeInitial, prixReelInitial, prixTheoriqueInitial));
//
//        // Calculer la somme des prix
//        double[] sommes = sommePrix(blocs);
//        System.out.println("Somme des prix réels : " + sommes[0]);
//        System.out.println("Somme des prix théoriques : " + sommes[1]);
//
//    }
}
