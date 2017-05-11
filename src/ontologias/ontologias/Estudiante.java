package ontologias;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: estudiante
* @author ontology bean generator
* @version 2017/05/11, 03:58:52
*/
public class Estudiante implements Concept {

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
* Protege name: id
   */
   private int id;
   public void setId(int value) { 
    this.id=value;
   }
   public int getId() {
     return this.id;
   }

}
