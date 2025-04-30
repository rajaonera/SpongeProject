package matiere;

import java.sql.*;
import java.util.*;
import java.util.ArrayList;

public class Reste {
    private int idMatiere;
    private double reste;

    public Reste() {
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public double getReste() {
        return reste;
    }

    public void setReste(double reste) {
        this.reste = reste;
    }

    public static Reste getByIdMatiere(int idMatiere,Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Reste reste = new Reste();
        preparedStatement = connection.prepareStatement("select\n" +
                "    idmatiere,sum(\n" +
                "            CASE  when type_mouv = 'entree'\n" +
                "                then qtt when type_mouv  = 'sorite'\n" +
                "                    then  -qtt else  0 end ) as reste\n" +
                "from stock_matiere where idmatiere = ? group by idmatiere ");
        preparedStatement.setInt(1, idMatiere);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            reste.setReste(resultSet.getDouble("reste"));
        }
        return reste;
    }
    public static double getResteByIdMatiere(int idMatiere, Timestamp date_ajout, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        preparedStatement = connection.prepareStatement("select\n" +
                "    idmatiere,sum(\n" +
                "            CASE  when type_mouv = 'entree'\n" +
                "                then qtt when type_mouv  = 'sorite'\n" +
                "                    then  -qtt else  0 end ) as reste\n" +
                "from stock_matiere where idmatiere = "+idMatiere+" and date_ajout <= ? group by idmatiere ");
        preparedStatement.setTimestamp(1, date_ajout);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
           return   resultSet.getDouble("reste");
        }else return 0;
    }

    public static Reste[] read(Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Reste> resteList = new ArrayList<Reste>();
        preparedStatement = connection.prepareStatement(
                "select\n" +
                "    idmatiere,sum(\n" +
                "            CASE  when type_mouv = 'entree'\n" +
                "                then qtt when type_mouv  = 'sorite'\n" +
                "                    then  -qtt else  0 end ) as reste\n" +
                "from stock_matiere group by idmatiere "
        );
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Reste reste = new Reste();
            reste.setIdMatiere(resultSet.getInt("idmatiere"));
            reste.setReste(resultSet.getDouble("reste"));
            resteList.add(reste);
        }
        return resteList.toArray(new Reste[resteList.size()]);
    }
    public static Reste[] readByDate( java.sql.Timestamp time,Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Reste> resteList = new ArrayList<Reste>();
        preparedStatement = connection.prepareStatement(
                "select\n" +
                "    idmatiere,sum(\n" +
                "            CASE  when type_mouv = 'entree'\n" +
                "                then qtt when type_mouv  = 'sorite'\n" +
                "                    then  -qtt else  0 end ) as reste\n" +
                "from stock_matiere where date_ajout <= ? group by idmatiere ; "
        );
        preparedStatement.setTimestamp(1, time);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            Reste reste = new Reste();
            reste.setIdMatiere(resultSet.getInt("idmatiere"));
            reste.setReste(resultSet.getDouble("reste"));
            resteList.add(reste);
        }
        return resteList.toArray(new Reste[resteList.size()]);
    }
    public  double getLastPriceBeforeDate(int idMatiere, Timestamp date_ajout, Connection connection) throws Exception {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        preparedStatement = connection.prepareStatement("select punitaire from stock_matiere where idmatiere = ? and date_ajout <= ? group by id order by id desc limit 1");
        preparedStatement.setInt(1, idMatiere);
        preparedStatement.setTimestamp(2, date_ajout);
        System.out.println(preparedStatement.toString());
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return   resultSet.getDouble("punitaire");
        }
        return 0;
    }
}
