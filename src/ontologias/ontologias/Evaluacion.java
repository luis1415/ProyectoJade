package ontologias;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: evaluacion
* @author ontology bean generator
* @version 2017/05/11, 03:58:52
*/
public class Evaluacion implements Concept {

   /**
* Protege name: nota
   */
   private float nota;
   public void setNota(float value) { 
    this.nota=value;
   }
   public float getNota() {
     return this.nota;
   }

   /**
* Protege name: estudiantes
   */
   private List estudiantes = new ArrayList();
   public void addEstudiantes(Estudiante elem) { 
     List oldList = this.estudiantes;
     estudiantes.add(elem);
   }
   public boolean removeEstudiantes(Estudiante elem) {
     List oldList = this.estudiantes;
     boolean result = estudiantes.remove(elem);
     return result;
   }
   public void clearAllEstudiantes() {
     List oldList = this.estudiantes;
     estudiantes.clear();
   }
   public Iterator getAllEstudiantes() {return estudiantes.iterator(); }
   public List getEstudiantes() {return estudiantes; }
   public void setEstudiantes(List l) {estudiantes = l; }

   /**
* Protege name: descripcion
   */
   private String descripcion;
   public void setDescripcion(String value) { 
    this.descripcion=value;
   }
   public String getDescripcion() {
     return this.descripcion;
   }

   /**
* Protege name: id_asignatura
   */
   private int id_asignatura;
   public void setId_asignatura(int value) { 
    this.id_asignatura=value;
   }
   public int getId_asignatura() {
     return this.id_asignatura;
   }

}
