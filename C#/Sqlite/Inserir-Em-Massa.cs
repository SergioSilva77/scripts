public void InserirEmMassa()
{
    Console.ForegroundColor = ConsoleColor.Yellow;
    string connectionString = $"Data Source={CaminhoDatabase};Version=3;";

    using (SQLiteConnection connection = new SQLiteConnection(connectionString))
    {
        try
        {
            connection.Open();

            string insertQuery = @"
            INSERT INTO DadosXML (
                CaminhoXML, NomeXML, Data, NumeroNF, Produto, Quantidade, 
                UnidadeMedida, CFOP, InscricaoEstadual, ChaveNF, NomeProdutor, 
                NomeFornecedor, ValorNF
            ) VALUES (
                @CaminhoXML, @NomeXML, @Data, @NumeroNF, @Produto, @Quantidade, 
                @UnidadeMedida, @CFOP, @InscricaoEstadual, @ChaveNF, @NomeProdutor, 
                @NomeFornecedor, @ValorNF
            )";


            foreach (ArgsSqlite dadosXml in lsConteudoXmls)
            {
                using (SQLiteCommand command = new SQLiteCommand(insertQuery, connection))
                {
                    // Adicionando parâmetros
                    command.Parameters.AddWithValue("@CaminhoXML", dadosXml.CaminhoXML);
                    command.Parameters.AddWithValue("@NomeXML", dadosXml.NomeXML);
                    command.Parameters.AddWithValue("@Data", dadosXml.Data);
                    command.Parameters.AddWithValue("@NumeroNF", dadosXml.NumeroNF);
                    command.Parameters.AddWithValue("@Produto", dadosXml.Produto);
                    command.Parameters.AddWithValue("@Quantidade", dadosXml.Quantidade);
                    command.Parameters.AddWithValue("@UnidadeMedida", dadosXml.UnidadeMedida);
                    command.Parameters.AddWithValue("@CFOP", dadosXml.CFOP);
                    command.Parameters.AddWithValue("@InscricaoEstadual", dadosXml.InscricaoEstadual);
                    command.Parameters.AddWithValue("@ChaveNF", dadosXml.ChaveNF);
                    command.Parameters.AddWithValue("@NomeProdutor", dadosXml.NomeProdutor);
                    command.Parameters.AddWithValue("@NomeFornecedor", dadosXml.NomeFornecedor);
                    command.Parameters.AddWithValue("@ValorNF", dadosXml.ValorNF);

                    int rowsAffected = command.ExecuteNonQuery();
                }
            }
        }
        catch (SQLiteException ex)
        {
            Console.WriteLine("Erro: " + ex.Message);
        }
        finally
        {
            connection.Close();
        }
    }
}