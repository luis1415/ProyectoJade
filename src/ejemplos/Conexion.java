package ejemplos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
public class Conexion {

    Connection conectar=null;
    public Connection conexion(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conectar=DriverManager.getConnection("jdbc:mysql://localhost:3306/multiagentes","root","");
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        return conectar;
    }
    
    
    public static void main(String[] args) {
        Conexion cc = new Conexion();
        Connection cn = cc.conexion();
        
        if(cn == null){
            JOptionPane.showMessageDialog(null, "no conectado");            
        }
        else{        
            JOptionPane.showMessageDialog(null, "conectado");          
        }
        
        try {
            // insertar

            PreparedStatement pst = cn.prepareStatement("INSERT INTO prueba (id, descripcion) VALUES (3, 'biologia') ", 0);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("select * from prueba");
            while(rs.next()){
                System.out.println("materia: " + rs.getString(2));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}

