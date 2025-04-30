package servlet;

import connexion.Base;
import jakarta.servlet.annotation.WebServlet;
import machine.Resultat;
import mg.dao.annotation.Table;
import simpleController.CtrlAnnotation;
import simpleController.MereController;

import java.sql.Connection;

@WebServlet(name = "ResultatController", value = "*.ResultatController")
public class ResultatController extends MereController {
    @CtrlAnnotation(name = "affList")
    public void affList() throws Exception {
        Connection connection = Base.PsqlConnect();
        Resultat[] resultats =  new Resultat().getAll(connection);
        connection.close();
        request.setAttribute("resultats", resultats);
        request.getRequestDispatcher("/Pages/part2/dashboard.jsp").forward(request, response);

    }
    @CtrlAnnotation(name = "affListWithAnnee")
    public void affListWithAnnee() throws Exception {
        int annee = Integer.parseInt(request.getParameter("annee"));
        if (annee == 0){
            this.affList();
        }
        Connection connection = Base.PsqlConnect();
        Resultat[] resultats =  new Resultat().getAll(annee,connection);
        connection.close();
        request.setAttribute("resultats", resultats);
        request.getRequestDispatcher("/Pages/part2/dashboard.jsp").forward(request, response);
    }

}
