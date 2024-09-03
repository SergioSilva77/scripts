  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>${junit.version}</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.hamcrest</groupId>
      <artifactId>hamcrest-all</artifactId>
      <version>1.3</version>
      <scope>test</scope>
    </dependency>
  </dependencies>









package com.quality;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class Util {
  public static void main(String[] args) {}
  
  public static void inserirEmMassa(Map<String, int[]> colunas, int linhaInicio, int range, String nomeTabela, String diretorioDb, String diretorioArquivo) {
    Connection conn = null;
    try {
      String url = "jdbc:sqlite:" + diretorioDb;
      conn = DriverManager.getConnection(url);
      Statement statement = conn.createStatement();
      statement.setQueryTimeout(30);
      List<String> lines = Files.readAllLines(Paths.get(diretorioArquivo, new String[0]));
      StringBuilder sb = new StringBuilder();
      StringBuilder sbf = new StringBuilder();
      for (int i = linhaInicio - 1; i < lines.size(); i++) {
        String line = lines.get(i);
        sb.append("(");
        for (String col : colunas.keySet()) {
          try {
            int[] cols = colunas.get(col);
            String value = line.substring(cols[0], cols[1]).trim();
            sb.append("'" + value + "',");
          } catch (Exception e) {
            sb.append("'',");
          } 
        } 
        sbf.append(sb.toString().replaceAll(",$", "") + "),");
        sb = new StringBuilder();
        if (i % range == 0) {
          statement.executeUpdate("insert into " + nomeTabela + " values" + sbf.toString().replaceAll(",$", ""));
          sb = new StringBuilder();
          sbf = new StringBuilder();
        } else if (i + 1 >= lines.size() && i % range != 0) {
          statement.executeUpdate("insert into " + nomeTabela + " values" + sbf.toString().replaceAll(",$", ""));
        } 
      } 
    } catch (SQLException|java.io.IOException e) {
      System.out.println(e.getMessage());
    } finally {
      try {
        if (conn != null)
          conn.close(); 
      } catch (SQLException ex) {
        System.out.println(ex.getMessage());
      } 
    } 
  }
}
