package machine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Resultat {
    int id;
    double count;
    double totalvolume ;
    double totalTheorique;
    double totalMachiniste;
    double eacart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getTotalvolume() {
        return totalvolume;
    }

    public void setTotalvolume(double totalvolume) {
        this.totalvolume = totalvolume;
    }

    public double getTotalTheorique() {
        return totalTheorique;
    }

    public void setTotalTheorique(double totalTheorique) {
        this.totalTheorique = totalTheorique;
    }

    public double getTotalMachiniste() {
        return totalMachiniste;
    }

    public void setTotalMachiniste(double totalMachiniste) {
        this.totalMachiniste = totalMachiniste;
    }

    public double getEacart() {
        return eacart;
    }

    public void setEacart(double eacart) {
        this.eacart = eacart;
    }
    public Resultat[] getAll(Connection connection) throws SQLException {
        List<Resultat> list = new ArrayList<Resultat>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        preparedStatement  = connection.prepareStatement("select idmachine,count(id)," +
                "        sum(volume)  as totalVolume," +
                "        sum(theorique) as theoriqueTotal ," +
                "        sum(machiniste) as machinisteTotal," +
                "        sum(theorique) - sum(machiniste) as ecart " +
                "from block " +
                " group by idmachine" +
                " order by  ecart");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Resultat resultat = new Resultat();
            resultat.setId(resultSet.getInt("idmachine"));
            resultat.setCount(resultSet.getDouble("count"));
            resultat.setTotalvolume(resultSet.getDouble("totalVolume"));
            resultat.setTotalTheorique(resultSet.getDouble("theoriqueTotal"));
            resultat.setTotalMachiniste(resultSet.getDouble("machinisteTotal"));
            resultat.setEacart(resultSet.getDouble("ecart"));
            list.add(resultat);
        }
        return list.toArray(new Resultat[]{});
    }
    public Resultat[] getAll(int annee,Connection connection) throws SQLException {
        List<Resultat> list = new ArrayList<Resultat>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        preparedStatement  = connection.prepareStatement("select idmachine,count(id)," +
                "        sum(volume)  as totalVolume," +
                "        sum(theorique) as theoriqueTotal ," +
                "        sum(machiniste) as machinisteTotal," +
                "        sum(theorique) - sum(machiniste) as ecart " +
                "from block " +
                "where extract(year from  date_ajout) = " +annee+
                " group by idmachine" +
                " order by  ecart");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Resultat resultat = new Resultat();
            resultat.setId(resultSet.getInt("idmachine"));
            resultat.setCount(resultSet.getDouble("count"));
            resultat.setTotalvolume(resultSet.getDouble("totalVolume"));
            resultat.setTotalTheorique(resultSet.getDouble("theoriqueTotal"));
            resultat.setTotalMachiniste(resultSet.getDouble("machinisteTotal"));
            resultat.setEacart(resultSet.getDouble("ecart"));
            list.add(resultat);
        }
        return list.toArray(new Resultat[]{});
    }
}
