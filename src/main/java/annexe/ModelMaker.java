package annexe;

import block.Block;
import connexion.Base;
import formule.DetailsFormule;
import formule.Formule;
import machine.Machine;
import matiere.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModelMaker {

    public void fabricationGenerate (List<Stock> stockMatieres, Block block, Formule formule, Connection con) throws Exception {
        DetailsFormule[] detailsFormule = formule.getDetailsFormule();
        double prix_theorique = 0;
        for (DetailsFormule df : detailsFormule) {
            double quantite = Utils.format(block.getVolume()*df.getQuantite());
            Double prix_unitaire = new Stock().getPrixStockSortieOBJGenerate(
                    stockMatieres,df.getIdMatiere(), true,block.getDateAjout(),quantite);
            if (prix_unitaire != null){
                prix_theorique += (Utils.format(block.getVolume()*df.getQuantite())*prix_unitaire);
            }
            else {
                throw new Exception("Stock de la Matiere "+df.getIdMatiere()+" est insuffisant pour BlockID:"+block.getId()+" date "+block.getDateAjout().toString());
            }
        }

        block.setTheorique(prix_theorique);
    }

    public void fabrication (List<Stock> stockMatieres, Block block, Formule formule, Connection con) throws Exception {
        DetailsFormule[] detailsFormule = formule.getDetailsFormule();
        double prix_theorique = 0;
//        int count = 0;
        for (DetailsFormule df : detailsFormule) {
            double quantite = Utils.format(block.getVolume()*df.getQuantite());
            Double prix_unitaire = new Stock().getPrixStockSortieOBJ(
                    stockMatieres.toArray(new Stock[]{}),df.getIdMatiere(), true,block.getDateAjout(),quantite);
            if (prix_unitaire != null){
//                System.out.println("pu "+df.getIdMatiere()+": "+prix_unitaire);
                prix_theorique += (Utils.format(block.getVolume()*df.getQuantite())*prix_unitaire);
            }
            else {
                throw new Exception("Stock de la Matiere "+df.getIdMatiere()+" est insuffisant pour BlockID:"+block.getId()+" date "+block.getDateAjout().toString());
            }
//            count++;
        }

        block.setTheorique(prix_theorique);
    }

    public void importFabrication(Formule formule,Block[] blocks, Connection con) throws Exception {
        boolean new_connection = false;
        if (con==null){
            con = Base.PsqlConnect();
            new_connection = true;
        }
        List<Stock> stockMatieres = new Stock().getAllStock(con);
        List<Stock> listStockExistant = new ArrayList<>();
        List<Stock> listStockNouveau = new ArrayList<>();
        int count = 0;
        for (Block block : blocks) {
            block.setMachiniste(block.getMachiniste());
            this.fabrication(stockMatieres,block,formule,con);
            System.out.println("Block "+count+" "+block.getLongueur()+"*"+block.getLargeur()+"*"+block.getHauteur()+" | "+block.getUnitPrice()+" | "+block.getTheorique()+" | "+block.getDateAjout());
            this.transfert(listStockExistant,listStockNouveau,stockMatieres);
            count++;
        }
        System.out.println("insertion des blocks  ...");
        insererBlocsEnBatch(blocks,con);
//        count =0;
        System.out.println("insertion des stocks restant ...");
        for (Stock stockMatiere : listStockExistant) {
            System.out.println(stockMatiere.getIdMatiere()+" : "+Utils.write(stockMatiere.getQuantity())+" | "+Utils.write(stockMatiere.getRestant())+ " | "+stockMatiere.getDateAjout().toString());
            stockMatiere.updateRestant(con);
            count++;
        }
        System.out.println("insertion des stocks epuisees ...");
        for (Stock stockMatiere : listStockNouveau) {
            stockMatiere.insertStock(con);
        }
        System.out.println("insertion fait ...");

        if (con!=null && new_connection){
            con.close();
        }
    }

    public void importFabricationGenerate(Formule formule,Block[] blocks, Connection con) throws Exception {
        boolean new_connection = false;
        if (con==null){
            con = Base.PsqlConnect();
            new_connection = true;
        }
        List<Stock> stockMatieres = new Stock().getAllStock(con);
        List<Stock> listStockExistant = new ArrayList<>();
        List<Stock> listStockNouveau = new ArrayList<>();
        for (Block block : blocks) {
            block.setMachiniste(block.getMachiniste());
            this.fabricationGenerate(stockMatieres,block,formule,con);
            this.transfert(listStockExistant,listStockNouveau,stockMatieres);
        }
        System.out.println("insertion des blocks  ...");
        insererBlocsEnBatch(blocks,con);
        System.out.println("insertion des stocks restant ...");
//        Stock.updateStockEnBatch(listStockExistant,con);
        System.out.println("insertion des stocks epuisees ...");
//        insererStockEnBatch(listStockNouveau,con);
        System.out.println("insertion fait ...");


        if (con!=null && new_connection){
            con.close();
        }
    }

    public static void insererBlocsEnBatch(Block[] blocs, Connection conn) throws SQLException {
        String sql = "insert into block (long, large, hauteur, date_ajout, status, volume, unit_price, idmachine, machiniste, theorique) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // Désactiver le commit automatique pour optimiser
            int count = 0;

            for (Block bloc : blocs) {
                pstmt.setDouble(1, bloc.getLongueur());
                pstmt.setDouble(2, bloc.getLargeur());
                pstmt.setDouble(3, bloc.getHauteur());
                pstmt.setTimestamp(4, bloc.getDateAjout());
                pstmt.setString(5, bloc.getStatus());
                pstmt.setDouble(6, bloc.getVolume());
                pstmt.setDouble(7, bloc.getUnitPrice());
                pstmt.setInt(8, bloc.getIdMachine());
                pstmt.setDouble(9,bloc.getMachiniste() );

                pstmt.setDouble(10, bloc.getTheorique());
                pstmt.addBatch();

                if (++count % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // Dernier batch
            conn.commit(); // Commit final
        }
        conn.setAutoCommit(true);
    }
//    public


    public void transfert(List<Stock> listStockExistant,List<Stock> listStockNouveau,List<Stock> stockMatieres) throws Exception {
        int count = 0;
        List<Stock> trades = new ArrayList<>();
        for (Stock s : stockMatieres) {
            if (s.getRestant() <= 0){
                if (s.getId() != 0){
                    listStockExistant.add(s);
                }
                else {
                    listStockNouveau.add(s);
                }
                trades.add(s);
            }
            count++;
        }
        for (Stock s : trades) {
            stockMatieres.remove(s);
        }
    }

    public static void insererStockEnBatch(List<Stock> stocks, Connection conn) throws SQLException {
        String sql = "insert into stock_matiere (idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) values(?,?,?,?,?,0)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // Désactiver le commit automatique pour optimiser
            int count = 0;

            for (Stock stock : stocks) {
                pstmt.setInt(1, stock.getIdMatiere());
                pstmt.setString(2, stock.getTypeMouv());
                pstmt.setDouble(3, stock.getQuantity());
                pstmt.setTimestamp(4, stock.getDateAjout());
                pstmt.setDouble(5, stock.getPrice());
                pstmt.addBatch();

                if (++count % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }
            pstmt.executeBatch(); // Dernier batch
            conn.commit(); // Commit final
        }
        conn.setAutoCommit(true);
    }

    public double getPrixRevientAleatoire (Random random, double result2, double result1) throws Exception{
        return random.nextDouble(result2 - result1 + 1) + result1;
    }

    public Block [] generBlocks(double limit,double pourcentage,Random random, Connection con) throws Exception {


        Machine[] machines = new Machine().read("",con).toArray(new Machine[]{});
        double prix_revient = new Block().getMoyennePrixRevient(con);
        int compt = 1;

        double nb = prix_revient * pourcentage;
        double moins_pourcentage = prix_revient - nb;
        double plus_pourcentage = prix_revient + nb;

        List<Block> blocks = new ArrayList<>();
        while (compt<=limit){
//            fonction manome liste 1 000 000 timestamps efa arranger croissant
            Timestamp[] listDate = Utils.generateWorkdayTimestamps(2022,2024,1000000,random);
            for (Timestamp date_fabrication : listDate){
                int indice = random.nextInt(machines.length);
                date_fabrication.toString().replace(" ","T");
//                fonction manome prix  de revient aleatoire
                double prix_pratique = Utils.format(this.getPrixRevientAleatoire(random,plus_pourcentage,moins_pourcentage));
                double longueur = random.nextDouble(25 - 20 + 1) + 20;
                double largeur = random.nextDouble(7 - 5 + 1) + 5;
                double hauteur = random.nextDouble(15 - 10 + 1) + 10;
                Block b = new Block(longueur,largeur,hauteur,date_fabrication,prix_pratique,machines[indice].getId());
                blocks.add(b);
                compt++;
            }
        }

        return blocks.toArray(new Block[]{});
    }

}
