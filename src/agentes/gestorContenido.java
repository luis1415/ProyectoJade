package agentes;

import Parents.SuperAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
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

        //BASE DE DATOS DE PELICULAS DISPONIBLES
        catalogoPeliculas.put("El señor de los anillos", 30000);
        catalogoPeliculas.put("El hobbit", 60000);
        catalogoPeliculas.put("Harry Potter", 10000);

        //Agregar comportamientos 
        this.addBehaviour(new EsperarOferta());
        this.addBehaviour(new EsperarAccion());
        this.addBehaviour(new EsperarConfirmacion());
    }
    
    private class EsperarOferta extends CyclicBehaviour {
        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //Recuperación del nombre de la pelicula
                String title = msg.getContent();
                
                ACLMessage reply = msg.createReply();
                Integer price = (Integer) catalogoPeliculas.get(title);
                if (price != null) {
                    reply.setPerformative(100);
                    reply.setContent(String.valueOf(price.intValue()));
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("pelicula no disponible");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }

    private class EsperarConfirmacion extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                System.out.println(getAID().getName() + ": Transacción exitosa: Pelicula comprada!!!");
            }
        }
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
    
    private void printPantalla(String msj){
        System.out.println(getAID().getName() + msj);
    }
    
}
