package matiere;

import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;
@Table(name = "unite")
public class Unite extends Dao {
    @Column(isPK = true)
    private int id;
    @Column
    private String nom;

    public Unite() {
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
}
