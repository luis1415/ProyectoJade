/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import Parents.SuperAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class recomendador extends SuperAgent {
    
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
    
    
    @Override
    protected void setup() {
       this.printPantalla("Esta corriendo");
        // Registro el servicio que presta este agente
        this.cargarServicios(this.servicios);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(recomendador.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Agregar comportamientos
        //this.addBehaviour();
    }
    
    
    
    
    /*
    private class EjemploDeEnvioMensajeConBusquedaDeAgentePorServicio extends OneShotBehaviour {

        @Override
        public void action() {
            DFAgentDescription[] resultados = null;
            try {
                resultados = buscarAgentesPorServicio("vendedor"); //SE envía el type del servicio que uno necesita ejecutar
                if (resultados.length == 0) {
                    System.out.println(getAID().getName() + ": Ningun agente ofrece el servicio deseado");
                }
            } catch (FIPAException ex) {
                Logger.getLogger(recomendador.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                
                
                //myAgent.addBehaviour(new EjemploDeRecepcionConRespuesta());
            }
        }

    }

    private class EjemploDeRecepcionConRespuesta extends CyclicBehaviour {

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
    }*/
   
}
