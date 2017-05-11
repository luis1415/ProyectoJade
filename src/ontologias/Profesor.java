package ontologias;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: profesor
* @author ontology bean generator
* @version 2017/05/11, 03:58:52
*/
public class Profesor implements Concept {

   /**
* Protege name: nombre
   */
   private String nombre;
   public void setNombre(String value) { 
    this.nombre=value;
   }
   public String getNombre() {
     return this.nombre;
   }

   /**
* Protege name: id_profesor
   */
   private int id_profesor;
   public void setId_profesor(int value) { 
    this.id_profesor=value;
   }
   public int getId_profesor() {
     return this.id_profesor;
   }

}
