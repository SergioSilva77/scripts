public void ExecutarQuery(string query)
{
    string connectionString = $"Data Source={CaminhoDatabase};Version=3;";

    using (SQLiteConnection connection = new SQLiteConnection(connectionString))
    {
        try
        {
            connection.Open();

            using (SQLiteCommand command = new SQLiteCommand(query, connection))
            {
                int rowsAffected = command.ExecuteNonQuery();
            }
        }
        catch (SQLiteException ex)
        {
            Msg.WriteLine("Erro: " + ex.Message, ConsoleColor.Red);
        }
        finally
        {
            connection.Close();
        }
    }
}