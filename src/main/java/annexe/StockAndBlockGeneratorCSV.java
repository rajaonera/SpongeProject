package annexe;
import block.Block;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class StockAndBlockGeneratorCSV {

    private static final double VOLUME_MIN = 10.0; // Volume minimum
    private static final double VOLUME_MAX = 100.0; // Volume maximum
    private static final double PRIX_MIN = 50.0; // Prix minimum
    private static final double PRIX_MAX = 200.0; // Prix maximum
    private static final double MARGE_PERCENTAGE = 0.1; // Exemple : 10% de marge
    private static final double QUANTITE_MIN = 100.0; // Quantité minimum pour les mouvements de stock
    private static final double QUANTITE_MAX = 1000.0; // Quantité maximum pour les mouvements de stock

    /**
     * Classe représentant un bloc d'éponge.
     */

//    static class Block {
//        int id;
//        double volume;
//        double prixReel;
//        double prixTheorique;
//
//        public Block(int id, double volume, double prixReel, double prixTheorique) {
//            this.id = id;
//            this.volume = volume;
//            this.prixReel = prixReel;
//            this.prixTheorique = prixTheorique;
//        }
//
//        @Override
//        public String toString() {
//            return "Block{id=" + id + ", volume=" + volume + ", prixReel=" + prixReel + ", prixTheorique=" + prixTheorique + "}";
//        }
//    }

    /**
     * Classe représentant un mouvement de stock.
     */
    static class StockMovement {
        int id;
        double quantity;
        double price;
        long timestamp;

        public StockMovement(int id, double quantity, double price, long timestamp) {
            this.id = id;
            this.quantity = quantity;
            this.price = price;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "StockMovement{id=" + id + ", quantity=" + quantity + ", price=" + price + ", timestamp=" + timestamp + "}";
        }
    }

    /**
     * Génère des blocs aléatoires et les écrit dans un fichier CSV.
     */
    public static void generateBlocksToCSV(String filePath, int nombreDeBlocs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,volume,prix_reel,prix_theorique\n");
            IntStream.range(0, nombreDeBlocs).parallel().forEach(i -> {
                double volume = ThreadLocalRandom.current().nextDouble(VOLUME_MIN, VOLUME_MAX);
                double prixReel = ThreadLocalRandom.current().nextDouble(PRIX_MIN, PRIX_MAX);
                double prixTheorique = prixReel * (1 + ThreadLocalRandom.current().nextDouble(-MARGE_PERCENTAGE, MARGE_PERCENTAGE));
                synchronized (writer) {
                    try {
                        writer.write(i + "," + volume + "," + prixReel + "," + prixTheorique + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Lit les blocs depuis un fichier CSV.
     */
    public static List<Block> readBlocksFromCSV(String filePath) throws IOException {
        List<Block> blocks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                double volume = Double.parseDouble(parts[1]);
                double prixReel = Double.parseDouble(parts[2]);
                double prixTheorique = Double.parseDouble(parts[3]);
//                blocks.add(new Block(id, volume, prixReel, prixTheorique));
            }
        }
        return blocks;
    }

    /**
     * Génère des mouvements de stock aléatoires et les écrit dans un fichier CSV.
     */
    public static void generateStockMovementsToCSV(String filePath, int nombreDeMouvements) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id,quantity,price,timestamp\n");
            IntStream.range(0, nombreDeMouvements).parallel().forEach(i -> {
                double quantity = ThreadLocalRandom.current().nextDouble(QUANTITE_MIN, QUANTITE_MAX);
                double price = ThreadLocalRandom.current().nextDouble(PRIX_MIN, PRIX_MAX);
                long timestamp = System.currentTimeMillis() - ThreadLocalRandom.current().nextInt(0, 1_000_000_000);
                synchronized (writer) {
                    try {
                        writer.write(i + "," + quantity + "," + price + "," + timestamp + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Lit les mouvements de stock depuis un fichier CSV.
     */
    public static List<StockMovement> readStockMovementsFromCSV(String filePath) throws IOException {
        List<StockMovement> movements = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                double quantity = Double.parseDouble(parts[1]);
                double price = Double.parseDouble(parts[2]);
                long timestamp = Long.parseLong(parts[3]);
                movements.add(new StockMovement(id, quantity, price, timestamp));
            }
        }
        return movements;
    }

    /**
     * Exemple d'utilisation.
     */
//    public static void main(String[] args) {
//        String blockFilePath = "blocks.csv";
//        String stockFilePath = "stock_movements.csv";
//        int nombreDeBlocs = 1_000_000;
//        int nombreDeMouvements = 500_000;
//
//        try {
//            // Génération des blocs
//            generateBlocksToCSV(blockFilePath, nombreDeBlocs);
//            System.out.println("Blocs générés et enregistrés dans " + blockFilePath);
//
//            // Lecture des blocs
//            List<Block> blocks = readBlocksFromCSV(blockFilePath);
//            System.out.println("Blocs lus : " + blocks.size());
//
//            // Génération des mouvements de stock
//            generateStockMovementsToCSV(stockFilePath, nombreDeMouvements);
//            System.out.println("Mouvements de stock générés et enregistrés dans " + stockFilePath);
//
//            // Lecture des mouvements de stock
//            List<StockMovement> movements = readStockMovementsFromCSV(stockFilePath);
//            System.out.println("Mouvements de stock lus : " + movements.size());
//
//        } catch (IOException e) {
//            System.err.println("Erreur : " + e.getMessage());
//        }
//    }
}
