package servlet;

import annexe.Utils;
import block.Block;
import connexion.Base;
import formule.Formule;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.annotation.WebServlet;
import machine.Machine;
import simpleController.CtrlAnnotation;
import simpleController.MereController;

import java.io.PrintWriter;
import java.sql.Timestamp;
import  java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Date;

@WebServlet(name = "BlockController", value = "*.BlockController")
public class BlockController extends MereController {

    @CtrlAnnotation(name="AffList")
    public void list() throws Exception {
        Base base  =  new Base();
        try{
            Connection  connection  =  base.PsqlConnect();
            Block block = new Block();
            List liste = (List) block.read("",connection);
            List<Block> blocks = new ArrayList<Block>();
            for (Object o : liste) {
                Block b = (Block) o;
                b.setMachine( b.getMachine(connection));
                blocks.add(b);

            }
            Block[] blockArray = new Block[blocks.size()];
            blocks.toArray(blockArray);
            List<Object> ma  = new Machine().read("",connection);
            List<Machine> machines  = new ArrayList<>();
            for (Object o : ma) {
                Machine machine = (Machine) o;
                machine.setFormule(machine.getFormule(connection)) ;
                machines.add(machine);
            }
            connection.close();

            request.setAttribute("machines", machines.toArray(new Machine[machines.size()]));
            request.setAttribute("blocks",blockArray );
            RequestDispatcher rd = request.getRequestDispatcher("/Pages/Block/ListBlock.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @CtrlAnnotation(name="insertBlock")
    public void insertBlock() throws Exception {
        String longString  = request.getParameter("longueur");
        String largeString  = request.getParameter("largeur");
        String hautString  = request.getParameter("hauteur");
        int idMachine = Integer.parseInt(request.getParameter("idMachine"));
        double longueur = Utils.split(longString);
        double largeur = Utils.split(largeString);
        double hauteur = Utils.split(hautString);double unit_price =  Double.parseDouble(request.getParameter("unit_price"))  ;
        Timestamp date_ajout   = Utils.strToTimeStamp(request.getParameter("date_ajout")) ;
        Base base  =  new Base();
        try{
            Connection connection =  base.PsqlConnect();
            Block block = new Block() ;
            block.setLongueur(longueur);
            block.setLargeur(largeur);
            block.setHauteur(hauteur);
            block.setVolume();
            block.setIdMachine(idMachine);
            block.setUnitPrice(unit_price);
            block.setDateAjout(date_ajout);
            block.create(connection);
            connection.close();
            request.setAttribute("message", "insertion effectuer");
            this.list();

        }
        catch(Exception e){
            request.setAttribute("message", "erreur:"+e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher("/Pages/Block/ListBLock.jsp");
            rd.forward(request, response);
//            throw new Exception(e.getMessage()) ;
        }
    }
    @CtrlAnnotation(name = "update")
    public void update() throws Exception {
        int idBlock =  Integer.parseInt(request.getParameter("idBlock"));
        Block block = new Block();
        block.setId(idBlock);
        Connection connection = Base.PsqlConnect();
        block.getById(connection);
        request.setAttribute("block", block);
        RequestDispatcher rd = request.getRequestDispatcher("/Pages/Block/update.jsp");
        rd.forward(request, response);
    }
    @CtrlAnnotation(name = "updatePRevient")
    public void updatePRevient() throws Exception {
        int idBlock =  Integer.parseInt(request.getParameter("idBlock"));
        double prixRevient =  Double.parseDouble(request.getParameter("prixRevient"));

        Block block = new Block();
        block.setId(idBlock);
        try {
            Connection connection = Base.PsqlConnect();
            List<Block> blocks  =  block.getAll(connection);
            for (Block b : blocks) {
                if (b.getId() == idBlock) {
                    block = b;
                    break;
                }
            }
//            block.getById(connection);
            double old = block.getUnitPrice();
            block.setUnitPrice(prixRevient);
            block.updatePRevient(connection);
            block.getById(connection);
            PrintWriter out = response.getWriter();
            out.println("old:"+old);
            out.println("new:"+block.getUnitPrice());
            block.updateChilds(prixRevient,old,connection);
            connection.close();
            this.list();
        }catch (Exception e){
            request.setAttribute("message", "erreur:"+e.getMessage());
            PrintWriter out  = response.getWriter();
            out.println(e.getMessage());
//            RequestDispatcher rd = request.getRequestDispatcher("/Pages/Block/ListBlock.jsp");
//            rd.forward(request, response);
        }

    }
}
