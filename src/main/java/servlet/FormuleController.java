package servlet;

import connexion.Base;
import formule.DetailsFormule;
import formule.Formule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import matiere.Matiere;
import simpleController.CtrlAnnotation;
import simpleController.MereController;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FormuleController", value = "*.FormuleController")
public class FormuleController extends MereController {
    @CtrlAnnotation(name = "affList")
    public void affList() throws Exception {
        Connection connection  = Base.PsqlConnect();
//        try{
            List<Object> mat  = new Formule().read("",connection);
            List<Formule> formules  = new ArrayList<>();
            for (Object o : mat) {
                Formule formule = (Formule) o;
                formule.setDetailsFormule(formule.getDetailsFormule(connection));
                System.out.println(formule.getDetails());

                formules.add(formule);
            }
            List<Object> ma  = new Matiere().read("",connection);
            List<Matiere> matieres  = new ArrayList<>();
            for (Object o : ma) {
                Matiere matiere = (Matiere) o;
                matiere.getUnite(matiere.getIdUnite(),connection);
                matieres.add(matiere);
            }
            connection.close();

            request.setAttribute("formules", formules.toArray(new Formule[formules.size()]));
            request.setAttribute("matieres", matieres.toArray(new Matiere[matieres.size()]));
            RequestDispatcher rd = request.getRequestDispatcher("Pages/matiere/list_Formule.jsp");
            rd.forward(request, response);

//        }catch (Exception e){
//            e.printStackTrace();
//           throw new Exception(e.getLocalizedMessage());
//        }
    }

    @CtrlAnnotation(name = "insert")
    public void insert() throws Exception {
        String nom = request.getParameter("nom");
        Connection connection  = Base.PsqlConnect();
        try {
            Formule formule = new Formule();
            formule.setNom(nom);
            formule.create(connection);
            connection.close();
            this.affList();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @CtrlAnnotation(name = "insertDetails")
    public void insertDetails() throws Exception {
        Connection connection  = Base.PsqlConnect();
        List<Object> ma  = new Matiere().read("",connection);
        List<Matiere> matieres  = new ArrayList<>();
        for (Object o : ma) {
            Matiere matiere = (Matiere) o;
            matiere.getUnite(matiere.getIdUnite(),connection);
            matieres.add(matiere);
        }

        for (Matiere matiere : matieres) {
            int idFormule = Integer.parseInt(request.getParameter("idFormule"));
            int idMatiere  = Integer.parseInt(request.getParameter("idMatiere"+matiere.getId()));
            double quantite  = Double.parseDouble(request.getParameter("matiere"+matiere.getId()));
            DetailsFormule detailsFormule = new DetailsFormule();
            detailsFormule.setIdFormule(idFormule);
            detailsFormule.setIdMatiere(idMatiere);
            detailsFormule.setQuantite(quantite);
            try{
                detailsFormule.create(connection);
            }catch (Exception e){
                throw  new Exception(e.getMessage());
            }
        }
        connection.close();
        this.affList();
    }


}
