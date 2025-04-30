package formule;

import connexion.Base;
import matiere.Matiere;
import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

@Table(name = "formule")
public class Formule  extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private String nom;
    DetailsFormule[] detailsFormule;
    HashMap<Integer,Double> details = new HashMap();

    public Formule() {
    }
    public Formule(int id) {
    this.id = id;
    }

    public HashMap<Integer, Double> getDetails() {
        return details;
    }
    public void addDetails(DetailsFormule detailsFormule)  throws Exception {
        Matiere matiere = new Matiere();
        details.put(detailsFormule.getIdMatiere(),detailsFormule.getQuantite());
    }
    public void setDetails(HashMap<Integer, Double> details) {
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public DetailsFormule[] getDetailsFormule() {
        return detailsFormule;
    }

    public void setDetailsFormule(DetailsFormule[] detailsFormule) {
        this.detailsFormule = detailsFormule;
    }
    public  void addDetails(int idMatiere,DetailsFormule detailsFormule) {
            details.put(idMatiere,detailsFormule.getQuantite());
    }
//    public DetailsFormule[] addDetailsFormule(DetailsFormule detailsFormule){
//        List<DetailsFormule> detailsFormuleList = new java.util.ArrayList<>(Arrays.stream(this.getDetailsFormule()).toList());
//        detailsFormuleList.add(detailsFormule);
//        this.setDetailsFormule(detailsFormuleList.toArray(new DetailsFormule[detailsFormuleList.size()]));
//        return this.getDetailsFormule();
//    }

    public DetailsFormule[] getDetailsFormule(Connection connection) throws Exception {
        List<DetailsFormule> detailsFormuleList = new java.util.ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from details_formule where idformule = ?";
//        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                DetailsFormule detailsFormule = new DetailsFormule();
                detailsFormule.setId(resultSet.getInt("id"));
                detailsFormule.setIdFormule(resultSet.getInt("idformule"));
                detailsFormule.setIdMatiere(resultSet.getInt("idmatiere"));
                detailsFormule.setQuantite(resultSet.getDouble("quantite"));
                detailsFormuleList.add(detailsFormule);
                addDetails(detailsFormule.getIdMatiere() , detailsFormule);
//                addDetailsFormule(detailsFormule);
            }
//        }catch (Exception e){
//            throw new Exception(e.getLocalizedMessage());
//        }
        this.setDetailsFormule(detailsFormuleList.toArray(new DetailsFormule[detailsFormuleList.size()]));
        return detailsFormuleList.toArray(new DetailsFormule[detailsFormuleList.size()]);
    }
    public  Formule getById(Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet  = null;
        String sql  = "select * from formule where id = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, this.getId());
        resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            Formule formule = new Formule();
            formule.setId(resultSet.getInt("id"));
            formule.setNom(resultSet.getString("nom"));
            formule.setDetailsFormule(this.getDetailsFormule(connection));
            return formule;
        }
        return null;
    }

    public Formule getLastFormule(Connection connection) throws Exception {
        Formule formule = new Formule();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from formule order by id desc limit 1";
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                formule.setId(resultSet.getInt("id"));
                formule.setNom(resultSet.getString("nom"));
                return formule;
            }
        }
        catch (Exception e){
            throw new Exception(e.getLocalizedMessage());
        }
        return null;
    }

}
