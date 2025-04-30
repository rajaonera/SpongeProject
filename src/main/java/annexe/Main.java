package annexe;

import block.Block;
import connexion.Base;
import formule.Formule;
import part3.OptimizedBlockGenerator;

import java.io.IOException;
import java.sql.Connection;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {

        try {
            long startRead = System.currentTimeMillis();
            Connection connection = Base.PsqlConnect();
            Formule formule  = new Formule(1).getById(connection);
            Random random =new Random();
            ModelMaker modelMaker  = new ModelMaker();
            Block[] blocks = modelMaker.generBlocks(1000000.0,10,random,connection);
            modelMaker.importFabricationGenerate(formule,blocks,connection);
            long endRead = System.currentTimeMillis();
            System.out.println("Temps pour lire : " + (endRead - startRead) + " ms");

        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
