/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import Parents.SuperAgent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class recomendador extends SuperAgent {

    private String nombrePelicula;
    private int presupuestoPelicula;
    DFAgentDescription[] resultados;
    
    public String servicios[][] = {
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
    
    public recomendador(){
        System.err.println("entre aquii");
    }
    
    @Override
    protected void setup() {
       this.printPantalla("Esta corriendo");
        // Registro el servicio que presta este agente
        this.cargarServicios(this.servicios);
        
        nombrePelicula = "El señor de los anillos";
        presupuestoPelicula = 200000;
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(recomendador.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            buscarAgentesPorServicio("vendedor"); //SE envía el type del servicio que uno necesita ejecutar
        } catch (FIPAException ex) {
            Logger.getLogger(recomendador.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Agregar comportamientos
        this.addBehaviour(new BuscarPelicula());
    }
    
    /*
        Esta función busca a un agente por un servicio determinado de acuerdo al Typo del servicio que debe ser unico
        en todos los agentes.
    */
    public void buscarAgentesPorServicio(String Type) throws FIPAException {
        ServiceDescription servicio = new ServiceDescription();
        servicio.setType(Type);
        //servicio.setType("vendedor");
        //servicio.setName("vender peliculas");

        // Plantilla de descripción que busca el agente
        DFAgentDescription descripcion = new DFAgentDescription();

        // Servicio que busca el agente
        descripcion.addServices(servicio);

        // Todas las descripciones que encajan con la plantilla proporcionada en el DF
        resultados = DFService.search(this, descripcion);

        if (resultados.length == 0) {
            System.out.println(getAID().getName() + ": Ningun agente ofrece el servicio deseado");
        }
    }

    private class BuscarPelicula extends OneShotBehaviour {

        @Override
        public void action() {

            for (int i = 0; i < resultados.length; ++i) {
                System.out.println(getAID().getName() + ": El agente " + resultados[i].getName() + " ofrece los siguientes servicios:");
                Iterator servicios = resultados[i].getAllServices();
                int j = 1;
                
                System.out.println(getAID().getName() + ": /n");
                
                while (servicios.hasNext()) {
                    ServiceDescription servicio = (ServiceDescription) servicios.next();
                    System.out.println(j + "- " + servicio.getName());
                    j++;
                }

                //enviar mensaje de solicitud a cada uno de los agentes
                System.out.println(getAID().getName() + ": enviar mensaje de busqueda");
                
                ACLMessage pregunta = new ACLMessage();
                pregunta.addReceiver(resultados[i].getName());
                pregunta.setContent(nombrePelicula);
                pregunta.setPerformative(ACLMessage.INFORM);
                
                send(pregunta);
                
                
                myAgent.addBehaviour(new EsperarPrecio());
            }
        }

    }

    private class EsperarPrecio extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(100);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                int valor = Integer.parseInt(msg.getContent().toString());
                ACLMessage reply = msg.createReply();
                System.out.println(getAID().getName() + ":El precio de la pelicula que estoy buscando es: " + valor);
                System.out.println(getAID().getName() + ":El agente que lo posee es: " + msg.getSender());

                if (valor <= presupuestoPelicula) {
                    System.out.println(getAID().getName() + ":COMPRANDO PELICULA...");
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    System.out.println(getAID().getName() + ":NO ALCANZA EL DINERO");
                }
                myAgent.send(reply);
                myAgent.doDelete();
            } else{
                block();
            }
        }
    }
    
    private void printPantalla(String msj){
        System.out.println(getAID().getName() + msj);
    }
}
