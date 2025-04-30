package machine;

import block.Block;
import formule.Formule;
import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Table(name = "machine")
public class Machine extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private String nom;
    @Column
    private int idFormule;
    Formule formule;

    public Machine() {
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

    public int getIdFormule() {
        return idFormule;
    }

    public void setIdFormule(int idFormule) {
        this.idFormule = idFormule;
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }

    public  Formule getFormule(Connection connection) throws Exception {
        Formule formule = new Formule();
        formule.setId(getIdFormule());
        formule.getById(connection);
        setFormule(formule);
        return formule;
    }
    public Machine getById(int id,Connection connection) throws Exception {
        Machine machine = new Machine();
        machine.setId(id);
        PreparedStatement preparedStatement = connection.prepareStatement("select * from machine where id=?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            machine.setNom(resultSet.getString("nom"));
            machine.setIdFormule(resultSet.getInt("idFormule"));
            machine.setFormule(getFormule(connection));
        }
        return machine;
    }
    public Block[] getBlocks(Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        preparedStatement =connection.prepareStatement("select * from block where idmachine=?");
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        List<Block> blocks = new ArrayList<Block>();
        while (resultSet.next()) {
            Block block = new Block();
            block.setId(resultSet.getInt("id"));
            block.setVolume(resultSet.getInt("volume"));
            block.setUnitPrice(resultSet.getDouble("unit_price"));
            block.setStatus(resultSet.getString("status"));
            block.setLongueur(resultSet.getInt("long"));
            block.setLargeur(resultSet.getInt("large"));
            block.setHauteur(resultSet.getInt("hauteur"));
            block.setIdSource(resultSet.getInt("idsource"));
            block.setDateAjout(resultSet.getTimestamp("date_ajout"));
            block.setIdMachine(resultSet.getInt("idmachine"));
            block.setMachine(this);
            blocks.add(block);
        }
        return blocks.toArray(new Block[blocks.size()]);
    }
}
