package agentes;

import Parents.SuperAgent;
import ejemplos.Conexion;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
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
        
        //Agregar comportamientos 
        this.addBehaviour(new guardarRespuestasDeEvaluacion());
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
                        System.out.println(getAID().getName() + ": Ningun agente ofrece el servicio deseado");
                    }
                } catch (FIPAException ex) {
                    Logger.getLogger(recomendador.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int i = 0; i < agentesEncontrados.length; ++i) {
                    
                    System.out.println(getAID().getName() + ": El agente " + agentesEncontrados[i].getName() + " ofrece los siguientes servicios:");
                    
                    Iterator servicios = agentesEncontrados[i].getAllServices();
                    int j = 1;

                    while (servicios.hasNext()) {
                        ServiceDescription servicio = (ServiceDescription) servicios.next();
                        System.out.println(j + "- " + servicio.getName());
                        j++;
                    }
                    
                    AID AgenteReceptor = agentesEncontrados[i].getName();
                    String Contenido = "12";
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
