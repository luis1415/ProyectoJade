/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import java.sql.Connection;
import java.sql.DriverManager;

public class SuperAgent  extends Agent {
    public Connection cn;
    
    /*
        Se realiza la asignacion de los servicios que ofrece este agente.
        Los servicios son las funciones que realiza y pueden ser utilizadas por otros agentes
    */
    public void cargarServicios(String[][] servicios){
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            
            ServiceDescription sd;
            for (String[] servicio : servicios) {
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
    
    public Connection conexion(){
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/multiagentes","root","");
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean verificarConeccion(Connection cn){
        if(cn == null){
            this.printPantalla("Hay un error en la conexión de base de datos");
            return false;          
        } else{
            System.err.println("Se conecto");
            return true;
        }
    }
    
    public void printPantalla(String msj){
        System.out.println(getAID().getName() + ": " + msj);
    }
    
    /*
        Esta función busca a un agente por un servicio determinado de acuerdo al Typo del servicio que debe ser unico
        en todos los agentes.
    */
    public DFAgentDescription[] buscarAgentesPorServicio(String Type) throws FIPAException {
        ServiceDescription servicio = new ServiceDescription();
        servicio.setType(Type);
        //servicio.setType("vendedor");
        //servicio.setName("vender peliculas");

        // Plantilla de descripción que busca el agente
        DFAgentDescription descripcion = new DFAgentDescription();

        // Servicio que busca el agente
        descripcion.addServices(servicio);

        // Todas las descripciones que encajan con la plantilla proporcionada en el DF
        return DFService.search(this, descripcion);
    }
    
    public void establecerConexionDB(){
        this.cn = this.conexion();
        this.verificarConeccion(cn);
    }
    
    public void enviarMensajeAOtroAgente(AID AgenteReceptor, String Contenido, int TipoMensaje, String TipoProtocolo){
        this.printPantalla("Se incio el envío enviarMensajeAOtroAgente al agente: " + AgenteReceptor + " con el tipo de mensaje: " + TipoMensaje + " y el protocolo: " + TipoProtocolo);
        ACLMessage mensaje = new ACLMessage();
        mensaje.addReceiver(AgenteReceptor);
        mensaje.setContent(Contenido);
        mensaje.setProtocol(TipoProtocolo);
        mensaje.setPerformative(TipoMensaje);
        send(mensaje);
    }
}
