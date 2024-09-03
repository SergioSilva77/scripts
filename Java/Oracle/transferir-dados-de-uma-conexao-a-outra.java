package com.oracle;


import java.sql.*;

public class Main1 {

    public static void main(String[] args) {
        inserir_em_massa();
    }
    private static String SOURCE_DB_URL = "jdbc:oracle:thin:@DESKTOP-AAAA:1521:xe";
    private static final String SOURCE_DB_USERNAME = "system";
    private static final String SOURCE_DB_PASSWORD = "admin";
    //informações de conexão para a base de dados de destino
    private static final String TARGET_DB_URL = "jdbc:oracle:thin:@DESKTOP-AAAA:1521:xe";
    private static final String TARGET_DB_USERNAME = "AAAA";
    private static final String TARGET_DB_PASSWORD = "AAAA";

    public static void inserir_em_massa() {
        Connection sourceConnection = null;
        Connection targetConnection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // carregar o driver JDBC da Oracle
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // conectar-se à base de dados de origem
            sourceConnection = DriverManager.getConnection(SOURCE_DB_URL, SOURCE_DB_USERNAME, SOURCE_DB_PASSWORD);
            statement = sourceConnection.createStatement();
            resultSet = statement.executeQuery("WITH SYNCHRO_GENERICO AS (select c.informante_est_codigo,\n" +
                    "    ma.nome,\n" +
                    "       c.sis_codigo,\n" +
                    "       c.emitente_pfj_codigo,\n" +
                    "       c.dt_fato_gerador_imposto,\n" +
                    "       c.numero,\n" +
                    "       c.dof_import_numero,\n" +
                    "       c.uf_codigo_emitente,\n" +
                    "       c.destinatario_pfj_codigo,\n" +
                    "       c.uf_codigo_destino,\n" +
                    "       c.dh_emissao,\n" +
                    "       i.cfop_codigo,\n" +
                    "      -- i.stc_codigo as CST_ICMS,\n" +
                    "      -- i.merc_codigo,\n" +
                    "      -- i.NBM_CODIGO,\n" +
                    "      -- m.descricao,\n" +
                    "       c.ind_entrada_saida,\n" +
                    "       sum(nvl(qtd,0))AS QTD,\n" +
                    "       sum(nvl(VL_CONTABIL,0)) AS VALOR_CONTABIL,\n" +
                    "       sum(nvl(vl_ajuste_preco_total,0)) as DESCONTO,    --fase3\n" +
                    "       sum(nvl(VL_base_ICMS,0)) AS BASE_DE_CALCULO,\n" +
                    "       sum(nvl(VL_TRIBUTAVEL_ICMS,0)) AS BASE_ICMS_NORMAL,\n" +
                    "       sum(nvl(aliq_icms,0))AS ALIQ_ICMS,\n" +
                    "       sum(nvl(VL_ICMS,0)) + sum(nvl(VL_ICMS_FCP,0)) AS VALOR_ICMS,\n" +
                    "     --sum(nvl(VL_ICMS_FCP,0)) AS VALOR_ICMS_FCP,\n" +
                    "       sum(nvl(VL_base_stf,0)) AS BASE_CALCULO_ICMS_ST,\n" +
                    "       sum(nvl(aliq_stf,0))AS ALIQ_ICMS_ST,\n" +
                    "       sum(nvl(VL_STF,0)) +  sum(nvl( i.vl_icms_fcpst,0)) + sum(nvl( i.vl_stt,0)) AS VALOR_ICMS_ST,       --fase3\n" +
                    "       sum(nvl(aliq_difa,0)) AS ALIQ_DIFA,\n" +
                    "       sum(nvl(VL_difa,0)) AS VALOR_DIFAL,\n" +
                    "       sum(nvl(vl_imposto_pis,0)) as VALOR_PIS,\n" +
                    "       sum(nvl(vl_imposto_cofins,0)) as VALOR_COFINS,\n" +
                    "       sum(nvl(VL_BASE_IPI,0)) AS VL_BASE_IPI,\n" +
                    "       sum(nvl(ALIQ_IPI,0)) AS ALIQ_IPI,\n" +
                    "       sum(nvl(VL_IPI,0)) AS VL_IPI,\n" +
                    "       sum(nvl(VL_OUTROS_IPI,0)) AS VL_OUTROS_IPI,\n" +
                    "       c.nfe_localizador\n" +
                    "from SYNCHRO.cor_dof c, SYNCHRO.cor_idf i, SYNCHRO.Cor_Mercadoria m, mapa_aux_locais ma\n" +
                    " --from SYNCHRO.cor_dof c, SYNCHRO.cor_idf i, SYNCHRO.Cor_Mercadoria m\n" +
                    " where to_number(TO_CHAR(c.dt_fato_gerador_imposto, 'YYYYMMDD')) >= '20221101'\n" +
                    "   and to_number(TO_CHAR(c.dt_fato_gerador_imposto, 'YYYYMMDD')) <= '20221130'\n" +
                    "   and c.dof_sequence = i. dof_sequence\n" +
                    "   and c.codigo_do_site = i.codigo_do_site\n" +
                    "   and i.merc_codigo = m.merc_codigo\n" +
                    "   and c.codigo_do_site = 1\n" +
                    "   and c.ctrl_situacao_dof = 'N'\n" +
                    "  -- and c.ind_entrada_saida = 'E'\n" +
                    "  -- and i.merc_codigo\n" +
                    "  and c.informante_est_codigo like 'D4%'\n" +
                    "and ma.codigo_synchro = c.informante_est_codigo\n" +
                    "  --and exists (select 1 FROM mapa_aux_locais where codigo_synchro = c.informante_est_codigo)\n" +
                    "   and sis_codigo = 'GAMMA'    --fase3\n" +
                    " group by c.informante_est_codigo,\n" +
                    "          c.sis_codigo,\n" +
                    "          c.emitente_pfj_codigo,\n" +
                    "          c.uf_codigo_emitente,\n" +
                    "          c.destinatario_pfj_codigo,\n" +
                    "          c.uf_codigo_destino,\n" +
                    "          c.dt_fato_gerador_imposto,\n" +
                    "          i.cfop_codigo,\n" +
                    "          i.stc_codigo,\n" +
                    "          c.ind_entrada_saida,\n" +
                    "          c.numero,\n" +
                    "          c.dof_import_numero,\n" +
                    "          i.merc_codigo,\n" +
                    "          i.NBM_CODIGO,\n" +
                    "          m.descricao,\n" +
                    "          c.dh_emissao,\n" +
                    "          c.nfe_localizador, ma.nome) \n" +
                    "  SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(QTD),3) VALOR, 'COMPRA_QTD' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.102','1.403','1.917','2.102','2.917') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA COMPRA QTD\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_CONTABIL),3) VALOR, 'COMPRA_VALOR_TOTAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.102','1.403','1.917','2.102','2.917') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA COMPRA TOTAL\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS),3) VALOR, 'COMPRA_ICMS_NORMAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.102','1.403','1.917','2.102','2.917') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA COMPRA ICMS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS_ST),3) VALOR, 'COMPRA_ICMS_ST' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.102','1.403','1.917','2.102','2.917') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA COMPRA ICMS ST\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_PIS),3) VALOR, 'COMPRA_PIS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.102','1.403','1.917','2.102','2.917') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA COMPRA PIS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_COFINS),3) VALOR, 'COMPRA_COFINS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.102','1.403','1.917','2.102','2.917') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA COMPRA CONFINS\n" +
                    "  \n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(QTD),3) VALOR, 'TRF_ENTRADA_QTD' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.152','1.409','2.152','2.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA TRF QTD\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_CONTABIL),3) VALOR, 'TRF_ENTRADA_VALOR_TOTAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.152','1.409','2.152','2.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA TRF TOTAL\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS),3) VALOR, 'TRF_ENTRADA_ICMS_NORMAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.152','1.409','2.152','2.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA TRF ICMS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS_ST),3) VALOR, 'TRF_ENTRADA_ICMS_ST' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO IN ('1.152','1.409','2.152','2.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA TRF ICMS ST\n" +
                    "  \n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(QTD),3) VALOR, 'OUTROS_ENTRADAS_QTD' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO NOT IN ('1.409','1.152','2.409','2.152','1.917','1.403','2.917','2.102','1.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA OUTRAS QTD\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_CONTABIL),3) VALOR, 'OUTROS_ENTRADAS_VALOR_TOTAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO NOT IN ('1.409','1.152','2.409','2.152','1.917','1.403','2.917','2.102','1.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA OUTRAS TOTAL\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS),3) VALOR, 'OUTROS_ENTRADAS_ICMS_NORMAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO NOT IN ('1.409','1.152','2.409','2.152','1.917','1.403','2.917','2.102','1.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA OUTRAS ICMS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS_ST),3) VALOR, 'OUTROS_ENTRADAS_ICMS_ST' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO NOT IN ('1.409','1.152','2.409','2.152','1.917','1.403','2.917','2.102','1.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA OUTRAS ICMS ST\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_PIS),3) VALOR, 'OUTROS_ENTRADAS_PIS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO NOT IN ('1.409','1.152','2.409','2.152','1.917','1.403','2.917','2.102','1.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA OUTRAS PIS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_COFINS),3) VALOR, 'OUTROS_ENTRADAS_COFINS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'E' AND CFOP_CODIGO NOT IN ('1.409','1.152','2.409','2.152','1.917','1.403','2.917','2.102','1.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- ENTRADA OUTRAS CONFINS\n" +
                    "    \n" +
                    "  -- SAIDA\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(QTD),3) VALOR, 'VENDAS_QTD' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.102','5.405') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA VENDA QTD\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_CONTABIL),3) VALOR, 'VENDAS_VALOR_TOTAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.102','5.405') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA VENDA TOTAL\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(DESCONTO),3) VALOR, 'VENDAS_DESCONTO' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.102','5.405') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA VENDA DESCONTO\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS),3) VALOR, 'VENDAS_ICMS_NORMAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.102','5.405') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA VENDA ICMS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_PIS),3) VALOR, 'VENDAS_PIS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.102','5.405') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA VENDA PIS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_COFINS),3) VALOR, 'VENDAS_COFINS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.102','5.405') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA VENDA CONFINS\n" +
                    "  \n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(QTD),3) VALOR, 'PERDA_QTD' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.927') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA PERDA QTD\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_CONTABIL),3) VALOR, 'PERDA_VALOR_TOTAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.927') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA PERDA TOTAL\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS),3) VALOR, 'PERDA_ICMS_NORMAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.927') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA PERDA ICMS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS_ST),3) VALOR, 'PERDA_ICMS_ST' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.927') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA PERDA ICMS ST\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_PIS),3) VALOR, 'PERDA_PIS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.927') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA PERDA PIS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_COFINS),3) VALOR, 'PERDA_COFINS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.927') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA PERDA CONFINS\n" +
                    "  \n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(QTD),3) VALOR, 'TRF_SAIDA_QTD' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.152','5.409','6.152','6.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA TRF QTD\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_CONTABIL),3) VALOR, 'TRF_SAIDA_VALOR_TOTAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.152','5.409','6.152','6.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA TRF TOTAL\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS),3) VALOR, 'TRF_SAIDA_ICMS_NORMAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.152','5.409','6.152','6.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA TRF ICMS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS_ST),3) VALOR, 'TRF_SAIDA_ICMS_ST' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO IN ('5.152','5.409','6.152','6.409') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA TRF ICMS ST\n" +
                    "  \n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(QTD),3) VALOR, 'OUTRAS_SAIDAS_QTD' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO NOT IN ('5.409','5.152','6.409','6.152', '5.927', '5.405', '5.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA OUTRAS QTD\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_CONTABIL),3) VALOR, 'OUTRAS_SAIDAS_VALOR_TOTAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO NOT IN ('5.409','5.152','6.409','6.152', '5.927', '5.405', '5.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA OUTRAS TOTAL\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS),3) VALOR, 'OUTRAS_SAIDAS_ICMS_NORMAL' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO NOT IN ('5.409','5.152','6.409','6.152', '5.927', '5.405', '5.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA OUTRAS ICMS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_ICMS_ST),3) VALOR, 'OUTRAS_SAIDAS_ICMS_ST' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO NOT IN ('5.409','5.152','6.409','6.152', '5.927', '5.405', '5.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA OUTRAS ICMS ST\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_PIS),3) VALOR, 'OUTRAS_SAIDAS_PIS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO NOT IN ('5.409','5.152','6.409','6.152', '5.927', '5.405', '5.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA OUTRAS PIS\n" +
                    "  UNION ALL SELECT INFORMANTE_EST_CODIGO, NOME, ROUND(SUM(VALOR_COFINS),3) VALOR, 'OUTRAS_SAIDAS_COFINS' INFORMACAO FROM SYNCHRO_GENERICO WHERE IND_ENTRADA_SAIDA = 'S' AND CFOP_CODIGO NOT IN ('5.409','5.152','6.409','6.152', '5.927', '5.405', '5.102') GROUP BY INFORMANTE_EST_CODIGO, NOME -- SAIDA OUTRAS CONFINS");

            targetConnection = DriverManager.getConnection(TARGET_DB_URL, TARGET_DB_USERNAME, TARGET_DB_PASSWORD);
            targetConnection.setAutoCommit(false); // desabilitar o auto-commit para usar executeBatch ANO_MES, LOCALIDADE, SISTEMA, INFORMACAO, VALOR, CREATION, MODIFIED
            PreparedStatement preparedStatement = targetConnection.prepareStatement(
                    "BEGIN UPDATE MAPA_INTEGRIDADE_FISCAL" +
                    "          SET VALOR = ?, MODIFIED = SYSDATE" +
                    "        WHERE ANO_MES = '2022-11'" +
                    "          AND LOCALIDADE = ?" +
                    "          AND SISTEMA = 'SYNCHRO_DSV'" +
                    "          AND INFORMACAO = ?;" +
                    "       IF(SQL%ROWCOUNT = 0) THEN" +
                    "            INSERT INTO MAPA_INTEGRIDADE_FISCAL(ANO_MES, LOCALIDADE, SISTEMA, INFORMACAO, VALOR, CREATION, MODIFIED)" +
                    "            VALUES('2022-11', ?, 'SYNCHRO_DSV', ?, ?, SYSDATE, SYSDATE); END IF; END;");
            int count = 0;
            while (resultSet.next()) {
                preparedStatement.setFloat(1, resultSet.getFloat("aaa"));
                preparedStatement.setString(2, resultSet.getString("nome"));
                preparedStatement.setString(3, resultSet.getString("INFORMACAO"));
                preparedStatement.setString(4, resultSet.getString("nome"));
                preparedStatement.setString(5, resultSet.getString("INFORMACAO"));
                preparedStatement.setInt(6, resultSet.getInt("valor"));

                preparedStatement.addBatch();
                if (++count % 1000 == 0) {
                    preparedStatement.executeBatch(); // executar inserções a cada 1000 linhas
                }
            }

            // Inserir os últimos registros
            preparedStatement.executeBatch();
            targetConnection.commit(); //commitar as inserções
            System.out.println("Inserção em massa concluída com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // fechar as conexões e resultados
            try { if (resultSet != null) resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (statement != null) statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (sourceConnection != null) sourceConnection.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (targetConnection != null) targetConnection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
