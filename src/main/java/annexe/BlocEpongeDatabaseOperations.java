package annexe;

import connexion.Base;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlocEpongeDatabaseOperations {

    // Fonction pour récupérer les données par pagination
    public static List<BlocEponge> recupererBlocsParPagination(Connection conn, int pageSize) throws SQLException {
        String sql = "SELECT volume, prix_reel, prix_theorique FROM block_test LIMIT ? OFFSET ?";
        List<BlocEponge> blocs = new ArrayList<>();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int offset = 0;
            while (true) {
                pstmt.setInt(1, pageSize);
                pstmt.setInt(2, offset);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) break; // Pas de nouvelles données

                do {
                    double volume = rs.getDouble("volume");
                    double prixReel = rs.getDouble("prix_reel");
                    double prixTheorique = rs.getDouble("prix_theorique");
                    blocs.add(new BlocEponge(volume, prixReel, prixTheorique));
                } while (rs.next());

                offset += pageSize;
            }
        }
        return blocs;
    }

    // Fonction pour insérer les données dans la table "blocs"
    public static void insererBlocsEnBatch(List<BlocEponge> blocs, Connection conn) throws SQLException {
        String sql = "INSERT INTO block_test (volume, prix_reel, prix_theorique) VALUES (?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // Désactiver le commit automatique pour optimiser
            int count = 0;

            for (BlocEponge bloc : blocs) {
                pstmt.setDouble(1, bloc.getVolume());
                pstmt.setDouble(2, bloc.getPrixReel());
                pstmt.setDouble(3, bloc.getPrixTheorique());
                pstmt.addBatch();

                if (++count % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // Dernier batch
            conn.commit(); // Commit final
        }
    }

    // Fonction pour insérer les données à partir d'un fichier CSV
    public static void insererDepuisCSV(String filePath, Connection conn) throws SQLException, IOException {
        String sql = "INSERT INTO block_test (volume, prix_reel, prix_theorique) VALUES (?, ?, ?)";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            String line;
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 3) continue; // Sauter les lignes invalides

                pstmt.setDouble(1, Double.parseDouble(values[0]));
                pstmt.setDouble(2, Double.parseDouble(values[1]));
                pstmt.setDouble(3, Double.parseDouble(values[2]));
                pstmt.addBatch();

                if (++count % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch();
            conn.commit();
        }
    }

    // Classe représentant un bloc d'éponge
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
////        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ma_base", "user", "password")) {
//            Connection conn = Base.PsqlConnect();
//            // Exemple d'utilisation pour récupérer les données
//            List<BlocEponge> blocs = recupererBlocsParPagination(conn, 1000);
//            System.out.println("Nombre de blocs récupérés : " + blocs.size());
//
////             Exemple d'insertion de blocs
//            insererBlocsEnBatch(blocs, conn);
//            System.out.println("Blocs insérés avec succès.");
//
////             Exemple d'insertion depuis un fichier CSV
////            String filePath = "path/to/your/file.csv";
////            insererDepuisCSV(filePath, conn);
////            System.out.println("Blocs insérés depuis le fichier CSV.");
//
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//    }
}
