package gestionAudits.data;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/gestionaudits";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null,"Erreur de connexion","Erreur",JOptionPane.ERROR_MESSAGE);
            }
        }
        return connection;
    }
}
