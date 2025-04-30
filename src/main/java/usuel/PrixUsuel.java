package usuel;

import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

@Table(name = "prix_usuel")
public class PrixUsuel  extends Dao {
    @Column(name = "id", isPK = true)
    private int id;
    @Column(name = "idForme")
    int idForme;
    @Column(name = "unit_price")
    double unitPrice;
//  ('vente','achat')
    @Column(name = "type_price")
    String typePrice;
    @Column(name = "date_modif")
    Date dateModif;

    public PrixUsuel(int id, int idForme, double unitPrice, String typePrice, Date dateModif) {
        this.id = id;
        this.idForme = idForme;
        this.unitPrice = unitPrice;
        this.typePrice = typePrice;
        this.dateModif = dateModif;
    }

    public PrixUsuel() {
    }

    public PrixUsuel(int idForme, double unitPrice, String typePrice, Date dateModif) {
        this.idForme = idForme;
        this.unitPrice = unitPrice;
        this.typePrice = typePrice;
        this.dateModif = dateModif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdForme() {
        return idForme;
    }

    public void setIdForme(int idForme) {
        this.idForme = idForme;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTypePrice() {
        return typePrice;
    }

    public void setTypePrice(String typePrice) {
        this.typePrice = typePrice;
    }

    public Date getDateModif() {
        return dateModif;
    }

    public void setDateModif(Date dateModif) {
        this.dateModif = dateModif;
    }
    public PrixUsuel getByIdForme(int idForme, Connection connection) {
        List<Object> objectList = this.read(" where idForme ="+idForme,connection);
        if (objectList.isEmpty()) {
            return null;
        }
        return (PrixUsuel) objectList.get(0);
    }
    public PrixUsuel getByIdFormelast(int idForme, Connection connection) {
        List<Object> objectList = this.read(" where idForme ="+idForme+" order by date_modif desc limit 1",connection);
        if (objectList.isEmpty()) {
            return null;
        }
        return (PrixUsuel) objectList.get(0);
    }
}
