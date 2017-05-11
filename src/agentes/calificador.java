package agentes;

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



public class calificador extends Agent {

    private String[][] servicios = {
        {
            "calcularPuntaje",
            "Calcular puntaje del estudiante"
        },
        {
            "obtenerResultadoEstudiante",
            "Obtener resultado de estudiante"
        }
    };
    
    @Override
    protected void setup() {
       this.printPantalla("Esta corriendo");
        // Registro el servicio que presta este agente
        this.cargarServicios();
        
        //Agregar comportamientos 
        this.addBehaviour(new EsperarRespuestas());
        this.addBehaviour(new EsperarSolicitudResultadosEstudiante());
        
    }

    /*
        Se realiza la asignacion de los servicios que ofrece este agente.
        Los servicios son las funciones que realiza y pueden ser utilizadas por otros agentes
    */
    private void cargarServicios(){
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            
            ServiceDescription sd;
            for (String[] servicio : this.servicios) {
                sd = new ServiceDescription();
                sd.setType(servicio[0]);
                sd.setName(servicio[1]);
                dfd.addServices(sd);
            }         
            
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
    
    private class EsperarRespuestas extends CyclicBehaviour {
        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //Recuperación del id de la evaluación o simulacro
                String id_evaluacion = msg.getContent();
                printPantalla("Me llego un mensaje con el id_evaluación - " + id_evaluacion);
                calcularPuntaje(Integer.parseInt(id_evaluacion));
            } else {
                block();
            }
        }
    }
    
    private class EsperarSolicitudResultadosEstudiante extends CyclicBehaviour {
        @Override
        public void action() {
//200 es el codigo establecido para la entrada a esta funcion y tambien responde por el codigo 200
            MessageTemplate mt = MessageTemplate.MatchPerformative(200);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //Recuperación del id del estudiante al que hay que devolver los resultados que ha tenido.
                String id_estudiante = msg.getContent();
                printPantalla("Me llego un mensaje con el id_evaluación - " + id_estudiante);
                
                
                String info = getInfoEstudiante(Integer.parseInt(id_estudiante));
                
                printPantalla("resultado getInfoEstudiante " + info);
                
                ACLMessage reply = msg.createReply();
                
                reply.setContent(info);
                reply.setPerformative(200);
                
                
                myAgent.send(reply);
                myAgent.doDelete();
                
                
            } else {
                block();
            }
        }
    }
    
    private void calcularPuntaje(int id_evaluacion){
        this.printPantalla("Esta calculando e puntaje de la evaluacion o simulacro");
    }
    
    public void printPantalla(String msj){
        System.out.println(getAID().getName() + msj);
    }
    
    private String getInfoEstudiante(int id_estudiante){
        return "Retornando la información del estudiante";
    }
}
