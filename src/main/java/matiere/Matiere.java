package matiere;

import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Table(name = "matiere")
public class Matiere extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private String nom;
    @Column
    private int idUnite;
    Unite unite;
    Stock[] stocks;

    public Matiere() {}

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

    public int getIdUnite() {
        return idUnite;
    }

    public void setIdUnite(int idUnite) {
        this.idUnite = idUnite;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }
    public Stock[] getStock() {
        return stocks;
    }
    public void setStock(Stock[] stock) {
        this.stocks = stock;
    }

    public Unite getUnite(int id, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from unite where id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                unite = new Unite();
                unite.setId(resultSet.getInt("id"));
                unite.setNom(resultSet.getString("nom"));
                setIdUnite(unite.getId());
                setUnite(unite);
                return unite;
            }
        }catch (Exception e) {
            throw  new RuntimeException(e);
        }
        return null;
    }


    public Matiere getById(int id, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from matiere where id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Matiere matiere = new Matiere();
                matiere.setId(resultSet.getInt("id"));
                matiere.setNom(resultSet.getString("nom"));
                matiere.setIdUnite(resultSet.getInt("idUnite"));
                matiere.setUnite(getUnite(resultSet.getInt("idUnite"), connection));
                matiere.setStock(getStock( connection));
                return matiere;
            }
        }catch (Exception e) {
            throw  new RuntimeException(e);
        }
        return null;
    }


    public Stock[] getStock(Connection connection) {
        List<Stock> stockList = new ArrayList<Stock>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from stock_matiere where idmatiere = ? ";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,this.getId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Stock st = new Stock();
                st.setId(resultSet.getInt("id"));
                st.setIdMatiere(resultSet.getInt("idMatiere"));
                st.setQuantity(resultSet.getDouble("qtt"));
                st.setDateAjout(resultSet.getTimestamp("date_ajout"));
                st.setTypeMouv(resultSet.getString("type_mouv"));
                st.setPrice(resultSet.getDouble("punitaire"));
                stockList.add(st);
            }
        }catch (Exception e) {
            throw  new RuntimeException(e);
        }
        this.stocks = stockList.toArray(new Stock[stockList.size()]);
        return stockList.toArray(new Stock[stockList.size()]);
    }
}
