public List<Dictionary<string, object>> ObterDados(string query)
{
    string connectionString = $"Data Source={CaminhoDatabase};Version=3;";
    var result = new List<Dictionary<string, object>>();

    using (var connection = new SQLiteConnection(connectionString))
    {
        connection.Open();
        using (var command = new SQLiteCommand(query, connection))
        {
            using (var reader = command.ExecuteReader())
            {
                while (reader.Read())
                {
                    var row = new Dictionary<string, object>();

                    for (int i = 0; i < reader.FieldCount; i++)
                    {
                        row[reader.GetName(i)] = reader.GetValue(i);
                    }

                    result.Add(row);
                }
            }
        }
    }

    return result;
}