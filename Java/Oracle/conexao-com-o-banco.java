package com.oracle;


import java.sql.*;

public class Main {
    // informações de conexão para o banco de dados
    private static final String DB_URL = "jdbc:oracle:thin:@192.168...:1521:p01";
    private static final String DB_USERNAME = "";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // carregar o driver JDBC da Oracle
            //Class.forName("oracle.jdbc.driver.OracleDriver");
//            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

            // conectar-se ao banco de dados
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            statement = connection.createStatement();

            // executar a consulta
            resultSet = statement.executeQuery("SELECT * FROM CUSTOM.TABELA");

            // processar o resultado
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("cod_caso") + " " + resultSet.getString("numero_processo"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // fechar as conexões e resultados
            try { if (resultSet != null) resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
