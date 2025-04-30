package block;

import stock.MouvementStock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Historique {
    List<MouvementStock> history = new ArrayList<MouvementStock>();
    HashMap<MouvementStock,Double> price = new HashMap<MouvementStock,Double>();
    double total;

    public Historique() {
    }

    public List<MouvementStock> getHistory() {
        return history;
    }

    public void setHistory(List<MouvementStock> history) {
        this.history = history;
    }

    public HashMap<MouvementStock, Double> getPrice() {
        return price;
    }

    public void setPrice(HashMap<MouvementStock, Double> price) {
        this.price = price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
