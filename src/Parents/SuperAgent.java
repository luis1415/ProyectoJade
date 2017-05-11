/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Parents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class SuperAgent  extends Agent {
    
    
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
}
