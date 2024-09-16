public static List<string> LerLinhasDoArquivo(string caminhoArquivo)
{
    var linhas = new List<string>();

    // Abre o arquivo com FileShare.Read para permitir leitura simultânea
    using (var fileStream = new FileStream(caminhoArquivo, FileMode.Open, FileAccess.Read, FileShare.Read))
    using (var streamReader = new StreamReader(fileStream))
    {
        string linha;
        while ((linha = streamReader.ReadLine()) != null)
        {
            linhas.Add(linha);
        }
    }

    return linhas;
}