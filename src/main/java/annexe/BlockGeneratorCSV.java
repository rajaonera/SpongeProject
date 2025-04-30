package  annexe;

import block.Block;
import connexion.Base;

import java.io.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class BlockGeneratorCSV {

    private static final double VOLUME_MIN = 1.8; // Volume minimum
    private static final double VOLUME_MAX = 2.2; // Volume maximum
    private static final double PRIX_MIN = 4500.0; // Prix minimum
    private static final double PRIX_MAX = 8500.0; // Prix maximum
    private static final double MARGE_PERCENTAGE = 0.1; // Exemple : 10% de marge


    /**
     * Génère une liste de timestamps correspondant aux jours ouvrables entre deux années.
     *
     * @param startYear L'année de début (incluse).
     * @param endYear   L'année de fin (incluse).
     * @return Une liste de timestamps (en millisecondes depuis Epoch).
     */
    public static List<Timestamp> generateWorkdayTimestamps(int startYear, int endYear) {
        List<Timestamp> timestamps = new ArrayList<>();
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);

        while (!startDate.isAfter(endDate)) {
            // Vérifie si le jour actuel est un jour ouvrable (lundi à vendredi)
            if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY && startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                // Génère un timestamp aléatoire entre 8h00 et 18h00
                LocalTime randomTime = generateRandomTime(8, 18);
                LocalDateTime dateTime = LocalDateTime.of(startDate, randomTime);
                timestamps.add(Timestamp.valueOf(dateTime));
            }
            startDate = startDate.plusDays(1);
        }

        // Trier les timestamps par ordre croissant
        Collections.sort(timestamps);

        return timestamps;
    }

    /**
     * Génère une heure aléatoire entre deux heures spécifiques.
     *
     * @param startHour Heure de début (incluse).
     * @param endHour   Heure de fin (incluse).
     * @return Une instance de LocalTime correspondant à l'heure générée.
     */
    private static LocalTime generateRandomTime(int startHour, int endHour) {
        int randomHour = ThreadLocalRandom.current().nextInt(startHour, endHour + 1);
        int randomMinute = ThreadLocalRandom.current().nextInt(0, 60);
        int randomSecond = ThreadLocalRandom.current().nextInt(0, 60);
        return LocalTime.of(randomHour, randomMinute, randomSecond);
    }

    /**
     * Exemple d'utilisation.
     */
    /**
     * Génère des blocs aléatoires et les écrit dans un fichier CSV.
     *
     * @param filePath       Chemin du fichier CSV à générer.
     * @param nombreDeBlocs  Nombre de blocs à générer.
     * @throws IOException En cas d'erreur d'écriture dans le fichier.
     */

    public static void generateBlocksToCSV(String filePath, int nombreDeBlocs) throws IOException {

        int count =0 ;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            List<Timestamp> timestamps = generateWorkdayTimestamps(2022, 2024);
            // Écriture de l'en-tête CSV
            writer.write("id,idsource,long,large,hauteur,date_ajout,status,volume,unit_price,idmachine,prix_reel\n");

            // Génération des blocs en parallèle
            IntStream.range(0, nombreDeBlocs).parallel().forEach(i -> {
                double volume = ThreadLocalRandom.current().nextDouble(VOLUME_MIN, VOLUME_MAX);
                double prixReel =  ThreadLocalRandom.current().nextDouble(PRIX_MIN, PRIX_MAX)* (1 + ThreadLocalRandom.current().nextDouble(-MARGE_PERCENTAGE, MARGE_PERCENTAGE));
                int idmachine = (ThreadLocalRandom.current().nextInt(1,3
                ));

                // Synchronisation pour écrire dans le fichier
                synchronized (writer) {
                    try {
                        writer.write(i + ",0,1,1," + volume +","+ timestamps.get(i)+",'usefull'," + volume + "," + prixReel + "," +idmachine+"," + prixReel*volume + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * Lit les données depuis un fichier CSV et les charge dans une liste d'objets Block.
     *
     * @param filePath Chemin du fichier CSV à lire.
     * @return Liste des blocs chargés depuis le fichier CSV.
     * @throws IOException En cas d'erreur de lecture.
     */
    public static List<Block> readBlocksFromCSV(String filePath, Connection connection) throws IOException {
        List<Block> blocks = new ArrayList<>();
        int c = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                // Ignore la première ligne (en-tête)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Découpe les colonnes
                String[] parts = line.split(",");
                String date_ajout =  parts[0];
                String longueur  =(parts[1]);
                String large = (parts[2]);
                String hauteur = (parts[3]);
                String unit_price = (parts[4]);
                String idmachine = (parts[5]);

                // Ajoute un bloc à la liste
                System.out.println("ligne :"+ c );
                blocks.add(new Block(longueur,large,hauteur,date_ajout,
               unit_price,idmachine,connection));
                c++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return blocks;
    }

    /**
     * Exemple d'utilisation.
     */
//    public static void main(String[] args) {
//        String filePath = "Data 1M.csv";
//        int nombreDeBlocs =  1_000_000; // Exemple : 1 million de blocs
//
//        try {
//            // Générer les données CSV
//              long startGeneration = System.currentTimeMillis();
////            generateBlocksToCSV(filePath, nombreDeBlocs);
////            long endGeneration = System.currentTimeMillis();
////            System.out.println("Fichier CSV généré avec succès : " + filePath);
////            System.out.println("Temps pour générer : " + (endGeneration - startGeneration) + " ms");
//
//            // Lire les données depuis le fichier CSV
//            long startRead = System.currentTimeMillis();
//            Connection connection = Base.PsqlConnect();
//            List<Block> blocks = readBlocksFromCSV(filePath,connection);
//            long endRead = System.currentTimeMillis();
//            System.out.println("Données lues depuis le CSV : " + blocks.size() + " blocs");
//            System.out.println("Temps pour lire : " + (endRead - startRead) + " ms");
////
////            // Exemple : Afficher les 5 premiers blocs
////            blocks.stream().limit(5).forEach(System.out::println);
//
//        } catch (IOException e) {
//            System.err.println("Erreur : " + e.getMessage());
//        }
//    }
}
