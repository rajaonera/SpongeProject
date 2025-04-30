package servlet;

import connexion.Base;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import matiere.Unite;
import simpleController.CtrlAnnotation;
import simpleController.MereController;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UniteController", value = "*.UniteController")
public class UniteController extends MereController {
    @CtrlAnnotation(name = "affList")
    public void affList() throws Exception {
        try{
            Connection connection  = Base.PsqlConnect();
            List<Object> unite  = new Unite().read("",connection);
            List<Unite> unites  = new ArrayList<>();
            for (Object o : unite) {
                unites.add((Unite)o);
            }
            connection.close();
            request.setAttribute("unites", unites.toArray(new Unite[unites.size()]));
            RequestDispatcher rd = request.getRequestDispatcher("Pages/matiere/list_unite.jsp");
            rd.forward(request, response);

        }catch (Exception e){
            PrintWriter out = response.getWriter();
            out.println(e.getMessage());
        }
    }
    @CtrlAnnotation(name = "insert")
    public void insert() throws Exception {
        String nomUnite = request.getParameter("nomUnite");
        Unite unite = new Unite();
        unite.setNom(nomUnite);
        Connection connection  = Base.PsqlConnect();
        try{
            unite.create(connection);
            connection.close();
            this.affList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
