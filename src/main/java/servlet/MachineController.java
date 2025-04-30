package servlet;

import connexion.Base;
import formule.Formule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import machine.Machine;
import matiere.Matiere;
import simpleController.CtrlAnnotation;
import simpleController.MereController;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "MachineController", value = "*.MachineController")
public class MachineController extends MereController {
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
        List<Object> ma  = new Machine().read("",connection);
        List<Machine> machines  = new ArrayList<>();
        for (Object o : ma) {
            Machine machine = (Machine) o;
            machine.setFormule(machine.getFormule(connection)) ;
            machines.add(machine);
        }
        connection.close();

        request.setAttribute("formules", formules.toArray(new Formule[formules.size()]));
        request.setAttribute("machines", machines.toArray(new Machine[machines.size()]));
        RequestDispatcher rd = request.getRequestDispatcher("Pages/matiere/list_machine.jsp");
        rd.forward(request, response);

//        }catch (Exception e){
//            e.printStackTrace();
//           throw new Exception(e.getLocalizedMessage());
//        }
    }

    @CtrlAnnotation(name = "insert")
    public void insert() throws Exception {
        String nom = request.getParameter("nom");
        int idForumle =Integer.parseInt(request.getParameter("idFormule")) ;
        Connection connection  = Base.PsqlConnect();
        Machine machine = new Machine();
        machine.setNom(nom);
        machine.setIdFormule(idForumle);
        machine.create(connection);
        connection.close();
        this.affList();

    }
}
