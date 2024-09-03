package com.sqlite;

import jdk.swing.interop.SwingInterOpUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.devtools.v85.database.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InserirEmMassa {
    public static void main(String[] args) throws IOException, SQLException {
        var t = "var iframe = document.querySelector('#iframe_313')\n" +
                "var doc = iframe.contentWindow.document || iframe.contentDocument\n" +
                "\n" +
                "return doc.querySelector('#control_6').click()";


        inserirEmMassa1(new JSONObject(" {\"nomeTabela\":\"registrosSIMCN\",\"diretorioArquivo\":\"C:\\\\Users\\\\sergi\\\\Documents\\\\Dev\\\\projects-java\\\\projetos-gerais\\\\src\\\\main\\\\resources\\\\anderson\\\\SIACI-499921801-05122022.txt\",\"diretorioBanco\":\"C:\\\\Users\\\\sergi\\\\Documents\\\\Dev\\\\projects-java\\\\projetos-gerais\\\\src\\\\main\\\\resources\\\\anderson\\\\dados.db\",\"inicioLinha\":6,\"colunas\":\"[[\\\"SUBCONTA\\\",[2,2,27],\\\"fixo\\\"],[\\\"UNIDADE\\\",[1,8]],[\\\"DATA_MOVIM\\\",[8,19]],[\\\"DATA_EFETI\\\",[19,31]],[\\\"ORIGEM\\\",[31,38]],[\\\"EVENTO\\\",[38,48]],[\\\"DOCTO\\\",[48,55]],[\\\"OL\\\",[55,64]],[\\\"CONTRATO\\\",[64,80]],[\\\"TP\\\",[80,85]],[\\\"PEDIDO\\\",[85,95]],[\\\"DEBITO\\\",[95,113]],[\\\"CREDITO\\\",[113,133]]]\"}"));
    }

    public static void atualizarReplace(String database, String tabela, String coluna, String search, String replaceWith) {
        Connection conn = null;
        Connection conn1 = null;
        try {
            /* variaveis de conexao */
            String url = "jdbc:sqlite:" + database;
            conn = DriverManager.getConnection(url);
            conn1 = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            Statement statement1 = conn1.createStatement();

            String sl = String.format("SELECT rowid, %s FROM %s", coluna, tabela);
            List<String[]> ls = new ArrayList<>();

            try (Connection cn = DriverManager.getConnection(url)) {
                try (Statement st = cn.createStatement();
                     ResultSet rs = st.executeQuery(sl)) {
                    while (rs.next()) {
                        ls.add(new String[]{rs.getString(coluna), rs.getString("rowid")});
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            for (int i = 0; i < ls.size(); i++) {
                String rowid = ls.get(i)[1];
                String col = ls.get(i)[0];

                String day = col.split("/")[0];
                String month = col.split("/")[1];
                String year = col.split("/")[2];
                String ndate = String.format("%s-%s-%s", year, month, day);
                String query = String.format("update %s set %s = '%s' where rowid = %s", tabela, coluna, ndate, rowid);
                statement.executeUpdate(query);
                System.out.println(col);
            }

            System.out.println();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void atualizaar(ResultSet rs, Statement statement, String tabela, String coluna) throws SQLException {
        int rowid = rs.getInt("rowid");
        String day = coluna.split("/")[0];
        String month = coluna.split("/")[1];
        String year = coluna.split("/")[2];
        String ndate = String.format("%s-%s-%s", year, month, day);
        String query = String.format("update %s set %s = '%s' where rowid = %s", tabela, coluna, ndate, rowid);
        statement.executeUpdate(query);
        System.out.println(coluna);
    }

    /**
     * Este é método insere em massa de forma muito rápida, mas é muito grande.     *
     */
    public static void inserirEmMassa(List<Object[]> args, int linhaInicio, int range, String nomeTabela, String diretorioDb, String diretorioArquivo) {
        Connection conn = null;
        try {
            /* variaveis de conexao */
            String url = "jdbc:sqlite:" + diretorioDb;
            conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);

            /* variaveis de arquivos */
            List<String> linhasArquivo = Files.readAllLines(Paths.get(diretorioArquivo));
            StringBuilder sb = new StringBuilder();
            StringBuilder values = new StringBuilder();

            /* loop nas linhas do arquivo */
            for (int l = linhaInicio - 1; l < linhasArquivo.size(); l++) {
                String line = linhasArquivo.get(l);
                sb.append("(");
                for (int a = 0; a < args.size(); a++) {
                    try {
                        Object[] objs = args.get(a);
                        switch (objs.length) {
                            case 3:
                                sb.append("'" + objs[1] + "',");
                                break;
                            default:
                                int[] cols = (int[]) objs[1];
                                String value = line.substring(cols[0] - 1, cols[1] - 1).trim();
                                sb.append("'" + value + "',");
                                break;
                        }
                    } catch (Exception e) {

                        sb.append("'',");
                    }
                }

                /* fecha a estrutura sql */
                values.append(sb.toString().replaceAll(",$", "") + "),");
                //sbf.append(sb.toString().replaceAll(",$","")+"),");
                sb = new StringBuilder();

                /* verifica se é o momento de inserir no banco */
                if (l % range == 0) {
                    String colunas = String.format("(%s)", join(",", args, 0));
                    String query = String.format("insert into %s %s values %s", nomeTabela, colunas, values.toString().replaceAll(",$", ""));
                    statement.executeUpdate(query);
                    sb = new StringBuilder();
                    values = new StringBuilder();
                } else if (l + 1 >= linhasArquivo.size() && l % range != 0) {
                    Object[] arr = args.stream().map(x -> x[0]).toArray();
                    String colunas = String.format("(%s)", join(",", args, 0));
                    String query = String.format("insert into %s %s values %s", nomeTabela, colunas, values.toString().replaceAll(",$", ""));
                    statement.executeUpdate(query);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {

        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static List<Object[]> validarRegistros(List<Object[]> argsP, String diretorioArquivo) {
        List<Object[]> args = argsP;
        try {
            List<String> linhasArquivo = Files.readAllLines(Paths.get(diretorioArquivo));
            for (int i = 0; i < args.size(); i++) {
                Object[] objs = args.get(i);

                switch (objs.length) {
                    case 4: {
                        int line = (int) objs[1];
                        int[] posicoes = (int[]) objs[2];
                        String textLine = linhasArquivo.get(line - 1);
                        args.set(i, new Object[]{objs[0], textLine.substring(posicoes[0] - 1, posicoes[1] - 1), "valor_fixo"});
                    }
                    break;
                    default:
                        args.set(i, objs);
                        break;
                }
            }
        } catch (IOException e) {
            return args;
        }
        return args;
    }

    public static String join(String separador, List<Object[]> array, int coluna) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            Object[] arr = (Object[]) array.get(i);
            sb.append(String.format("%s, ", (String) arr[coluna]));
        }
        return sb.toString().replaceAll(", $", "");
    }

    /**
     * Este é método insere em massa, mas é muito lento, mas tem menos scripts.     *
     */
    public static void inserirEmMassa1(JSONObject jsonObject) throws IOException, SQLException {
        /* json */
        int linhaInicio = jsonObject.getInt("inicioLinha");
        String nomeTabela = jsonObject.getString("nomeTabela");
        String diretorioDb = jsonObject.getString("diretorioBanco");
        String diretorioArquivo = jsonObject.getString("diretorioArquivo");
        JSONArray jsonArray = new JSONArray(jsonObject.get("colunas").toString());

        /* ler arquivos */
        List<String> linhasArquivo = Files.readAllLines(Paths.get(diretorioArquivo));

        /* sql */
        String url = "jdbc:sqlite:" + diretorioDb;
        Connection conn = DriverManager.getConnection(url);
        conn.setAutoCommit(false);
        String estruturaSql = gerarEstrutura(jsonArray, nomeTabela);
        PreparedStatement pstmt = conn.prepareStatement(estruturaSql);

        for (int l = linhaInicio - 1; l < linhasArquivo.size(); l++) {
            String line = linhasArquivo.get(l);

            for (int i = 0; i < jsonArray.length();i++){
                try {
                    JSONArray array = jsonArray.getJSONArray(i);
                    JSONArray posicao = array.getJSONArray(1);

                    if (posicao.length() == 3){
                        String value = linhasArquivo.get(posicao.getInt(0) - 1).substring(posicao.getInt(1) - 1, posicao.getInt(2) - 1).trim();
                        pstmt.setString(i + 1, value);
                        continue;
                    }

                    String value = line.substring(posicao.getInt(0) - 1, posicao.getInt(1) - 1).trim();
                    pstmt.setString(i + 1, value);

                } catch (Exception e) {
                    pstmt.setNull(i+1, 0);
                }
                System.out.println();
            }
            pstmt.addBatch();
        }
        pstmt.executeBatch();

        conn.commit();
        pstmt.close();
        conn.close();
    }

    public static String gerarEstrutura(JSONArray jsonArray, String tabela) {
        StringBuilder sb = new StringBuilder();
        List<String> interrogacoes = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String strArray = jsonArray.get(i).toString();
            JSONArray array = new JSONArray(strArray);
            String nomeColuna = array.getString(0);
            sb.append(String.format("%s,", nomeColuna));
            interrogacoes.add("?");
        }
        String f = String.format("INSERT INTO %s (%s) values (%s)", tabela, sb.toString().replaceAll(",$", ""), String.join(",", interrogacoes));
        return f;
    }
}

