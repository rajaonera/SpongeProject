package fabrication;

import java.sql.*;
import java.util.*;
import java.util.ArrayList;

public class Fabrication{
    int id;
    int idBlock;
    int idMatiere;
    double punitaire;
    double quantite;
    Timestamp date_ajout;

    public Fabrication() {
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public double getPunitaire() {
        return punitaire;
    }

    public void setPunitaire(double punitaire) {
        this.punitaire = punitaire;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public Timestamp getDate_ajout() {
        return date_ajout;
    }

    public void setDate_ajout(Timestamp date_ajout) {
        this.date_ajout = date_ajout;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdBlock() {
        return idBlock;
    }

    public void setIdBlock(int idBlock) {
        this.idBlock = idBlock;
    }
    public Fabrication[] read(Connection conn) throws SQLException {
        List<Fabrication> list = new ArrayList<Fabrication>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "select * from fabrication";
        ps = conn.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Fabrication f = new Fabrication();
            f.setId(rs.getInt("id"));
            f.setIdBlock(rs.getInt("idBlock"));
            f.setIdMatiere(rs.getInt("idMatiere"));
            f.setPunitaire(rs.getDouble("punitaire"));
            f.setQuantite(rs.getDouble("quantite"));
            f.setDate_ajout(rs.getTimestamp("date_ajout"));
            list.add(f);
        }
        return list.toArray(new Fabrication[list.size()]);
    }

    public  void  insert(Connection connection) throws  Exception{
        String sql  = "insert into fabrication (idblock,idblock,idmatiere,punitaire,date_ajout,qtt) values (?,?,?,?,?,?)";
        PreparedStatement ps =  connection.prepareStatement(sql);
        ps.setInt(1, idBlock);
        ps.setInt(2, idBlock);
        ps.setInt(3, idMatiere);
        ps.setDouble(4, punitaire);
        ps.setDouble(5, quantite);
        ps.setTimestamp(6, date_ajout);
        ps.executeUpdate();
        System.out.println("Fabrication inserted successfully");
    }

}
