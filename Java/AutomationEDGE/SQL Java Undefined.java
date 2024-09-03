import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws ProcessStudioException {
  if (first) {
    first = false;
  }

  Object[] r = getRow();

  if (r == null) {
    setOutputDone();
    return false;
  }

  r = createOutputRow(r, data.outputRowMeta.size());

  /////////////////////////////////////////////////ENTRADA
  String strSqlECH = get(Fields.Out, "strSqlECH").getString(r);
  String usuarioSqlServer = get(Fields.In, "usuarioSqlServer").getString(r);
  String senhaSqlServer = get(Fields.In, "senhaSqlServer").getString(r);
  String conexaoStringSqlServer = get(Fields.In, "conexaoStringSqlServer").getString(r);
  /////////////////////////////////////////////////ENTRADA

  List<Map<String, String>> results = DatabaseOperations.executeQuery(conexaoStringSqlServer,usuarioSqlServer,senhaSqlServer,strSqlECH);
  
  if (results.size() > 0){
	logBasic("================================(Resultados encontrados: "+results.size()+")================================");
	Map<String, String> row = (Map<String, String>)results.get(0);
	String hord = (String)row.get("HORD");
	get(Fields.In, "pedidoTitan").setValue(r, hord);
	get(Fields.Out, "resultadoEch").setValue(r, "Sim");
  } else {
	logBasic("================================(Nao encontrado)================================");
	get(Fields.Out, "resultadoEch").setValue(r, "Nao");
  }
  

  putRow(data.outputRowMeta, r);

  return true;
}



public class DatabaseConnection {
    public static Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

public class DatabaseOperations {

	public static List<Map<String, String>> executeQuery(String url, String user, String password, String query) {
        List<Map<String, String>> results = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection(url, user, password);
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = rs.getString(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public static void executeUpdate(String url, String user, String password, String updateQuery) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection(url, user, password);
            stmt = conn.prepareStatement(updateQuery);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}