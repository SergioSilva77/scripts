public void ConfigurarSerilog()
{
    // Caminho do log
    diretorioLogs = Path.Combine(Environment.CurrentDirectory, "logs");
    string caminhoLog = Path.Combine(diretorioLogs, "log.txt");

    if (!Directory.Exists(diretorioLogs))
    {
        Directory.CreateDirectory(diretorioLogs);
    }

    // Configuração do Serilog com template de saída personalizado
    Log.Logger = new LoggerConfiguration()
        .MinimumLevel.Debug() // Define o nível mínimo de log
        .WriteTo.Console(
            outputTemplate: "{Timestamp:yyyy-MM-dd HH:mm:ss} [{Level:u3}] - {Message:lj}{NewLine}{Exception}"
        )
        .WriteTo.File(
            caminhoLog,
            rollingInterval: RollingInterval.Day,
            outputTemplate: "{Timestamp:yyyy-MM-dd HH:mm:ss} [{Level:u3}] - {Message:lj}{NewLine}{Exception}"
        )
        .CreateLogger();
    Log.Information("");
    Log.Information("-----------------------------------------------------------------------");
    Log.Information("");
}