package ontologias;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: asignatura
* @author ontology bean generator
* @version 2017/05/11, 03:58:52
*/
public class Asignatura implements Concept {

   /**
* Protege name: tematica
   */
   private String tematica;
   public void setTematica(String value) { 
    this.tematica=value;
   }
   public String getTematica() {
     return this.tematica;
   }

   /**
* Protege name: evaluaciones
   */
   private Evaluacion evaluaciones;
   public void setEvaluaciones(Evaluacion value) { 
    this.evaluaciones=value;
   }
   public Evaluacion getEvaluaciones() {
     return this.evaluaciones;
   }

   /**
* Protege name: codigo
   */
   private int codigo;
   public void setCodigo(int value) { 
    this.codigo=value;
   }
   public int getCodigo() {
     return this.codigo;
   }

}
