package servlet;

import annexe.Utils;
import block.Block;

import block.Historique;
import connexion.Base;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import simpleController.CtrlAnnotation;
import simpleController.MereController;
import stock.MouvementStock;
import usuel.Forme_usuel;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StockController", value = "*.StockController")
public class StockController extends MereController {
    @CtrlAnnotation(name = "AffList")
    public void list() throws Exception {
        int idBlock  = Integer.parseInt(request.getParameter("idBlock"));
        Base base  =  new Base();
        try{
            Connection connection  =  Base.PsqlConnect();
//          block
            Block block = new Block();
            block.setId(idBlock);
            block =block.getById(connection);
            block.setVolume();

            Block parent = block.parent(connection) ;
            block.setParent(parent);

            block.setSimulationReste(block.simulateMinReste(false,connection));
            block.setSimulationPrix(block.simulateMaxRevenues(false,connection));

//          forme list
            Forme_usuel forme_usuel = new Forme_usuel();
            List liste = (List) forme_usuel.read("",connection);
            connection.close();
            List<Forme_usuel> formeUsuels = new ArrayList<Forme_usuel>();
            for (Object o : liste) {
                formeUsuels.add((Forme_usuel) o);
            }
            Forme_usuel[] list = new Forme_usuel[formeUsuels.size()];
            formeUsuels.toArray(list);
            request.setAttribute("formes",list );
            HttpSession session = request.getSession(true);

            List<Forme_usuel> choices  = (List<Forme_usuel>) session.getAttribute("choices");
            if (choices == null) {
                choices = new ArrayList<>();
            }
            List<Integer> qtt_choices  = (List<Integer>) session.getAttribute("qtt_choices");
            if (qtt_choices == null) {
                qtt_choices = new ArrayList<>();
            }
            session.setAttribute("choices",choices);
            session.setAttribute("qtt_choices",qtt_choices);
            session.setAttribute("block",block);
            RequestDispatcher rd = request.getRequestDispatcher("/Pages/model/listModel.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @CtrlAnnotation(name =  "insertPanier")
    public void insertPanier() throws Exception {
        int idForme  =  Integer.parseInt(request.getParameter("idForme"));
        HttpSession session = request.getSession(true);
        Block block = (Block) session.getAttribute("block");
        int idBlock = block.getId();
        int qtt =  Integer.parseInt(request.getParameter("qtt"));
        Timestamp dateAjout   = Utils.strToTimeStamp(request.getParameter("date_ajout")) ;
        try{
            Connection connection =  Base.PsqlConnect();
//            block
            Block source = new Block() ;
            source.setId(idBlock);
            source =  source.getById(connection);
            source.setSimulationReste(source.simulateMinReste(false,connection));
            source.setSimulationPrix(source.simulateMaxRevenues(false,connection));
//            forme
            Forme_usuel forme_choice = new Forme_usuel();
            forme_choice.setId(idForme);
            forme_choice =  forme_choice.getById(connection);
            List<Forme_usuel> choices  = (List<Forme_usuel>) session.getAttribute("choices");
            List<Integer> qtt_choices  = (List<Integer>) session.getAttribute("qtt_choices");
            choices.add(forme_choice);
            qtt_choices.add(qtt);
            session.setAttribute("choices",choices);
            session.setAttribute("qtt_choices",qtt_choices);
            double volume_utilise = 0 ;
            int count = 0;
            PrintWriter out = response.getWriter();
            for (Forme_usuel c: choices){
                volume_utilise += c.getVolume()*qtt_choices.get(count);
                count++;
            }
            Double volume_reste  = source.getVolume() - volume_utilise ;
            session.setAttribute("volume_utilise",volume_utilise );
            session.setAttribute("volume_reste",volume_reste );
            connection.close();
            request.setAttribute("message", "insertion effectuer");

            this.list();
        }
        catch(Exception e){
            request.setAttribute("message", "erreur:"+e.getMessage());
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
//            RequestDispatcher rd = request.getRequestDispatcher("/Pages/model/listModel.jsp");
//            rd.forward(request, response);
//            throw new Exception(e.getMessage()) ;
        }
    }
    @CtrlAnnotation(name="restartSession")
    public void restartSession() throws Exception {
        HttpSession session = request.getSession(false);
        session.invalidate();
        int idBlock =  Integer.parseInt(request.getParameter("idBlock"));
        response.sendRedirect(request.getContextPath()+"/AffList.StockController?idBlock="+idBlock);
    }
    @CtrlAnnotation(name="killSession")
    public void killSession() throws Exception {
        if (request.getSession(false) != null) {
            HttpSession session = request.getSession(false);
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath()+"/index.jsp");
    }
    @CtrlAnnotation(name ="verification")
    public void verification()throws Exception{
        String longString  = request.getParameter("longueur");
        String largeString  = request.getParameter("largeur");
        String hautString  = request.getParameter("hauteur");
        double longueur = Utils.split(longString);
        double largeur = Utils.split(largeString);
        double hauteur = Utils.split(hautString);
        double reste_saisie = longueur*largeur*hauteur ;
        double marge  =  Double.parseDouble(request.getParameter("marge"));
        Timestamp date_ajout   = Utils.strToTimeStamp(request.getParameter("date_ajout")) ;
        HttpSession session = request.getSession(false);
        session.setAttribute("date_ajout",date_ajout);
        Block bLock = (Block) session.getAttribute("block");
        double volume_utilise = (double) session.getAttribute("volume_utilise");
        double reste_normal = bLock.getVolume() - volume_utilise ;
        double value =reste_normal -reste_saisie;
        double teta = marge*bLock.getVolume()/100;
        try{
            if (value>= teta){
                throw new Exception("volume restant non coherant, reessayer. volume" +
                        ":"+reste_normal+" // marge:"+marge*bLock.getVolume()/100);
            }
            session.setAttribute("new_reste",reste_saisie );
            this.validerForme();
        }catch (Exception e){
            request.setAttribute("message",e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/Pages/model/verification.jsp");
            rd.forward(request, response);
//            this.list();
        }
    }

    @CtrlAnnotation(name = "validerForme")
    public void validerForme() throws Exception {
        HttpSession session = request.getSession(false);
        Block bLock = (Block) session.getAttribute("block");
        Timestamp date_ajout   = Utils.strToTimeStamp(session.getAttribute("date_ajout").toString()) ;
        List<Forme_usuel> choices  = (List<Forme_usuel>) session.getAttribute("choices");
        List<Integer> qtt_choices  = (List<Integer>) session.getAttribute("qtt_choices");
        try{
            MouvementStock mouvementStock = new MouvementStock();
            Connection connection = Base.PsqlConnect();
            bLock = bLock.findById(connection);
            double volume = bLock.getVolume();
            double prix_block= bLock.getUnitPrice()*volume;
            for (int i = 0; i <choices.size() ; i++) {
                mouvementStock.setIdForme(choices.get(i).getId());
                mouvementStock.setIdBlock(bLock.getId());
                int idForme = choices.get(i).getId();
                Forme_usuel forme_choice = new Forme_usuel();
                forme_choice.setId(idForme);
                forme_choice =  forme_choice.findById(connection);
                double prixRevient = forme_choice.getVolume()*prix_block/volume;
                mouvementStock.setPrUnitaire(prixRevient);
                mouvementStock.setQtt(qtt_choices.get(i));
                mouvementStock.setTypeMouv("entree");
                mouvementStock.setDateAjout(date_ajout);
                mouvementStock.create(connection);
            }
            double new_volume  = (double) session.getAttribute("new_reste");
            Block new_block =  new Block();
            new_block.setIdSource(bLock.getId());
            new_block.setLongueur(1);
            new_block.setLargeur(1);
            new_block.setHauteur(new_volume);
            new_block.setVolume();
            double price  = bLock.getUnitPrice()*new_volume/bLock.getVolume();
            new_block.setUnitPrice(price);
            new_block.setDateAjout(date_ajout);
            new_block.create(connection);
            bLock.usedBlock(connection);
            connection.close();
            this.killSession();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @CtrlAnnotation(name = "historique")
    public void historique() throws Exception {
        int idBlock  = Integer.parseInt(request.getParameter("idBlock"));
        Base base  =  new Base();
        try{
            Connection connection  =  base.PsqlConnect();
    //          block
            Block block = new Block();
            block.setId(idBlock);
            block =block.findById(connection);
            block.setVolume();
            Block child = block.childs(connection) ;
            Block parent = block.parent(connection);
            block.setChild(child);
            block.setParent(parent);
            System.out.println("Status: "+block.getChild().getStatus());
            block.setSimulationReste(block.simulateMinReste(true,connection));
            block.setSimulationPrix(block.simulateMaxRevenues(true, connection));
            List<Forme_usuel> formeUsuels = new Forme_usuel().getAll(connection);
    //          forme list
            Historique historique = block.findHistorique(connection);
            request.setAttribute("total",historique.getTotal());
            request.setAttribute("price",historique.getPrice());
            request.setAttribute("formes",formeUsuels);
            request.setAttribute("block",block);
            request.setAttribute("history",historique.getHistory());
            RequestDispatcher rd = request.getRequestDispatcher("/Pages/Block/Historique.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
