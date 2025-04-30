package annexe;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WorkdayTimestampGenerator {

    /**
     * Génère une liste de timestamps correspondant aux jours ouvrables entre deux années.
     *
     * @param startYear L'année de début (incluse).
     * @param endYear   L'année de fin (incluse).
     * @return Une liste de timestamps (en millisecondes depuis Epoch).
     */
    public static List<Timestamp> generateWorkdayTimestamps(int startYear, int endYear,double limit) {
        List<Timestamp> timestamps = new ArrayList<>();
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        Long jourEntree  = ChronoUnit.DAYS.between(startDate, endDate);
        Random random = new Random();
        while (limit>0) {
            Long joursleatoire  = random.nextLong(jourEntree+1) ;
            LocalDate jourAleatoire = startDate.plusDays(joursleatoire);

            // Vérifie si le jour actuel est un jour ouvrable (lundi à vendredi)
            if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY && startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                // Génère un timestamp aléatoire entre 8h00 et 18h00
                LocalTime randomTime = generateRandomTime(8, 18);
                LocalDateTime dateTime = LocalDateTime.of(startDate, randomTime);
                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                timestamps.add(new Timestamp(timestamp));
                limit--;
//                timestamps.add(new Timestamp() Timestamp.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
            }
//            startDate = startDate.plusDays(1);
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
//    public static void main(String[] args) {
//        List<Long> timestamps = generateWorkdayTimestamps(2022, 2024);
//        System.out.println("Nombre de timestamps générés : " + timestamps.size());
//        System.out.println("Exemple de timestamps triés (croissants) :");
//        timestamps.stream().limit(1000000).forEach(System.out::println); // Affiche les 10 premiers timestamps triés
//
//        // Convertir les timestamps triés en LocalDateTime pour vérification
//        System.out.println("\nDates correspondantes (triées) :");
//        timestamps.stream().limit(1000000).forEach(ts -> {
//            LocalDateTime dateTime = Instant.ofEpochMilli(ts).atZone(ZoneId.systemDefault()).toLocalDateTime();
//            System.out.println(dateTime);
//        });
//    }
}