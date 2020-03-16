package fr.godartm.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {

    public static Connection connection;
    public static String urlbase;
    public static String port;
    public static String host;
    public static String database;
    public static String user;
    public static String pass;

    public SqlConnection(String urlbase, String host, String port, String database, String user, String pass) {
        SqlConnection.urlbase = urlbase;
        SqlConnection.host = host;
        SqlConnection.port = port;
        SqlConnection.database = database;
        SqlConnection.user = user;
        SqlConnection.pass = pass;
    }

    public static synchronized void openConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user="
                    + user + "&password=" + pass + "&*&autoReconnect=true");
            System.out.println("La connexion au serveur mysql c'est effectuer sans probleme");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if ((connection != null) && (!connection.isClosed())) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() {
        return SqlConnection.connection;
    }

    public static boolean isConnected() {
        return SqlConnection.connection != null;
    }


}