 public void InserirDeOutroBanco(string bancoQueRecebe, string bancoComDados)
 {
     string connBancoRecebedor = $"Data Source={bancoQueRecebe};Version=3;";
     using (SQLiteConnection connection = new SQLiteConnection(connBancoRecebedor))
     {
         connection.Open();

         // Anexar o segundo banco de dados
         string attachCommandText = $"ATTACH DATABASE '{bancoComDados}' AS db2;";
         using (SQLiteCommand attachCommand = new SQLiteCommand(attachCommandText, connection))
         {
             attachCommand.ExecuteNonQuery();
         }

         // Realizar a consulta entre as tabelas dos dois bancos de dados
         string query = @"
         INSERT INTO DadosXML
         SELECT Nome,
                NomeXML,
                Data,
                NumeroNF,
                Produto,
                Quantidade,
                UnidadeMedida,
                CFOP,
                InscricaoEstadual,
                ChaveNF,
                NomeProdutor,
                NomeFornecedor,
                ValorNF,
                Status,
                Observacao,
                NCM,
                InscricaoEstadualDestinatario
           FROM db2.DadosXML a
         WHERE a.Nome||a.ChaveNF NOT IN (SELECT Nome||ChaveNF FROM DadosXML);";

         using (SQLiteCommand command = new SQLiteCommand(query, connection))
         {
             int qtd = command.ExecuteNonQuery();
             if (qtd > 0)
             {
                 Msg.WriteLine($"Copiado {qtd} registros do robô: {Path.GetFileName(Path.GetDirectoryName(bancoComDados))}");
             }
         }

         // Desanexar o banco de dados quando terminar
         string detachCommandText = "DETACH DATABASE db2;";
         using (SQLiteCommand detachCommand = new SQLiteCommand(detachCommandText, connection))
         {
             detachCommand.ExecuteNonQuery();
         }
     }
 }