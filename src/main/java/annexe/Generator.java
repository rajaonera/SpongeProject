package annexe;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Generator {

    // Fonction pour générer les données du bloc avec marges de manière optimisée
    public static BlocEponge[] genererBlocs(int nombreDeBlocs, double volumeInitial, double prixReelInitial, double prixTheoriqueInitial,
                                    double margeVolume, double margePrixReel, double margePrixTheorique) {
        BlocEponge[] blocs = new BlocEponge[nombreDeBlocs];

        // Executor pour paralléliser la génération
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        AtomicInteger counter = new AtomicInteger();

        // Lancer les tâches de génération en parallèle
        for (int i = 0; i < nombreDeBlocs; i++) {
            int finalI = i;
            executorService.submit(() -> {
                BlocEponge bloc = genererBloc(volumeInitial, prixReelInitial, prixTheoriqueInitial,
                        margeVolume, margePrixReel, margePrixTheorique);
                blocs[finalI] = bloc;
                // Utiliser un compteur atomique pour compter les blocs générés (par exemple pour affichage ou vérification)
                if (counter.incrementAndGet() % 1000 == 0) {
                    System.out.println(counter.get() + " blocs générés...");
                    System.out.println(" bloc reel : "+ bloc.getPrixReel());
                    System.out.println(" bloc theorique : "+ bloc.getPrixTheorique());
                }
            });
        }

        // Fermer l'executor après traitement
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        return blocs;
    }

    // Fonction pour générer un bloc avec marges de manière rapide
    private static BlocEponge genererBloc(double volumeInitial, double prixReelInitial, double prixTheoriqueInitial,
                                          double margeVolume, double margePrixReel, double margePrixTheorique) {
        // Utiliser ThreadLocalRandom pour de meilleures performances en multithreading
        double volumeMax = volumeInitial * (1 + margeVolume / 100);
        double volumeMin = volumeInitial * (1 - margeVolume / 100);

        double prixReelMax = prixReelInitial * (1 + margePrixReel / 100);
        double prixReelMin = prixReelInitial * (1 - margePrixReel / 100);

        double prixTheoriqueMax = prixTheoriqueInitial * (1 + margePrixTheorique / 100);
        double prixTheoriqueMin = prixTheoriqueInitial * (1 - margePrixTheorique / 100);

        // Générer des valeurs aléatoires dans les intervalles
        double volume = ThreadLocalRandom.current().nextDouble(volumeMin, volumeMax);
        double prixReel = ThreadLocalRandom.current().nextDouble(prixReelMin, prixReelMax);
        double prixTheorique = ThreadLocalRandom.current().nextDouble(prixTheoriqueMin, prixTheoriqueMax);

        // Créer et retourner l'objet BlocEponge avec les valeurs générées
        return new BlocEponge(volume, prixReel, prixTheorique);
    }

    // Classe représentant un Bloc d'Eponge
    public static class BlocEponge {
        private final double volume;
        private final double prixReel;
        private final double prixTheorique;

        public BlocEponge(double volume, double prixReel, double prixTheorique) {
            this.volume = volume;
            this.prixReel = prixReel;
            this.prixTheorique = prixTheorique;
        }

        public double getVolume() {
            return volume;
        }

        public double getPrixReel() {
            return prixReel;
        }

        public double getPrixTheorique() {
            return prixTheorique;
        }

        @Override
        public String toString() {
            return "BlocEponge [Volume=" + volume + ", PrixReel=" + prixReel + ", PrixTheorique=" + prixTheorique + "]";
        }
    }
    // Fonction pour calculer la somme des prix théoriques et réels à partir d'une liste de blocs
    public static double[] sommePrix(List<BlocEponge> blocs) {
        double sommePrixReel = 0;
        double sommePrixTheorique = 0;

        // Utilisation d'un stream pour additionner les prix rapidement
        for (BlocEponge bloc : blocs) {
            sommePrixReel += bloc.getPrixReel()*bloc.getVolume();
            sommePrixTheorique += bloc.getPrixTheorique()*bloc.getVolume();
        }

        return new double[]{sommePrixReel, sommePrixTheorique};
    }
//
//    public static void main(String[] args) {
//        // Exemple d'utilisation avec des valeurs initiales et des marges
//        double volumeInitial = 50.0;
//        double prixReelInitial = 3000.0;
//        double prixTheoriqueInitial = 3000.0;
//
//        double margeVolume = 1.0;  // 10% de marge sur le volume
//        double margePrixReel = 10.0; // 5% de marge sur le prix réel
//        double margePrixTheorique =10.0; // 8% de marge sur le prix théorique
//
//        int  nombreDeBlocs = 1000000; // Exemple pour générer 1 million de blocs
//
//        long startTime = System.currentTimeMillis();
//
//        // Générer les blocs en parallèle de manière optimisée
//        BlocEponge[] blocEponges = genererBlocs(nombreDeBlocs, volumeInitial, prixReelInitial, prixTheoriqueInitial,
//                margeVolume, margePrixReel, margePrixTheorique);
//        System.out.println(Arrays.toString(sommePrix(Arrays.stream(blocEponges).toList())));
//        long endTime = System.currentTimeMillis();
//        System.out.println("Temps de génération des blocs : " + (endTime - startTime) + " ms");
//    }
}
