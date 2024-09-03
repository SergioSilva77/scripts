Task.Run((Action)(() =>
{
    try
    {
        using (OleDbConnection conn = new OleDbConnection(connectionString))
        {
            // Abrindo a conexão
            conn.Open();
            using (OleDbTransaction transaction = conn.BeginTransaction())
            {
                try
                {
                    for (int i = 0; i < querys.Count; i++)
                    {
                        // Evita executar querys vazias
                        if (string.IsNullOrEmpty(querys[i].Trim()) || querys[i]?.Trim()?.Length <= 10)
                        {
                            continue;
                        }

                        // Preparar comandos
                        using (OleDbCommand command = new OleDbCommand(querys[i], conn, transaction))
                        {
                            try
                            {
                                command.CommandTimeout = 30;

                                int linhasAfetadas = command.ExecuteNonQuery();

                                // Guardar querys executadas com sucesso
                                querysCommit.Add(querys[i]);
                            }
                            catch (Exception e)
                            {
                                erros++;

                                // Guardar querys com erros
                                querysErro.Add(querys[i]);
                            }
                        }
                    }

                    // Commit
                    transaction.Commit();
                }
                catch (Exception ex)
                {
                    transaction.Rollback();
                }
            }

            // Fechando a conexão
            conn.Close();
        }
    }
    catch (Exception ex)
    {
        Log.Error(ex, "Erro ao executar comando sql");
    }
})).Wait();
