public void InserirChavesEmMassa(List<string> chaves)
{
    string connectionString = $"Data Source={CaminhoDatabase};Version=3;";
    using (var connection = new SQLiteConnection(connectionString))
    {
        connection.Open();
        using (var transaction = connection.BeginTransaction())
        {
            try
            {
                var sb = new StringBuilder();
                sb.Append("INSERT OR IGNORE INTO Chaves (Chave, Status, Observacao) VALUES ");

                var parameters = new List<SQLiteParameter>();

                for (int i = 0; i < chaves.Count; i++)
                {
                    string chaveNF = chaves[i];
                    if (i > 0)
                        sb.Append(", ");

                    sb.Append($"({chaveNF}, 'Pendente', NULL)");
                    //parameters.Add(new SQLiteParameter($"@ChaveNF{i}", chaveNF));
                }

                using (var command = new SQLiteCommand(sb.ToString(), connection))
                {
                    command.ExecuteNonQuery();
                }

                transaction.Commit();
            }
            catch (Exception)
            {
                transaction.Rollback();
                throw;
            }
        }
    }
}