package agentes;

import Parents.SuperAgent;
import ejemplos.Conexion;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class gestorContenido extends SuperAgent {

    private Hashtable catalogoPeliculas = new Hashtable();

    private String[][] servicios = {
        {
            "vendedor",
            "Vender peliculas"
        },
        {
            "guardarAsignatura",
            "Guardar asignatura en el Sistema"
        },
        {
            "guardarPreguntasDeEvaluacion",
            "Guardar preguntas de la evaluación en el Sistema"
        }
    };
    
    @Override
    protected void setup() {
       this.printPantalla("Esta corriendo");
        // Registro el servicio que presta este agente
        this.cargarServicios(this.servicios);
        this.establecerConexionDB();
 
        this.ObtenerInformacionDatos();
        
        //Agregar comportamientos 
        this.addBehaviour(new EsperarAccion());
    }
    
    private void ObtenerInformacionDatos(){
        
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from evaluacion where id=1");
            while(rs.next()){
                System.out.println("nota: " + rs.getString(2));
                System.out.println("descripcion: " + rs.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
     
    //Temporalmente se realiza una función que 
    public void guardarPreguntasDeEvaluacion(){
        
    }
    
    /*
        Esta función recibe las peticiones por parte de los otros agentes y realiza las acciones necesarias
        Recibe un JSON en donde se indica que función realizará con los datos recibidos.
    */
    private class EsperarAccion extends CyclicBehaviour{

        @Override
        public void action() {
            
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //Recuperación del JSON que se envía
                String stringJson = msg.getContent();
                
                System.err.println(stringJson);
            } else {
                block();
            }
        }
        
    }
   
    private class ObtenerResultadosEstudiante extends OneShotBehaviour {

        @Override
        public void action() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    
}
