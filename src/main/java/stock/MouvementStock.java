package stock;

import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;
import usuel.Forme_usuel;
import usuel.PrixUsuel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
@Table(name = "MouvStock")
public class MouvementStock extends Dao {
    @Column(name = "id",isPK = true)
    private int id;
    @Column(name ="idBlock")
    private int idBlock;
    @Column(name = "idForme")
    private int idForme;
    @Column(name = "type_mouv")
    private String typeMouv;
    @Column(name = "qtt")
    private int qtt;
    @Column(name = "PRUnitaire")
    private double prUnitaire;
    @Column(name = "date_ajout")
    private Timestamp dateAjout;

    public MouvementStock() {
    }

    public double getPrUnitaire() {
        return prUnitaire;
    }

    public void setPrUnitaire(double prUnitaire) {
        this.prUnitaire = prUnitaire;
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

    public int getIdForme() {
        return idForme;
    }

    public void setIdForme(int idForme) {
        this.idForme = idForme;
    }

    public String getTypeMouv() {
        return typeMouv;
    }

    public void setTypeMouv(String typeMouv) {
        this.typeMouv = typeMouv;
    }

    public int getQtt() {
        return qtt;
    }

    public void setQtt(int qtt) {
        this.qtt = qtt;
    }

    public Timestamp getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Timestamp dateAjout) {
        this.dateAjout = dateAjout;
    }

    public double getMontant(Connection connection) throws Exception {
        Forme_usuel forme_usuel = new Forme_usuel();
        forme_usuel.setId(this.getIdForme());
        forme_usuel = forme_usuel.getById(connection);
        PrixUsuel prixUsuel = new PrixUsuel();
        prixUsuel = prixUsuel.getByIdForme(this.getIdForme(), connection);
        if (prixUsuel == null) {
            return 0;
        }
        return prixUsuel.getUnitPrice()*forme_usuel.getVolume()*this.getQtt();
    }

    public double getMontant(List<MouvementStock> list,Connection connection) throws Exception {
        double total = 0;
        for (MouvementStock mouvementStock : list) {
           total += mouvementStock.getMontant(connection);
        }
        return total;
    }
//    public void updatePRevient(Connection connection) throws Exception {
//        this.setUnitPrice(this.getUnitPrice());
//        PreparedStatement preparedStatement = null;
//        String sql = "update Block set unit_price ="+this.getUnitPrice()+" where id=" + id;
//        try{
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.executeUpdate();
//
//            preparedStatement.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
