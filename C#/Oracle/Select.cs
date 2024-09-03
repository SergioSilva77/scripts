public void ObterListaCPFs()
{
    if (File.Exists("sql.info"))
    {
        string[] lines = File.ReadAllLines("sql.info", encoding: Encoding.UTF8);
        string conexao = CriptoHelper.Decrypt(lines[0]);
        string database = CriptoHelper.Decrypt(lines[1]);
        string porta = CriptoHelper.Decrypt(lines[2]);
        string usuario = CriptoHelper.Decrypt(lines[3]);
        string senha = CriptoHelper.Decrypt(lines[4]);

        //connectionString = $"Provider=OraOLEDB.Oracle;Data Source={database};User Id={usuario};Password={senha};";
        connectionString = $@"
                            Provider=OraOLEDB.Oracle;
                            User Id={usuario};
                            Password={senha};
                            Data Source=(DESCRIPTION=
                                (ADDRESS_LIST=
                                    (ADDRESS=(PROTOCOL=TCP)(HOST={conexao})(PORT={porta}))
                                )
                                (CONNECT_DATA=
                                    (SERVICE_NAME={database})
                                )
                            );";
    }
    else
    {
        lblStatus.Text = "Não foi possivel achar o arquivo sql.info";
        return;
    }


    _ = Task.Run((Action)(() =>
    {
        try
        {
            using (OleDbConnection conn = new OleDbConnection(connectionString))
            {
                // Abrindo a conexão
                conn.Open();

                using (OleDbCommand command = new OleDbCommand(configProgram.Query, conn))
                {
                    using (OleDbDataReader reader = command.ExecuteReader())
                    {
                        this.Invoke((MethodInvoker)delegate
                        {
                            progressBar.Value = 10;  // Exemplo: Início do processo
                            lblStatus.Text = "Carregando dados...";
                        });
                        while (reader.Read())
                        {
                            // Assume que os valores são strings; use o método adequado de conversão se necessário
                            string cpf = reader["CPF_FORMATADO"].ToString();
                            string ano = reader["ANO_ENEM"].ToString();
                            string pessoa = reader["PESSOA"].ToString();
                            string fase = reader["FASE"].ToString();
                            string concurso = reader["CONCURSO"].ToString();
                            this.listaCpfs.Add(new ArgsCpf()
                            {
                                CPF = Regex.Replace(cpf, @"[\.,-]", "").Trim(),
                                Ano = ano,
                                Pessoa = pessoa,
                                Fase = fase,
                                Concurso = concurso
                            });
                        }
                        this.Invoke((MethodInvoker)delegate
                        {
                            if (this.listaCpfs.Count == 1)
                            {
                                lblStatus.Text = $"{this.listaCpfs.Count} CPF encontrado";
                                Log.Information($"{this.listaCpfs.Count} CPF encontrado");
                            }
                            else
                            {
                                lblStatus.Text = $"{this.listaCpfs.Count} CPFs encontrados";
                                Log.Information($"{this.listaCpfs.Count} CPFs encontrados");
                            }
                            progressBar.Value = 100;
                        });
                    }
                }

                // Fechando a conexão
                conn.Close();
                listaCpfs = listaCpfs.Where(x => !string.IsNullOrEmpty(x.Ano)).ToList();
                lotes = GerarLotes();
                
            }
        }
        catch (OleDbException ex)
        {
            this.Invoke((MethodInvoker)delegate
            {
                lblStatus.Text = ex.Message;
                progressBar.Value = 0;  // Reset no progresso em caso de erro
            });
        }
        catch (Exception ex)
        {
            Log.Error(ex, "Aconteceu um erro ao iniciar");
            this.Invoke((MethodInvoker)delegate
            {
                lblStatus.Text = ex.Message;
                progressBar.Value = 0;  // Reset no progresso em caso de erro
            });

        }
    }));
}