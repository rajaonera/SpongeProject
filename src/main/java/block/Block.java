package block;

import annexe.Utils;
import connexion.Base;
import fabrication.Fabrication;
import fabrication.MaterielUsed;
import machine.Machine;
import matiere.Reste;
import matiere.Stock;
import mg.dao.annotation.Column;
import mg.dao.annotation.Table;
import mg.dao.utils.Dao;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import stock.MouvementStock;
import usuel.Forme_usuel;

import java.sql.*;
import java.util.*;

@Table(name = "block")
public class Block extends Dao {
    @Column(name = "id",isPK = true)
    int id;
    @Column(name = "idSource")
    int idSource  = 0;
    @Column(name = "long")
    double longueur;
    @Column(name = "large")
    double largeur;
    @Column(name = "hauteur")
    double hauteur;
    @Column(name = "date_ajout")
    Timestamp dateAjout;
    @Column(name = "unit_price")
    double unitPrice;
    @Column(name = "volume")
    double volume;
    @Column(name = "status")
    String status  = "usefull";
    @Column
    int idMachine;
    @Column
            double theorique;
    @Column
            double machiniste;

    public Block(String longueur, String largeur, String hauteur, String dateAjout, String unitPrice, String idMachine, String machiniste) throws Exception {
        this.idSource = 0;
        this.setLongueur(longueur);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setDateAjout(dateAjout);
        this.setUnitPrice(unitPrice);
        this.setIdMachine(idMachine, "M");
        this.volume = getLongueur() * getLargeur() * getHauteur();
        this.status = "usefull";
        this.setMachiniste(Utils.parse(unitPrice));
    }

    public double getTheorique() {
        return theorique;
    }

    public void setTheorique(double theorique) {
        this.theorique = theorique;
    }

    public double getMachiniste() {
        return machiniste;
    }

    public void setMachiniste(double machiniste) {
        this.machiniste = machiniste;
    }

    Machine machine;
    Historique historique;
    Simulation simulationPrix = new Simulation();
    Simulation simulationReste =  new Simulation();
    Block parent ;
    Block child ;
    Fabrication[] fabrications;
    MaterielUsed[] materielUsed;

    public Block() {
    }

    @Override
    public String toString() {
        return "Block{id=" + id + ", volume=" + volume + ", prixReel=" + machiniste + ", prixTheorique=" + theorique + "}";
    }

    public Block(int idSource, double longueur, double largeur, double hauteur, Timestamp dateAjout,
                 double unitPrice, String status, int idMachine ,Connection connection) throws Exception {
        this.idSource = idSource;
        this.longueur = longueur;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.dateAjout = dateAjout;
        this.unitPrice = unitPrice;
        this.volume = longueur*largeur*hauteur;
        this.status = status;
        this.idMachine = idMachine;


        this.machine = new Machine().getById(idMachine,connection);
        this.materielUsed =this.getUsed(connection);
        if (!checkStock(connection, this.getMaterielUsed())){
            System.out.println("Erreur de stock blockId :" + id + " -- date_ajout : " + dateAjout);
            throw new Exception("stock insuffisant  blockId :" + id + " -- date_ajout : " + dateAjout);
        }else {
            theorique =  this.insertUsed(connection);
            machiniste = unitPrice*volume;
            this.insert(connection);
            System.out.println("succes reel :"+ unitPrice*volume);
            System.out.println("theorique :"+ theorique);
        }

    }

    public Block( String longueur, String largeur, String hauteur, String dateAjout,
                  String unitPrice, String idMachine ,Connection connection) throws Exception {
        this.idSource = 0;
        this.setLongueur(longueur);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setDateAjout(dateAjout);
        this.setUnitPrice( unitPrice);
        this.setIdMachine(idMachine,"M");
        this.volume = getLongueur()*getLargeur()*getHauteur();
        this.status = "usefull";
        this.setMachiniste(Utils.parse(unitPrice));

        this.machine = new Machine().getById(getIdMachine(),connection);
        this.materielUsed =this.getUsed(connection);
        if (!checkStock(connection, this.getMaterielUsed())){
            System.out.println("Erreur de stock blockId :" + id + " -- date_ajout : " + dateAjout);
            throw new Exception("stock insuffisant  blockId :" + id + " -- date_ajout : " + dateAjout);
        }else {
            theorique =  this.insertUsed(connection);

            this.insert(connection);
            System.out.println("succes reel :"+ getUnitPrice()*volume);
            System.out.println("theorique :"+ theorique);
        }

    }

    public Block( String longueur, String largeur, String hauteur, String dateAjout,
                  String unitPrice, String idMachine ) throws Exception {
        this.idSource = 0;
        this.setLongueur(longueur);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setDateAjout(dateAjout);
        this.setUnitPrice(unitPrice);
        this.setIdMachine(idMachine, "M");
        this.volume = getLongueur() * getLargeur() * getHauteur();
        this.status = "usefull";
        this.setMachiniste(Utils.parse(unitPrice));
    }

    public Block( double longueur, double largeur, double hauteur, String dateAjout,
                  double unitPrice, int idMachine ) throws Exception {
        this.idSource = 0;
        this.setLongueur(longueur);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setDateAjout(dateAjout);
        this.setUnitPrice(unitPrice);
        this.setIdMachine(idMachine);
        this.volume = getLongueur() * getLargeur() * getHauteur();
        this.status = "usefull";
        this.setMachiniste(unitPrice);
    }

    public Block( double longueur, double largeur, double hauteur, Timestamp dateAjout,
                  double unitPrice, int idMachine ) throws Exception {
        this.idSource = 0;
        this.setLongueur(longueur);
        this.setLargeur(largeur);
        this.setHauteur(hauteur);
        this.setDateAjout(dateAjout);
        this.setUnitPrice(unitPrice);
        this.setIdMachine(idMachine);
        this.volume = getLongueur() * getLargeur() * getHauteur();
        this.status = "usefull";
        this.setMachiniste(unitPrice);
    }

    public double getMoyennePrixRevient (Connection con) throws Exception{
        Block [] blocks = new Block().read(" ORDER BY id ASC LIMIT 3",con).toArray(new Block[]{});
        double somme = 0;
        for (Block block : blocks) {
            somme+=block.getMachiniste();
        }
        double moyenne = 0;
        if (blocks.length > 0){
            moyenne = somme/blocks.length;
        }
        return moyenne;
    }

    //
//    public Block(int idSource, String longueur, double largeur, double hauteur, Timestamp dateAjout,
//                 double unitPrice, String status, int idMachine ,Connection connection,Stock[] stocks) throws Exception {
//        this.idSource = idSource;
//        this.longueur = longueur;
//        this.largeur = largeur;
//        this.hauteur = hauteur;
//        this.dateAjout = dateAjout;
//        this.unitPrice = unitPrice;
//        this.volume = longueur*largeur*hauteur;
//        this.status = status;
//        this.idMachine = idMachine;
//
//
//        this.machine = new Machine().getById(idMachine,connection);
//        this.materielUsed =this.getUsed(connection);
//        if (!checkStock(connection, this.getMaterielUsed())){
//            System.out.println("Erreur de stock blockId :" + id + " -- date_ajout : " + dateAjout);
//            throw new Exception("stock insuffisant  blockId :" + id + " -- date_ajout : " + dateAjout);
//        }else {
//            theorique =  this.insertUsed(stocks,connection);
//            machiniste = unitPrice*volume;
//            this.insert(connection);
//            System.out.println("succes reel :"+ unitPrice*volume);
//            System.out.println("theorique :"+ theorique);
//        }
//
//    }

    public void setIdMachine(String idMachine,String split) throws Exception {
        try{
            this.idMachine = Integer.parseInt(idMachine.split(split)[1]);
        }catch (Exception e) {
            throw new Exception("idmachine invalide");
        }
    }
    public void setUnitPrice(String unitPrice) {
        if (Utils.isParsableToDouble(unitPrice)){
            this.unitPrice = Double.parseDouble(unitPrice);
        }
        else throw new NumberFormatException("prix de revient invalide");
    }
    public void   setLongueur(String longueur) throws Exception{
        if (Utils.isParsableToDouble(longueur)){
            this.longueur = Double.parseDouble(longueur);
        }
        else throw new Exception("longueur invalide");
    }
    public void   setLargeur(String largeur) throws Exception{
        if (Utils.isParsableToDouble(largeur)){
            this.largeur = Double.parseDouble(largeur);
        }
        else throw new Exception("largeur invalide");
    }
    public void setHauteur(String hauteur) throws Exception{
        if (Utils.isParsableToDouble(hauteur)){
            this.hauteur = Double.parseDouble(hauteur);
        }else throw new Exception("hauteur invalide");
    }
    public void  setDateAjout(String dateAjout) throws Exception{
        this.dateAjout = Utils.strTimestamp(dateAjout);
    }


    private void insert(Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        preparedStatement  = connection.prepareStatement(
                "insert into block " +
                        "(long, large, hauteur, date_ajout, status, volume, unit_price, idmachine, machiniste, theorique) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)");
        preparedStatement.setDouble(1, longueur);
        preparedStatement.setDouble(2, largeur);
        preparedStatement.setDouble(3, hauteur);
        preparedStatement.setTimestamp(4, dateAjout);
        preparedStatement.setString(5, status);
        preparedStatement.setDouble(6, volume);
        preparedStatement.setDouble(7, unitPrice);
        preparedStatement.setInt(8, idMachine);
        preparedStatement.setDouble(9, machiniste);
        preparedStatement.setDouble(10, theorique);
        preparedStatement.executeUpdate();
    }

    public MaterielUsed[] getMaterielUsed() {
        return materielUsed;
    }

    public void setMaterielUsed(MaterielUsed[] materielUsed) {
        this.materielUsed = materielUsed;
    }

    public Fabrication[] getFabrications() {
        return fabrications;
    }

    public void setFabrications(Fabrication[] fabrications) {
        this.fabrications = fabrications;
    }

    public int getIdMachine() {
        return idMachine;
    }

    public void setIdMachine(int idMachine) {
        this.idMachine = idMachine;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Block getParent() {
        return parent;
    }

    public void setParent(Block parent) {
        this.parent = parent;
    }

    public Block getChild() {
        return child;
    }

    public void setChild(Block child) {
        this.child = child;
    }

    public Simulation getSimulationReste() {
        return simulationReste;
    }

    public void setSimulationReste(Simulation simulationReste) {
        this.simulationReste = simulationReste;
    }

    public Simulation getSimulationPrix() {
        return simulationPrix;
    }

    public void setSimulationPrix(Simulation simulationPrix) {
        this.simulationPrix = simulationPrix;
    }

    public Historique getHistorique() {
        return historique;
    }

    public void setHistorique(Historique historique) {
        this.historique = historique;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSource() {
        return idSource;
    }

    public void setIdSource(int idSource) {
        this.idSource = idSource;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) throws Exception {
//        if (unitPrice < 0) {
            this.unitPrice = unitPrice;
//        }
//        else {
//            throw new IllegalArgumentException("L'unit price n'existe pas");
//        }
    }

    public double getLongueur() {
        return longueur;
    }

    public void setLongueur(double longueur) throws Exception {
        if(longueur > 0){
            this.longueur = longueur;
        }
        else {
            throw  new Exception("longueur non valide");
        }
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) throws Exception {
        if(largeur > 0){
            this.largeur = largeur;
        }
        else {
            throw new Exception("largeur non valide");
        }
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) throws  Exception {
        if (hauteur > 0){
            this.hauteur = hauteur;
        }
        else{
            throw new Exception("hauteur non valide");
        }
    }

    public Timestamp getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Timestamp dateAjout) {
        this.dateAjout = dateAjout;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setVolume() throws  Exception{
        if (longueur*largeur*hauteur> 0){
            this.volume = longueur * largeur * hauteur;
        }
        else {
            throw  new Exception("volume invalide");
        }
    }
    public double getPrixRevient(Connection connection)throws Exception{
        if(this.getIdSource()!= 0){
            Block block =  (Block)this.read(" where idsource = " + this.getIdSource(),connection);
            double rapport  =  this.getVolume()/block.getVolume();
            double result = rapport*block.getUnitPrice();
            return result;
        }
        else {
            return this.getUnitPrice();
        }
    }

    public Block getById(Connection connection) throws Exception {
        if (connection==null){
            connection = Base.PsqlConnect();
        }
        List<Object> list =  this.read(" where id = "+ this.getId(),connection );
        if (list.isEmpty()){
            return null;
        }
        Block forme = (Block) list.get(0);
        return forme;
    }

    public List<Block> usefullBlock(Connection connection)throws Exception{
        List<Object> objects =  this.read(" where status = usefull",connection);
        List<Block> blocks = new ArrayList<Block>();
        for (Object object : objects) {
            Block block = (Block) object;
            blocks.add(block);
        }
        return blocks;
    }

    public List<Block> uselessBlock(Connection connection)throws Exception{
        List<Object> objects =  this.read(" where status = useless",connection);
        List<Block> blocks = new ArrayList<Block>();
        for (Object object : objects) {
            Block block = (Block) object;
            blocks.add(block);
        }
        return blocks;
    }

    public void usedBlock(Connection connection) throws SQLException {
        this.setStatus("useless");
        PreparedStatement preparedStatement = null;
        String sql = "update Block set status='"+this.getStatus()+"' where id=" + id;
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<MouvementStock> historyBlock(Connection connection) throws SQLException {
        MouvementStock mouvementStock = new MouvementStock();
        List<Object> objects =  mouvementStock.read(" where idBlock = "+this.getId(),connection);
        List<MouvementStock> mouvementStocks = new ArrayList<MouvementStock>();
        for (Object object : objects) {
            MouvementStock block = (MouvementStock) object;
            mouvementStocks.add(block);
        }
        return mouvementStocks;
    }

    public Block parent(Connection connection) throws SQLException {
        List<Object> o  = this.read(" where id ="+this.getIdSource(),connection);
        if (o.isEmpty()){
            return null;
        }
        return (Block) o.get(0);
    }

    public List<Block> origins (Connection connection) throws SQLException {
        Block temp = this.parent(connection);
        List<Block> values = new ArrayList<Block>();
        values.add(temp);
        while (temp.getIdSource() != 0) {
            temp = this.parent(connection);
            values.add(temp);
        }
        return values;
    }
    public Block ancestor(Connection connection) throws SQLException {
        List<Block> origins = this.origins(connection);
        Block first = origins.get(origins.size()-1);
        return first;
    }

    public Block childs(Connection connection) throws SQLException {
        List<Object> o  = this.read(" where idSource  ="+this.getId(),connection);
        if (o.isEmpty()){
            return null;
        }
        return  (Block) o.get(0);
    }
    public  double getRapport (double new_value){
        return new_value/this.getUnitPrice();
    }
    public void updatePRevient(double rapport,  Connection connection) throws Exception {
        this.setUnitPrice(this.getUnitPrice()*rapport);
        PreparedStatement preparedStatement = null;
        String sql = "update Block set unit_price ='"+this.getUnitPrice()+"' where id=" + id;
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updatePRevient(Connection connection) throws Exception {
        this.setUnitPrice(this.getUnitPrice());
        PreparedStatement preparedStatement = null;
        String sql = "update Block set unit_price ="+this.getUnitPrice()+" where id=" + id;
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<Block> lignee (Connection connection) throws SQLException {
        List<Block> childs = new ArrayList<Block>();
        Block child = this.childs(connection) ;
        childs.add( child);
        this.setChild(child);
        int counter = 1;
        while (child.childs(connection) != null) {
            child = child.childs(connection);
            child.setChild(child);
            counter++;
            childs.add(child);
        }
        return childs;
    }

    public void updateChilds(double newPrevient,double oldPrixRevient,  Connection connection) throws Exception {
        List<Block> childs = this.lignee(connection);
        double rapport = newPrevient/oldPrixRevient;
        System.out.println("rapport:"+rapport);
        double value = 0;
        for (Block block : childs) {
            value = block.getUnitPrice()*rapport;
            block.setUnitPrice(value);
            block.updatePRevient(connection);

        }

    }

    public Historique findHistorique(Connection connection) throws Exception {
        List<MouvementStock> history = this.historyBlock(connection);
        HashMap<MouvementStock,Double> price = new HashMap<MouvementStock,Double>();
        for (MouvementStock mouvementStock : history) {
            price.put(mouvementStock,mouvementStock.getMontant(connection));
        }
        double montant = history.get(0).getMontant(history,connection);
        this.setHistorique(new Historique());
        this.getHistorique().setHistory(history);
        this.getHistorique().setTotal(montant);
        this.getHistorique().setPrice(price);
        Historique historique1 = new Historique();

        return this.getHistorique();
    }


    @Contract("_, _ -> new")
    public static @NotNull Simulation optimizeSurfaces(double v1, List<Forme_usuel> smallSurfaces){
        smallSurfaces.sort((a,b)->Double.compare(b.getVolume(),a.getVolume()));
        double currentSum = 0;
        double revenues = 0;
        int counter = 0;
        Map<Integer,Integer> surfaceCount = new HashMap<Integer,Integer>();
        Map<Integer,Double> revenuesPerSurfaces = new HashMap<Integer,Double>();
        for (Forme_usuel surface : smallSurfaces) {
            while (currentSum+surface.getVolume() <= v1){
                currentSum += surface.getVolume();
                counter++;
                revenues += surface.getPrixUsuel().getUnitPrice()*surface.getVolume();
                surfaceCount.put(surface.getId(),surfaceCount.getOrDefault(surface.getId(),0)+1);
                revenuesPerSurfaces.
                        put(
                                surface.getId(),
                                revenuesPerSurfaces.getOrDefault(
                                        surface.getId(),
                                        0.0)
                                        +surface.getPrixUsuel().getUnitPrice()*surface.getVolume()
                        );
            } 
        }
        double remainingSurfaces = v1  - currentSum;
        Simulation simulation = new Simulation();
        simulation.setSurfaceCount(surfaceCount);
        simulation.setRemaining(remainingSurfaces);
        simulation.setTotalUsed(currentSum);
        simulation.setRevenuesPerSurface(revenuesPerSurfaces);
        return simulation;
    }

    public Simulation simulateMinReste(boolean hasChild, Connection connection) throws Exception {
        double volume  = this.getVolume();
        Block child =  this.childs(connection);
        if (hasChild){
            volume -= child.getVolume();
        }
        List<Forme_usuel> formeUsuels = new Forme_usuel().getAll(connection);
        this.setSimulationReste(Block.optimizeSurfaces(volume,formeUsuels));
        return this.getSimulationReste();
    }

    public static Simulation optimizeByBestPrice(double v1, List<Forme_usuel> smallSurfaces){
        smallSurfaces.sort((a,b)->Double.compare(b.getPrixUsuel().getUnitPrice()* b.getVolume(), a.getPrixUsuel().getUnitPrice()*a.getVolume()));
        double currentSum = 0;
        double revenues  = 0;
        Map<Integer,Integer> qttTotal = new HashMap<Integer,Integer>();
        Map<Integer,Double> revenuesPerSurfaces = new HashMap<Integer,Double>();

        for (Forme_usuel surface : smallSurfaces) {

            while (currentSum+surface.getVolume() <= v1){

                currentSum += surface.getVolume();
                revenues += surface.getPrixUsuel().getUnitPrice()*surface.getVolume();
                qttTotal.
                        put(
                                surface.getId(),
                                qttTotal.getOrDefault(
                                        surface.getId(),
                                        0)+1);

                revenuesPerSurfaces.
                        put(
                                surface.getId(),
                                revenuesPerSurfaces.getOrDefault(
                                        surface.getId(),
                                        0.0)
                                        +surface.getPrixUsuel().getUnitPrice()*surface.getVolume()
                                );
            }
        }
        double remainingSurfaces = v1  - currentSum;
        Simulation simulation1 =  new Simulation();
        simulation1.setRemaining(remainingSurfaces);
        simulation1.setTotalUsed(currentSum);
        simulation1.setSurfaceCount(qttTotal);
        simulation1.setRevenuesPerSurface(revenuesPerSurfaces);
        return simulation1;
    }

    public Simulation simulateMaxRevenues(boolean hasChild,Connection connection) throws Exception {
        double volume  = this.getVolume();
        if (hasChild){
            volume -= child.getVolume();
        }
        List<Forme_usuel> formeUsuels = new Forme_usuel().getAll(connection);
        this.simulationPrix = new Simulation();
        this.setSimulationPrix(Block.optimizeByBestPrice(volume,formeUsuels));
        return this.getSimulationPrix();
    }

    public Block findById(Connection connection) throws Exception{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from block where id = ?";

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, this.getId());
        System.out.println(preparedStatement);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            this.setId(resultSet.getInt("id"));
            this.setVolume(resultSet.getInt("volume"));
            this.setUnitPrice(resultSet.getDouble("unit_price"));
            System.out.println("unit Price:"+ unitPrice);
            this.setStatus(resultSet.getString("status"));
            this.setLongueur(resultSet.getInt("long"));
            this.setLargeur(resultSet.getInt("large"));
            this.setHauteur(resultSet.getInt("hauteur"));
            this.setIdSource(resultSet.getInt("idsource"));
            this.setDateAjout(resultSet.getTimestamp("date_ajout"));
            this.setIdMachine(resultSet.getInt("idmachine"));
            this.getMachine(connection);
            return this;
        }

        return  null;
    }

    public List<Block> getAll(Connection connection) throws Exception {
        if (connection==null){
            connection = Base.PsqlConnect();
        }
        List<Object> list =  this.read("",connection );
        List<Block> formes = new ArrayList<Block>();
        if (list.isEmpty()){
            return null;
        }
        for (Object object : list) {
            Block forme = (Block) object;
            forme.getMachine(connection);
            formes.add(forme);
        }
        connection.close();
        return formes;
    }
/*
*                           PART 2
* */
    public Machine getMachine(Connection connection) throws Exception {
        Machine machine = new Machine();
        machine.setId(this.getIdMachine());
        machine = machine.getById(machine.getId(),connection);
        return machine;
    }
    public  MaterielUsed[] getUsed(Connection connection) throws Exception {
        return MaterielUsed.getByIdMachine(getIdMachine(),getVolume(),connection);
    }

    public boolean checkStock(Connection connection,MaterielUsed[] materielUseds) throws Exception {
        if (materielUseds.length == 0) {
            throw  new Exception("liste vide");
        }else System.out.println("taille "+ materielUseds.length);
        for (int i = 0; i < materielUseds.length; i++) {
            if (materielUseds[i].getQtt() > Reste.getResteByIdMatiere(materielUseds[i].getIdMatiere(),this.getDateAjout(),connection)){
                return false;
            }
        }
        return true;
    }
//
//    public boolean checkStock(MaterielUsed[] materielUseds,Stock[] stocks) throws Exception {
//        if (materielUseds.length == 0) {
//            throw  new Exception("liste vide");
//        }else System.out.println("taille "+ materielUseds.length);
//        for (int i = 0; i < materielUseds.length; i++) {
//            for (Stock stock : stocks) {
//                fi
//            }
//            if (materielUseds[i].getQtt() > Reste.getResteByIdMatiere(materielUseds[i].getIdMatiere(),this.getDateAjout(),connection)){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public boolean checkStock(Connection connection,MaterielUsed[] materielUseds) throws Exception {
//        System.out.println("start check");
//        if (materielUseds.length == 0) {
//            throw  new Exception("liste vide");
//        }else System.out.println("taille "+ materielUseds.length);
//        for (int i = 0; i < materielUseds.length; i++) {
//            System.out.println("check "+i);
//            if (materielUseds[i].getQtt() > Reste.getResteByIdMatiere(materielUseds[i].getIdMatiere(),this.getDateAjout(),connection)){
//                System.out.println("FALSE id"+materielUseds[i].getIdMatiere()+": used "+materielUseds[i].getQtt()+" \n reste: " + Reste.getResteByIdMatiere(materielUseds[i].getIdMatiere(),this.getDateAjout(),connection));
//                return false;
//            }
//            System.out.println(" TRUE -> id"+materielUseds[i].getIdMatiere()+": used "+materielUseds[i].getQtt()+"\n reste: " + Reste.getResteByIdMatiere(materielUseds[i].getIdMatiere(),this.getDateAjout(),connection));
//
//        }
//        return true;
//    }

    /*
    * fabrication with check stock
    * alaina ny liste Stock mbola tsy  == 0 ny reste par matiere
    * update restant -> restant  =  restant - used
    * getpunitaire et punitaire * qtt  = couttotal
    * sum += cout total
    * if mbola tsy ampy : miampita stock  meme id manaraka
    * mandrapa restant > used
    * getTheorique (sum[])
    * insert theorique et machiniste
    * 5 points
    * */

    public double insertUsed( Connection connection) throws Exception {
        MaterielUsed[] materielUseds  = this.getMaterielUsed();
        double theorique = 0;
        for (MaterielUsed used : materielUseds) {
            Stock[] stocks = new Stock().getStockByIdMatiere(this.getDateAjout(), used.getIdMatiere(), connection);
            theorique +=  used.insertused(stocks, this.getDateAjout(), connection);
        }
        System.out.println("insert used ok");
        return theorique;
    }

    public double insertUsed( Stock[] stocks, Connection connection) throws Exception {
        MaterielUsed[] materielUseds  = this.getMaterielUsed();
        double theorique = 0;
        for (MaterielUsed used : materielUseds) {
            theorique +=  used.insertused(stocks, this.getDateAjout(), connection);
        }
        System.out.println("insert used ok");
        return theorique;
    }

}

