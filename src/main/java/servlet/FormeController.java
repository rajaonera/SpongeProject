package servlet;

import annexe.Utils;
import block.Block;
import connexion.Base;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import simpleController.CtrlAnnotation;
import simpleController.MereController;
import usuel.Forme_usuel;
import usuel.PrixUsuel;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FormeController",value = "*.FormeController")
public class FormeController extends MereController {
    public FormeController() {}
    @CtrlAnnotation(name = "AffList")
    public void list() throws Exception {
        Base base  =  new Base();
        try{
            Connection connection  =  base.PsqlConnect();
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
            RequestDispatcher rd = request.getRequestDispatcher("/Pages/forme/ListForme.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @CtrlAnnotation(name = "insertPrix")
    public void insertPrix() throws Exception {
        Base base =  new Base();
        Connection connection  =  base.PsqlConnect();
        PrixUsuel prix = new PrixUsuel();
        int id  =  Integer.parseInt(request.getParameter("id"));
        System.out.println(id);
        prix.setIdForme(id);
        Forme_usuel forme_usuel = new Forme_usuel();
        forme_usuel.setId(id);
        forme_usuel.findById(connection);
        Date dateAjout  = Date.valueOf(request.getParameter("date_ajout"));
        System.out.println(dateAjout);
        prix.setDateModif(dateAjout);
        String type = request.getParameter("typePrice");
        System.out.println(type);
        prix.setTypePrice(type);
        Double unitPrice  = Double.parseDouble(request.getParameter("prix"));
        double perVolume  = unitPrice/forme_usuel.getVolume();
        System.out.println("block:"+forme_usuel.getVolume());
        System.out.println("unit:"+unitPrice);
        System.out.println("pervolume:"+perVolume);
        prix.setUnitPrice(perVolume);
        prix.create(connection);
        connection.close();
        this.Info();
    }

    @CtrlAnnotation(name = "Info")
    public void Info() throws Exception {
        Base base  =  new Base();
        try{
            Connection connection  =  base.PsqlConnect();
            Forme_usuel forme_usuel = new Forme_usuel();
            PrixUsuel prixUsuel = new PrixUsuel();
            int id = Integer.parseInt(request.getParameter("id"));
            List liste = (List) forme_usuel.read(" where id="+id,connection);
            List listePrix = (List) prixUsuel.read(" where idForme="+id+" order by date_modif desc",connection);
            connection.close();
            List<Forme_usuel> formeUsuels = new ArrayList<Forme_usuel>();
            for (Object o : liste) {
                formeUsuels.add((Forme_usuel) o);
            }
            List<PrixUsuel> prixUsuels = new ArrayList<PrixUsuel>();
            for (Object o : listePrix) {
                prixUsuels.add((PrixUsuel) o);
            }
            Forme_usuel formeUsuel = formeUsuels.get(0);
            PrixUsuel[] prix = new PrixUsuel[prixUsuels.size()];
            prixUsuels.toArray(prix);
            request.setAttribute("forme",formeUsuel );
            request.setAttribute("listePrix",prix );

              RequestDispatcher rd = request.getRequestDispatcher("/Pages/forme/InfoForme.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CtrlAnnotation(name="insertForme")
    public void insertForme() throws Exception {
        String nom =  request.getParameter("nom")  ;
        String longString  = request.getParameter("longueur");
        String largeString  = request.getParameter("largeur");
        String hautString  = request.getParameter("hauteur");
        double longueur = Utils.split(longString);
        double largeur = Utils.split(largeString);
        double hauteur = Utils.split(hautString);
        Date date_ajout   = Date.valueOf(request.getParameter("date_ajout")) ;
        Base base  =  new Base();
        try{
            Connection connection =  base.PsqlConnect();
            Forme_usuel forme_usuel = new Forme_usuel() ;
            forme_usuel.setLongueur(longueur);
            forme_usuel.setNom(nom);
            forme_usuel.setLargeur(largeur);
            forme_usuel.setHauteur(hauteur);
            forme_usuel.setVolume();
            forme_usuel.setDateAjout(date_ajout);
            forme_usuel.create(connection);
            connection.close();
            request.setAttribute("message", "insertion effectuer");
            this.list();
        }
        catch(Exception e){
            request.setAttribute("message", "erreur:"+e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/Pages/forme/ListForme.jsp");
            rd.forward(request, response);
        }

    }
}
