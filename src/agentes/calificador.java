package agentes;

import Parents.SuperAgent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;



public class calificador extends SuperAgent {

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
        this.cargarServicios(this.servicios);
        
        //Agregar comportamientos 
        this.addBehaviour(new EsperarRespuestas());
        this.addBehaviour(new EsperarSolicitudResultadosEstudiante());
        
    }

    private class EsperarRespuestas extends CyclicBehaviour {
        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                //Recuperación del id de la evaluación o simulacro
                String contenido = msg.getContent();
                printPantalla("Me llego un mensaje con el siguiente contenido - " + contenido);
                
                
                JsonElement arrayElement = new JsonParser().parse(contenido);
                int id_evaluacion = arrayElement.getAsJsonObject().get("id_evaluacion").getAsInt();
                int id_estudiante = arrayElement.getAsJsonObject().get("id_estudiante").getAsInt();
                
                printPantalla("el id de la evaluacion recibida es: " + id_evaluacion);
                printPantalla("el id del estudiante que realizo la evaluacion es: " + id_estudiante);
                
                //calcularPuntaje(Integer.parseInt(id_evaluacion));
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
    
    private String getInfoEstudiante(int id_estudiante){
        return "Retornando la información del estudiante";
    }
}
