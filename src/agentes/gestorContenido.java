package agentes;

import Parents.SuperAgent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Hashtable;

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

        //Connection cn = this.conexion();
        //this.verificarConeccion(cn);
        
        //Agregar comportamientos 
        this.addBehaviour(new EsperarAccion());
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
