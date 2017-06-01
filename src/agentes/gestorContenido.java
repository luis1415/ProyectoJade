package agentes;

import Parents.SuperAgent;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ejemplos.Conexion;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.sql.PreparedStatement;
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
                printPantalla("Me llego un mensaje con el siguiente contenido - " + stringJson);
                
                JsonElement arrayElement = new JsonParser().parse(stringJson);
                String funcion = arrayElement.getAsJsonObject().get("funcion").getAsString();
                System.err.println(funcion);
                // {"funcion":"guardarAsignatura","id_evaluacion":1,"id_estudiante":10}
                // {"funcion":"guardarAsignatura","descripcion":"mate"}
                // {"funcion":"guardarPreguntasEvaluacion","descripcion":"pregunta 1","id_evaluacion":1}
                if ("guardarAsignatura".equals(funcion)){
                    this.guardarAsignatura(arrayElement);
                }else if ("guardarPreguntasEvaluacion".equals(funcion)){
                    this.guardarPreguntasEvaluacion(arrayElement);
                }else if ("guardarPreguntasSimulacro".equals(funcion)){
                    this.guardarPreguntasSimulacro(arrayElement);
                }else if ("seleccionarAsignatura".equals(funcion)){
                    
                    String Contenido = seleccionarAsignatura();
                    
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent(Contenido);
                    myAgent.send(reply);  //Descomentar luego
                }else{
                    System.err.println("Llego un mensaje pero no se que hacer con el");
                }
            } else {
                block();
            }
        }
        
        
        public void guardarAsignatura(JsonElement arrayElement) {
            String descripcion = arrayElement.getAsJsonObject().get("descripcion").getAsString();
            // consulta SQL que inserte en la tabla Asignatura 
            try {
                Statement st = cn.createStatement();
                PreparedStatement pst = cn.prepareStatement("INSERT INTO asignatura (descripcion) VALUES ('"+descripcion+"') ", Statement.RETURN_GENERATED_KEYS);
                // para recuperar el ultimo id insertado  
                pst.executeUpdate();  
                ResultSet keys = pst.getGeneratedKeys();    
                keys.next();  
                int key = keys.getInt(1);
                System.out.println("key: " + key);
                pst.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
            printPantalla("descripcion: " + descripcion);
            
        }
        
        public void guardarPreguntasEvaluacion(JsonElement arrayElement){
            String descripcion = arrayElement.getAsJsonObject().get("descripcion").getAsString();
            int id_evaluacion = arrayElement.getAsJsonObject().get("id_evaluacion").getAsInt();

            // Falta consulta SQL que inserte en la tabla Preguntas y preguntas_evaluacion 
            try {
                Statement st = cn.createStatement();
                PreparedStatement pst = cn.prepareStatement("INSERT INTO pregunta (descripcion) VALUES ('"+descripcion+"') ", Statement.RETURN_GENERATED_KEYS);
                // para recuperar el ultimo id insertado  
                pst.executeUpdate();  
                ResultSet id_pregunta = pst.getGeneratedKeys();    
                id_pregunta.next();  
                int id_pr = id_pregunta.getInt(1);
                PreparedStatement pst2 = cn.prepareStatement("INSERT INTO preguntas_evaluacion (id_pregunta, id_evaluacion) VALUES ("+id_pr+", "+id_evaluacion+") ", Statement.RETURN_GENERATED_KEYS);
                pst.executeUpdate();
                pst2.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
            printPantalla("descripcion: " + descripcion);
            printPantalla("descripcion recibida es: " + descripcion);
            printPantalla("id_evaluacion recibida es: " + id_evaluacion);
        }
        
        public void guardarPreguntasSimulacro(JsonElement arrayElement){
            String descripcion = arrayElement.getAsJsonObject().get("descripcion").getAsString();
            int id_evaluacion = arrayElement.getAsJsonObject().get("id_evaluacion").getAsInt();

            // Falta consulta SQL que inserte en la tabla Preguntas y preguntas_evaluacion donde simulacro sea true

            printPantalla("descripcion recibida es: " + descripcion);
            printPantalla("id_evaluacion recibida es: " + id_evaluacion);
        }
        
        public String seleccionarAsignatura(){
            // Consulta SQL que retorne el listado de asignatura y luego crear el json con ese listado
            
            JsonObject jsonObject = new JsonObject();
            
            JsonObject jsonObjectTemp = new JsonObject();
            //jsonObject.addProperty("asignaturas", id_evaluacion);
            //jsonObject.addProperty("id_estudiante", id_estudiante);

            return jsonObject.toString();
        }
        
        /*
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
        
        */
    }
   
    private class ObtenerResultadosEstudiante extends OneShotBehaviour {

        @Override
        public void action() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    
}
