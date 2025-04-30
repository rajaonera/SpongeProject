package annexe;

import block.Block;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public  static  Double split(String valeur) throws Exception{
        String unite   ="";
        if (valeur.endsWith("cm")){
            valeur = valeur.substring(0, valeur.length()-2);
             unite = "cm";
             double value  =  Double.parseDouble(valeur);
             return value*0.01;
        }else if (valeur.endsWith("m")){
            valeur = valeur.substring(0, valeur.length()-1);
             unite = "m";
             return Double.valueOf(valeur) ;
        }else{
            return Double.valueOf(valeur);
        }
    }

    public  static  Double parse(String valeur) throws Exception{
        String unite   ="";
        if (valeur.endsWith("cm")){
            valeur = valeur.substring(0, valeur.length()-2);
            unite = "cm";
            double value  =  Double.parseDouble(valeur);
            return value*0.01;
        }
        else if (valeur.endsWith("m")){
            valeur = valeur.substring(0, valeur.length()-1);
            unite = "m";
            return Double.valueOf(valeur);

        }
        else{
            return Double.valueOf(valeur);
        }
    }
    public  static Timestamp strToTimeStamp(String date) throws Exception{
        LocalDateTime dt1 = LocalDateTime.parse(date);
        Timestamp ts1 = Timestamp.valueOf(dt1);
        System.out.println("ts1 = " + ts1);
        return ts1;
    }
    public static List<Block> readBlocksFromCSV(String filePath, Connection connection) throws IOException {
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
                int idsource = Integer.parseInt(parts[1]);
                int idmachine = Integer.parseInt(parts[2]);
                String status = "usefull";
                double longueur  =Double.parseDouble(parts[4]);
                double large = Double.parseDouble(parts[5]);
                double hauteur = Double.parseDouble(parts[6]);
                double unit_price = Double.parseDouble(parts[7]);
                Timestamp date_ajout = strToTimeStamp(parts[8]+"T"+generateRandomTime(8,18).toString());

                // Ajoute un bloc à la liste
                blocks.add(new Block( idsource,longueur,large,hauteur,date_ajout,
                        unit_price,status,idmachine,connection));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return blocks;
    }

//    public static LocalTime generateRandomTime(int startHour, int endHour) {
//        int randomHour = ThreadLocalRandom.current().nextInt(startHour, endHour + 1);
//        int randomMinute = ThreadLocalRandom.current().nextInt(0, 60);
//        int randomSecond = ThreadLocalRandom.current().nextInt(0, 60);
//        return LocalTime.of(randomHour, randomMinute, randomSecond);
//    }
    public  static boolean isParsableToDouble(String value){
        if (value == null || value.isEmpty() ){
            return false;
        }
        try{
            Double.parseDouble(value);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public  static Timestamp strTimestamp(String value) throws Exception{
        if (value == null || value.isEmpty() ){
            throw  new RuntimeException("date vide");
        }

        try{

            DateTimeFormatter formatter  =DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate  = LocalDate.parse(value,formatter);
            LocalDateTime localTime  = localDate.atStartOfDay();
            return Timestamp.valueOf(localTime);
        }
        catch (Exception e){
            throw new Exception(e.getLocalizedMessage()) ;
        }
    }

    public static double format(double value) {
        return Double.valueOf(String.format(Locale.US,"%.2f",value));
    }
    public static String write(double value) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value);
    }

    public static Timestamp[] generateWorkdayTimestamps(int startYear, int endYear,int limit,Random random) {
        List<Timestamp> timestamps = new ArrayList<>();
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        Long jourEntree  = ChronoUnit.DAYS.between(startDate, endDate);
        while (limit>0) {
            Long joursleatoire  = random.nextLong(jourEntree+1) ;
            LocalDate jourAleatoire = startDate.plusDays(joursleatoire);

            if (jourAleatoire.getDayOfWeek() != DayOfWeek.SATURDAY && jourAleatoire.getDayOfWeek() != DayOfWeek.SUNDAY) {
                LocalTime randomTime = generateRandomTime(8, 18);
                LocalDateTime dateTime = LocalDateTime.of(startDate, randomTime);
                long timestamp = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
                timestamps.add(new Timestamp(timestamp));
                limit-=1;
            }
        }

        Collections.sort(timestamps);

        return timestamps.toArray(new Timestamp[]{});
    }

    private static LocalTime generateRandomTime(int startHour, int endHour) {
        int randomHour = ThreadLocalRandom.current().nextInt(startHour, endHour + 1);
        int randomMinute = ThreadLocalRandom.current().nextInt(0, 60);
        int randomSecond = ThreadLocalRandom.current().nextInt(0, 60);
        return LocalTime.of(randomHour, randomMinute, randomSecond);
    }

}
