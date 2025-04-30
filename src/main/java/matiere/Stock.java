package matiere;

import annexe.Utils;
import connexion.Base;
import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;

import java.util.*;
import java.sql.*;

@Table(name = "stock_matiere")
public class Stock extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idMatiere;
    @Column(name = "type_mouv")
    private String typeMouv;
    @Column(name = "qtt")
    private double quantity;
    @Column(name = "date_ajout")
    private Timestamp dateAjout;
    @Column( name = "PUnitaire")
    private double price;
    @Column( name = "restant")
    private double restant;

    public Stock(int idMatiere, String typeMouv, double quantity, Timestamp dateAjout, double price) {
        this.idMatiere = idMatiere;
        this.typeMouv = typeMouv;
        this.quantity = quantity;
        this.dateAjout = dateAjout;
        this.price = price;
    }

    public Stock() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public String getTypeMouv() {
        return typeMouv;
    }

    public void setTypeMouv(String typeMouv) {
        this.typeMouv = typeMouv;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Timestamp getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Timestamp dateAjout) {
        this.dateAjout = dateAjout;
    }

    public double getRestant() {
        return restant;
    }

    public void setRestant(double restant) {
        this.restant = restant;
    }

    public Stock[] getStock(Timestamp dateAjout,  Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from stock_matiere where restant > 0 and date_ajout <= ? and type_mouv = ? ";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setTimestamp(1, dateAjout);
        preparedStatement.setString(2, "entree");
        resultSet = preparedStatement.executeQuery();
        Stock[] stocks = new Stock[resultSet.getMetaData().getColumnCount()];
        int count = 0;
        while (resultSet.next()) {
            Stock stock = new Stock();
            stock.setId(resultSet.getInt(1));
            stock.setIdMatiere(resultSet.getInt(2));
            stock.setTypeMouv(resultSet.getString(3));
            stock.setQuantity(resultSet.getDouble(4));
            stock.setDateAjout(resultSet.getTimestamp(5));
            stock.setPrice(resultSet.getDouble(6));
            stock.setRestant(resultSet.getDouble(7));
            stocks[count] = stock;
            count++;
        }
        return stocks;
    }

    public List<Stock> getAllStock(  Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from stock_matiere where restant > 0 and type_mouv = 'entree' order by date_ajout asc ";
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        List<Stock> stocks = new ArrayList<Stock>();
        while (resultSet.next()) {
            Stock stock = new Stock();
            stock.setId(resultSet.getInt(1));
            stock.setIdMatiere(resultSet.getInt(2));
            stock.setTypeMouv(resultSet.getString(3));
            stock.setQuantity(resultSet.getDouble(4));
            stock.setDateAjout(resultSet.getTimestamp(5));
            stock.setPrice(resultSet.getDouble(6));
            stock.setRestant(resultSet.getDouble(7));
            stocks.add(stock) ;
        }
        return stocks;
    }

    public Stock[] getStockByIdMatiere(Timestamp dateAjout, int idMatiere,  Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from stock_matiere where restant > 0 and date_ajout <= ? and type_mouv = ? and idmatiere = ? ";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setTimestamp(1, dateAjout);
        preparedStatement.setString(2, "entree");
        preparedStatement.setInt(3, idMatiere);
        resultSet = preparedStatement.executeQuery();
        Stock[] stocks = new Stock[resultSet.getMetaData().getColumnCount()];
        int count = 0;
        while (resultSet.next()) {
            Stock stock = new Stock();
            stock.setId(resultSet.getInt(1));
            stock.setIdMatiere(resultSet.getInt(2));
            stock.setTypeMouv(resultSet.getString(3));
            stock.setQuantity(resultSet.getDouble(4));
            stock.setDateAjout(resultSet.getTimestamp(5));
            stock.setPrice(resultSet.getDouble(6));
            stock.setRestant(resultSet.getDouble(7));
            stocks[count] = stock;
            count++;
            }
        return stocks;
    }

    public void updateRestant(double used, Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        preparedStatement = connection.prepareStatement("update stock_matiere set restant = restant-? where id = "+id);
        preparedStatement.setDouble(1, used);
        preparedStatement.executeUpdate();
    }

    public static void updateStockEnBatch(List<Stock> stocks, Connection conn) throws SQLException {
        String sql = "UPDATE stock_matiere SET restant=? WHERE id=?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // Désactiver le commit automatique pour optimiser
            int count = 0;

            for (Stock stock : stocks) {
                pstmt.setDouble(1, stock.getRestant());
                pstmt.setInt(2, stock.getId());
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
    public void updateRestant(Connection conn) {
        Statement st = null;
        boolean newConnection = false;
        try{
            if (conn == null) {
                conn = Base.PsqlConnect();
                newConnection = true;
            }
                String sql = "UPDATE stock_matiere SET restant="+restant+" WHERE id="+id;
            st = conn.createStatement();

            st.executeUpdate(sql);
        }catch(SQLException e){
            System.out.println(e.getLocalizedMessage());
        }
        finally {
            try {
                if (st != null) st.close();
                if (conn != null && newConnection) conn.close();
            } catch (SQLException e) {e.printStackTrace();}
        }
    }

    public void  insertStock(Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        preparedStatement =  connection.prepareStatement("insert into stock_matiere (idmatiere, type_mouv, qtt, date_ajout, punitaire, restant) values(?,?,?,?,?,0)");
        preparedStatement.setInt(1, idMatiere);
        preparedStatement.setString(2, typeMouv);
        preparedStatement.setDouble(3, quantity);
        preparedStatement.setTimestamp(4, dateAjout);
        preparedStatement.setDouble(5, price);
        preparedStatement.executeUpdate();
    }

    public Double getPrixStockSortieOBJ (
            Stock [] stockMatieres, int idMatiere, boolean isEntrer, Timestamp date_ajout, double quantite){
        Double result = null;
        double prixTotalParStock = 0;
        double quantiteTotal = 0;

        for (Stock s : stockMatieres) {

            if (s.getIdMatiere() == idMatiere
                    && s.getRestant() > 0
                    && (s.getDateAjout().before(date_ajout)
                    || s.getDateAjout().equals(date_ajout))) {
                double reste = s.getRestant() - quantite;
                prixTotalParStock += s.getRestant() * s.getPrice();
                quantiteTotal += s.getRestant();
                if (reste > 0) {
                    s.setRestant(reste);
                    quantite=0;

                    break;
                }
                else {
                    s.setRestant(0);
                    quantite = reste*-1;
                }
            }
        }
        if(quantite == 0) {
            result = prixTotalParStock/quantiteTotal;
        }
        return result;
    }

    public Double getPrixStockSortieOBJGenerate (List<Stock> stockMatieres, int idMatiere, boolean isEntrer, Timestamp date_ajout, double quantite){
        Double result = null;
        double prixTotalParStock = 0;
        double quantiteTotal = 0;
        double prix_unitaire = 0;
        for (Stock s : stockMatieres) {
            if (s.getIdMatiere() == idMatiere) {
                if (s.getRestant() > 0 && (s.getDateAjout().before(date_ajout) || s.getDateAjout().equals(date_ajout))) {
                    double reste = s.getRestant() - quantite;
                    prixTotalParStock += s.getRestant() * s.getPrice();
                    quantiteTotal += s.getRestant();
                    if (reste > 0) {
                        s.setRestant(reste);
                        quantite = 0;
                        break;
                    }
                    else {
                        s.setRestant(0);
                        quantite = reste*-1;
                    }
                }
                else {
                    prix_unitaire = s.getPrice();
                }
            }
        }
        if(quantite == 0) {
            result = prixTotalParStock/quantiteTotal;
        }
        else {
            result = prix_unitaire;
            Stock new_stock = new Stock(idMatiere,"entree",quantite,date_ajout,prix_unitaire);
            new_stock.setDateAjout(date_ajout);
            stockMatieres.add(new_stock);
        }
        return result;
    }


}
