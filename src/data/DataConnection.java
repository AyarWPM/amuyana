package data;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataConnection {

    private Connection connection;
    
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    public static void setValues(String url, String username, String password) {
        DataConnection.URL = url;
        DataConnection.USERNAME = username;
        DataConnection.PASSWORD = password;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public void connect(){

        try {
            //Cargar driver
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + this.URL + "?autoReconnect=true&useSSL=false", this.USERNAME, this.PASSWORD);
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static boolean testConexion(String url, String username, String password){
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + url + "?autoReconnect=true&useSSL=false", username, password);
            return connection.isValid(0);
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    // metodo para cerrar conexión
    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DataConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
