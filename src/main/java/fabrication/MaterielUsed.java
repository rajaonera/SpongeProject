package fabrication;

import matiere.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielUsed {
    private int idMatiere;
    private double qtt;
    int idmachine;
    double volume;

    public MaterielUsed(int idmachine, double volume) {
        this.idmachine = idmachine;
        this.volume = volume;
    }

    public int getIdmachine() {
        return idmachine;
    }

    public void setIdmachine(int idmachine) {
        this.idmachine = idmachine;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public MaterielUsed() {
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public double getQtt() {
        return qtt;
    }

    public void setQtt(double qtt) {
        this.qtt = qtt;
    }
    public static MaterielUsed[] getByIdMachine(int idmachine,double volume, Connection connection) throws SQLException {
        List<MaterielUsed> list = new ArrayList<MaterielUsed>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select idmatiere,\n" +
                "       (quantite * ?) as qttTotal\n" +
                "from details_formule \n" +
                "where idformule = (select idformule\n" +
                "                   from machine\n" +
                "                   where id = ?)";
        ps  =  connection.prepareStatement(sql);
        ps.setDouble(1, volume);
        ps.setInt(2, idmachine);
        rs = ps.executeQuery();
        int count  =0;
        while (rs.next()) {
            System.out.println("idMachine: "+ idmachine);
            System.out.println("volume: "+ volume);
            MaterielUsed f = new MaterielUsed();
            f.setIdMatiere(rs.getInt("idMatiere"));
            f.setQtt(rs.getDouble("qttTotal"));
            f.setVolume(volume);
            f.setIdmachine(idmachine);
            count++;
            list.add(f);
        }
        return list.toArray(new MaterielUsed[list.size()]);
    }

    public  double insertused(Stock[] stocks, Timestamp timestamp, Connection connection) throws Exception {
        double total = this.getQtt();
        double retour = 0;
        for (Stock stock : stocks) {
            if (total<stock.getRestant()){
                stock.updateRestant(total,connection);
                retour +=total*stock.getPrice();
                new Stock(this.getIdMatiere(),"sortie",total,timestamp,stock.getPrice()).insertStock(connection);
                break;
            }else {
                stock.updateRestant(stock.getRestant(),connection);
                retour +=total*stock.getPrice();
                total-=stock.getRestant();
                new Stock(this.getIdMatiere(),"sortie",this.getQtt(),timestamp,stock.getPrice()).insertStock(connection);
            }
            System.out.println("inserted");
        }
    return retour;

    }
//    public  double insertused(Stock[] stocks, Timestamp timestamp, Connection connection) throws Exception {
//        double total = this.getQtt();
//        double retour = 0;
//        for (Stock stock : stocks) {
//            if (total<stock.getRestant()){
//                stock.updateRestant(total);
//                retour +=total*stock.getPrice();
//                new Stock(this.getIdMatiere(),"sortie",total,timestamp,stock.getPrice()).insertStock(connection);
//                break;
//            }else {
//                stock.updateRestant(stock.getRestant());
//                retour +=total*stock.getPrice();
//                total-=stock.getRestant();
//                new Stock(this.getIdMatiere(),"sortie",this.getQtt(),timestamp,stock.getPrice()).insertStock(connection);
//            }
//            System.out.println("inserted");
//        }
//        return retour;
//
//    }
}
