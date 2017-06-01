package agentes;

import Parents.SuperAgent;
import com.google.gson.JsonObject;
import ejemplos.Conexion;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class gestorContenido extends SuperAgent {

    private String[][] servicios = {
        {
            "guardarAsignatura",
            "Guardar asignatura en el Sistema"
        },
        {
            "guardarPreguntasEvaluacion",
            "Guardar preguntas de la evaluacion en el sistema."
        },
        {
            "guardarPreguntasSimulacro",
            "Guardar preguntas de simulacros en el sistema."
        },
        {
            "seleccionarAsignatura",
            "Seleccionar asignatura a estudiar."
        },
        {
            "seleccionarEvaluacionResolver",
            "Seleccionar evaluacion a resolver."
        },
        {
            "seleccionarSimluacroResolver",
            "seleccionar simualcro a resolver."
        },
        {
            "obtenerPreguntasEvaluacion",
            "Obtener preguntas de evaluación."
        },
        {
            "obtenerPreguntasSimulacro",
            "Obtener preguntas de simulacros."
        },
        {
            "enviarRespuestasEvaluacion",
            "Enviar respuestas de evaluación."
        },
        {
            "enviarRespuestasSimulacro",
            "Enviar respuestas de simulacro."
        },
        {
            "seleccionarPreguntasEvaluacion",
            "Seleccionar preguntas de evaluación."
        }
    };
    
    @Override
    protected void setup() {
       this.printPantalla("Esta corriendo");
        // Registro el servicio que presta este agente
        this.cargarServicios(this.servicios);
        this.establecerConexionDB();
        
        //Agregar comportamientos 
        this.addBehaviour(new guardarRespuestasDeEvaluacion());
        this.addBehaviour(new EsperarAccion());
    }
    
    private class guardarAsignatura extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de guardar asignatura en el sistema.");
        }
    }

    private class guardarPreguntasEvaluacion extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de guardar preguntas de evaluación.");
        }
    }
 
    private class guardarPreguntasSimulacro extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de guardar preguntas de simulacro.");
        }
    }
    
    private class seleccionarAsignatura extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de seleccionar asignatura a estudiar.");
        }
    }    

    private class seleccionarEvaluacionResolver extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de seleccionar evaluacion a resolver.");
        }
    }
 
    private class seleccionarSimulacroResolver extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de seleccionar simulacro a resolver.");
        }
    }
 
    private class obtenerPreguntasEvaluacion extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de obtener preguntas de evaluación.");
        }
    }
    
    private class obtenerPreguntasSimulacro extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de obtener preguntas de simulacro.");
        }
    }
 
    private class enviarRespuestasEvaluacion extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de enviar respuestas de evaluación.");
        }
    }

    private class enviarRespuestasSimulacro extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de enviar respuestas de simulacro.");
        }
    }
    
     private class seleccionarPreguntasEvaluacion extends OneShotBehaviour {
        @Override
        public void action(){
            printPantalla("Utilizando Rol de selecionar preguntas de evaluación.");
        }
    }   
    
     
    private boolean guardarRespuestasEvaluacion(){
        //se debe devolver si guardo o no
        return true;
    }
    
    private class guardarRespuestasDeEvaluacion extends OneShotBehaviour {
        @Override
        public void action() {
            printPantalla("Entre en guardarPreguntasDeEvaluacion");
            
            //se debe insertar la respuesta seleccionada de una evaluación y obtener el id del estudiante
            int id_evaluacion = 1;
            int id_estudiante = 10;
            
            if (guardarRespuestasEvaluacion()){
                String ServicioEjecutar = "calcularPuntaje";
                printPantalla("Se esta enviando el el msj al agente que tenga el servicio: " + ServicioEjecutar);
                
                DFAgentDescription[] agentesEncontrados = null;
                try {
                    agentesEncontrados = buscarAgentesPorServicio(ServicioEjecutar); //SE envía el type del servicio que uno necesita ejecutar
                    if (agentesEncontrados.length == 0) {
                       printPantalla(getAID().getName() + ": Ningun agente ofrece el servicio deseado");
                    }
                } catch (FIPAException ex) {
                    Logger.getLogger(recomendador.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < agentesEncontrados.length; ++i) {
                    
                    printPantalla(getAID().getName() + ": El agente " + agentesEncontrados[i].getName() + " ofrece los siguientes servicios:");
                    
                    Iterator servicios = agentesEncontrados[i].getAllServices();
                    int j = 1;

                    while (servicios.hasNext()) {
                        ServiceDescription servicio = (ServiceDescription) servicios.next();
                        System.out.println(j + "- " + servicio.getName());
                        j++;
                    }
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id_evaluacion", id_evaluacion);
                    jsonObject.addProperty("id_estudiante", id_estudiante);
                    
                    AID AgenteReceptor = agentesEncontrados[i].getName();
                    String Contenido = jsonObject.toString();
                    int TipoMensaje = ACLMessage.INFORM;
                    String TipoProtocolo = FIPANames.InteractionProtocol.FIPA_REQUEST;
                    
                    enviarMensajeAOtroAgente(AgenteReceptor, Contenido, TipoMensaje, TipoProtocolo);
                }
            }else{
                printPantalla("Ocurrió un error en el guardado de la respuesta");
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
    
    
}
