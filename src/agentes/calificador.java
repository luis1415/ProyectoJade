package agentes;

import ejemplos.*;
import Parents.SuperAgent;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class calificador extends SuperAgent {

    private String[][] servicios = {
        {
            "calcularPuntaje",
            "Calcular puntaje del estudiante."
        },
        {
            "obtenerResultadoEstudiante",
            "Obtener resultado de estudiante."
        }
    };
    
    @Override
    protected void setup() {
       this.printPantalla("Esta corriendo");
        // Registro el servicio que presta este agente
        this.cargarServicios(this.servicios);
        
        //Agregar comportamientos 
        this.addBehaviour(new EsperarRespuestas());
        this.addBehaviour(new calcularPuntaje());
        
    }

    private class obtenerResultado extends CyclicBehaviour {
        @Override
        public void action() {
            printPantalla("Utilizando rol de obtener resultado del estudiante.");
    }}    
    
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
                
                /* no da en este punto por que se mete id_estudiante */
                calcularPuntaje1(id_estudiante);
          try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            Statement st1 = cn.createStatement();
            ResultSet rs2 = st1.executeQuery("SELECT * FROM respuestas_estudiante_evaluacion WHERE id_estudiante="+id_estudiante);
            rs2.next();
            System.out.println("Estudiante: " + rs2.getString(1));
            ;
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
                //calcularPuntaje(Integer.parseInt(id_evaluacion));
            } else {
                block();
            }
        }
    }

    private void CalcularPuntuacion(int evaluacion, int estudiante){
        
        try {
            Statement st1 = cn.createStatement();
            ResultSet rs1 = st1.executeQuery("select * from evaluacion where id="+evaluacion);
            Statement st2 = cn.createStatement();
            ResultSet rs2 = st2.executeQuery("select * from estudiantes where id="+estudiante);           
            
            
            while(rs1.next()){
                System.out.println("nota: " + rs1.getString(2));
                System.out.println("descripcion: " + rs1.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private class calcularPuntaje extends CyclicBehaviour {
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
                int estud = Integer.parseInt(id_estudiante);
                calcularPuntaje1(estud);
 
                
                       /* try {
                                    Conexion cc = new Conexion();
        Connection cn = cc.conexion();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from respuestas_estudiante_evaluacion where id_estudiante="+estud);
            while(rs.next()){
                System.out.println("Estudiante: " + rs.getString(1));
                System.out.println("Pregunta: " + rs.getString(2));
                System.out.println("Respuesta: " + rs.getString(3));
                System.out.println("Evaluacion: " + rs.getString(4));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }*/
                myAgent.send(reply);
                myAgent.doDelete();
                
                
            } else {
                block();
            }
        }
    }
    
    private void calcularPuntaje1(int id_estudiante){
        this.printPantalla("Esta calculando el puntaje de la evaluacion o simulacro");
        
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT \n" +
"respuestas_estudiante_evaluacion.id_estudiante,\n" +
"respuestas_estudiante_evaluacion.id_pregunta AS pregunta_contestada,\n" +
"respuestas_estudiante_evaluacion.id_respuesta AS respuesta_contestada,\n" +
"respuestas_estudiante_evaluacion.id_evaluacion,\n" +
"correcta\n" +
"FROM \n" +
"respuestas_estudiante_evaluacion\n" +
"INNER JOIN respuestas_pregunta ON respuestas_estudiante_evaluacion.id_pregunta = respuestas_pregunta.id_pregunta AND \n" +
"respuestas_estudiante_evaluacion.id_respuesta = respuestas_pregunta.id_respuesta WHERE respuestas_estudiante_evaluacion.id_estudiante="+id_estudiante);
            while(rs.next()){
                System.out.println("Pregunta: " + rs.getString(2));
                System.out.println("Respuesta: " + rs.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
   
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            Statement st = cn.createStatement();
            System.out.println("Obteniendo calculo de respuestas...");
            ResultSet rs = st.executeQuery("SELECT * FROM estudiante_evaluacion WHERE id_estudiante="+id_estudiante);
            rs.next();
                System.out.println("ID de la evaluación: " + rs.getString(2));
                System.out.println("NOTA OBTENIDA: " + rs.getString(3));
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String getInfoEstudiante(int id_estudiante){
        return "Retornando la información del estudiante";
    }
}
