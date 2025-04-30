package block;

import stock.MouvementStock;
import usuel.Forme_usuel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation {
    private double totalUsed;
    private double remaining;
    private Map<Integer,Integer> surfaceCount;
    private Map<Integer,Double> revenuesPerSurface;
    private double montantTotal;
    public Simulation() {
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Simulation(double totalUsed, double remaining, Map<Integer, Integer> surfaceCount) {
        this.totalUsed = totalUsed;
        this.remaining = remaining;
        this.surfaceCount = surfaceCount;
    }

    public Map<Integer, Double> getRevenuesPerSurface() {
        return revenuesPerSurface;
    }

    public void setRevenuesPerSurface(Map<Integer, Double> revenuesPerSurface) {
        this.revenuesPerSurface = revenuesPerSurface;
        double montantTotal = 0;
        for (Map.Entry<Integer, Double> entry : revenuesPerSurface.entrySet()) {
            montantTotal += entry.getValue();
        }
        this.setMontantTotal(montantTotal);
    }

    public double getTotalUsed() {
        return totalUsed;
    }

    public void setTotalUsed(double totalUsed) {
        this.totalUsed = totalUsed;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public Map<Integer, Integer> getSurfaceCount() {
        return surfaceCount;
    }

    public void setSurfaceCount(Map<Integer, Integer> surfaceCount) {
        this.surfaceCount = surfaceCount;
    }
}
