package annexe;

import block.Block;
import connexion.Base;
import formule.Formule;
import part3.OptimizedBlockGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class BlockGeneratorOBJ {
    public static Block[] readBlocksFromCSV(String filePath, Connection connection) throws IOException {
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
                String date_ajout = (parts[0]);
                String longueur  =(parts[1]);
                String large = (parts[2]);
                String hauteur = (parts[3]);
                String unit_price = (parts[4]);
                String idmachine = (parts[5]);

                // Ajoute un bloc à la liste
                System.out.println("ligne :"+ c );
                blocks.add(new Block(longueur,large,hauteur,date_ajout,
                        unit_price,idmachine));
                c++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        blocks.sort((b1, b2) ->
                b1.getDateAjout().compareTo(b2.getDateAjout()));

        return blocks.toArray(new Block[blocks.size()]);
    }

    /**
     * Exemple d'utilisation.
     */
    public static void main(String[] args) {
        String filePath = "Data 1M.csv";

        try {
            // Lire les données depuis le fichier CSV
            long startRead = System.currentTimeMillis();
            Connection connection = Base.PsqlConnect();
            Block[] blocks = readBlocksFromCSV(filePath,connection);
            Formule formule  = new Formule(1).getById(connection);

            ModelMaker modelMaker  = new ModelMaker();
//            Block[] blocks = new OptimizedBlockGenerator().generBlocks(1000000.0,10,connection);

            modelMaker.importFabrication(formule,blocks,connection);
            long endRead = System.currentTimeMillis();
//            System.out.println("Données lues depuis le CSV : " + blocks.length + " blocs");
            System.out.println("Temps pour lire : " + (endRead - startRead) + " ms");

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
