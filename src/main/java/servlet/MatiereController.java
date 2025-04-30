package servlet;

import annexe.Utils;
import connexion.Base;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import matiere.Matiere;
import matiere.Stock;
import matiere.Unite;
import simpleController.CtrlAnnotation;
import simpleController.MereController;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@WebServlet(name = "MatiereController", value = "*.MatiereController")
public class MatiereController extends MereController {
    @CtrlAnnotation(name = "affList")
    public void affList() throws Exception {
        Connection connection  = Base.PsqlConnect();
        try{
            List<Object> mat  = new Matiere().read("",connection);
            List<Matiere> matieres  = new ArrayList<>();
            for (Object o : mat) {
                Matiere matiere = (Matiere) o;
                matiere.getUnite(matiere.getIdUnite(),connection);
                matieres.add(matiere);
            }
            List<Object> list = new Unite().read("",connection);
            List<Unite> unites = new ArrayList<>();
            for (Object o : list) {
                unites.add((Unite) o);
            }
            connection.close();
            request.setAttribute("matieres", matieres.toArray(new Matiere[matieres.size()]));
            request.setAttribute("unites", unites.toArray(new Unite[unites.size()]));
            RequestDispatcher rd = request.getRequestDispatcher("Pages/matiere/list_matiere.jsp");
            rd.forward(request, response);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @CtrlAnnotation(name = "insert")
    public void insert() throws Exception {
        String nomMatiere = request.getParameter("nomMatiere");
        int idUnite = Integer.parseInt(request.getParameter("uniteMesure")) ;
        Matiere matiere = new Matiere();
        matiere.setNom(nomMatiere);
        matiere.setIdUnite(idUnite);
        Connection connection  = Base.PsqlConnect();
        try{
            matiere.create(connection);
            connection.close();
            this.affList();
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

    @CtrlAnnotation(name = "insertStock")
    public void insertStock() throws Exception {
        int idMatiere = Integer.parseInt(request.getParameter("idMatiere")) ;
        String type_mouv = "entree";
        double prixUnitaire = Double.parseDouble(request.getParameter("prixUnitaire"));
        double quantite = Double.parseDouble(request.getParameter("quantite"));
        Timestamp date_ajout   = Utils.strToTimeStamp(request.getParameter("date_ajout")) ;
        Stock stock = new Stock();
        stock.setIdMatiere(idMatiere);
        stock.setTypeMouv(type_mouv);
        stock.setQuantity(quantite);
        stock.setDateAjout(date_ajout);
        stock.setPrice(prixUnitaire);
        Connection connection  = Base.PsqlConnect();
        try{
            stock.create(connection);
            connection.close();
            request.setAttribute("id", idMatiere);
            this.stock();
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

    @CtrlAnnotation(name = "stock")
    public void stock() throws Exception {
        int idMatiere = Integer.parseInt(request.getParameter("idMatiere"));
        Matiere matiere = new Matiere();
        matiere.setId(idMatiere);
        Connection connection  = Base.PsqlConnect();
        try{
            matiere = matiere.getById(idMatiere,connection);
            connection.close();
            request.setAttribute("matiere", matiere);
            RequestDispatcher rd = request.getRequestDispatcher("Pages/matiere/info_stock.jsp");
            rd.forward(request, response);

        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }
    }

}


