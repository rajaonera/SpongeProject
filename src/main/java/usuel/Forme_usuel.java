package usuel;

import block.Block;
import connexion.Base;
import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Date;

@Table(name = "forme_usuel")
public class Forme_usuel extends Dao {
    @Column(name = "id", isPK = true)
    private int id;
    @Column(name = "nom")
    String nom;
    @Column(name = "long")
    double longueur;
    @Column(name = "large")
    double largeur;
    @Column(name = "hauteur")
    double hauteur;
   @Column(name = "volume")
    double volume;
    @Column(name = "date_ajout")
    Date dateAjout;
    PrixUsuel prixUsuel;

    public Forme_usuel() {
    }

    public Forme_usuel(String nom, double longueur, double largeur, double hauteur, double volume, Date dateAjout) {
        this.nom = nom;
        this.longueur = longueur;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.volume = volume;
        this.dateAjout = dateAjout;
    }

    public Forme_usuel(int id, String nom, double longueur, double largeur, double hauteur, double volume, Date dateAjout) {
        this.id = id;
        this.nom = nom;
        this.longueur = longueur;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.volume = volume;
        this.dateAjout = dateAjout;
    }

    public PrixUsuel getPrixUsuel() {
        return prixUsuel;
    }

    public void setPrixUsuel(PrixUsuel prixUsuel) {
        this.prixUsuel = prixUsuel;
    }
    public void setPrixUsuel(Connection connection) {
        PrixUsuel prixUsuel = new PrixUsuel();
        prixUsuel =  prixUsuel.getByIdFormelast(this.getId(),connection);
        this.setPrixUsuel(prixUsuel);
        System.out.println("new PUsuel: " + this.getPrixUsuel().getUnitPrice());
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setVolume() {
        this.volume = longueur*largeur*hauteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongueur() {
        return longueur;
    }

    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public Forme_usuel getById(Connection connection) throws Exception {
        if (connection==null){
            connection = Base.PsqlConnect();
        }
        List<Object> list =  this.read(" where id = "+ this.getId(),connection );
        if (list.isEmpty()){
            return null;
        }
        Forme_usuel forme = (Forme_usuel) list.get(0);
        forme.setPrixUsuel(connection);
        return forme;
    }
    public List<Forme_usuel> getAll(Connection connection) throws Exception {
        if (connection==null){
            connection = Base.PsqlConnect();
        }
        List<Object> list =  this.read("",connection );
        List<Forme_usuel> formes = new ArrayList<Forme_usuel>();
        if (list.isEmpty()){
            return null;
        }
        for (Object object : list) {
            Forme_usuel forme = (Forme_usuel) object;
            forme.setPrixUsuel(connection);
            formes.add(forme);
        }
        return formes;
    }
    public Forme_usuel findById(Connection connection) throws Exception{
        try{
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            String sql = "select * from forme_usuel where id = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, this.getId());
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                this.setId(resultSet.getInt("id"));
                this.setVolume(resultSet.getInt("volume"));
                this.setLongueur(resultSet.getInt("long"));
                this.setLargeur(resultSet.getInt("large"));
                this.setHauteur(resultSet.getInt("hauteur"));
                this.setDateAjout(resultSet.getDate("date_ajout"));
                this.setNom(resultSet.getString("nom"));
//                this.setPrixUsuel(connection);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return this;
    }
}
