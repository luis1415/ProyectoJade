package ontologias;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ontologiaevaluacion
* @author ontology bean generator
* @version 2017/05/11, 03:58:52
*/
public class Ontologiaevaluacion implements Concept {

   /**
* Protege name: profesores
   */
   private List profesores = new ArrayList();
   public void addProfesores(Profesor elem) { 
     List oldList = this.profesores;
     profesores.add(elem);
   }
   public boolean removeProfesores(Profesor elem) {
     List oldList = this.profesores;
     boolean result = profesores.remove(elem);
     return result;
   }
   public void clearAllProfesores() {
     List oldList = this.profesores;
     profesores.clear();
   }
   public Iterator getAllProfesores() {return profesores.iterator(); }
   public List getProfesores() {return profesores; }
   public void setProfesores(List l) {profesores = l; }

   /**
* Protege name: asignaturas
   */
   private List asignaturas = new ArrayList();
   public void addAsignaturas(Asignatura elem) { 
     List oldList = this.asignaturas;
     asignaturas.add(elem);
   }
   public boolean removeAsignaturas(Asignatura elem) {
     List oldList = this.asignaturas;
     boolean result = asignaturas.remove(elem);
     return result;
   }
   public void clearAllAsignaturas() {
     List oldList = this.asignaturas;
     asignaturas.clear();
   }
   public Iterator getAllAsignaturas() {return asignaturas.iterator(); }
   public List getAsignaturas() {return asignaturas; }
   public void setAsignaturas(List l) {asignaturas = l; }

}
