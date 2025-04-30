package formule;

import matiere.Matiere;
import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;

import java.util.HashMap;

@Table(name = "details_formule")
public class DetailsFormule extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private int idFormule;
    @Column
    private int idMatiere;
    @Column
    private double quantite;

    public DetailsFormule() {
    }
    public DetailsFormule(int idFormule, int idMatiere, double quantite) {
        this.idFormule = idFormule;
        this.idMatiere = idMatiere;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFormule() {
        return idFormule;
    }

    public void setIdFormule(int idFormule) {
        this.idFormule = idFormule;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }
}
