public static string GerarCaminhoArquivoUnico(string diretorio, string nomeArquivo, string sufixo)
{
    // Combina o diretório e o nome do arquivo para obter o caminho completo
    string caminhoNovoArquivo = Path.Combine(diretorio, nomeArquivo);

    // Se o arquivo não existir, retorna o caminho direto
    if (!File.Exists(caminhoNovoArquivo))
    {
        return caminhoNovoArquivo;
    }

    // Obtém o nome do arquivo sem extensão e a extensão
    string nomeSemExtensao = Path.GetFileNameWithoutExtension(nomeArquivo);
    string extensao = Path.GetExtension(nomeArquivo);

    // Itera adicionando sufixos numéricos até encontrar um nome que não exista
    int contador = 1;
    string caminhoComSufixo;
    do
    {
        caminhoComSufixo = Path.Combine(diretorio, $"{nomeSemExtensao}{sufixo}{contador}{extensao}");
        contador++;
    } while (File.Exists(caminhoComSufixo));

    return caminhoComSufixo;
}