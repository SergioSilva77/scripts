public async Task Renomear(string originalPath, string newFileName)
{
    // Obter o diretório do arquivo original e combinar com o novo nome do arquivo
    string directory = Path.GetDirectoryName(originalPath);
    string newFilePath = Path.Combine(directory, newFileName);

    // Verificar se o novo arquivo já existe
    int count = 1;
    string fileWithoutExtension = Path.Combine(directory, Path.GetFileNameWithoutExtension(newFileName));
    string extension = Path.GetExtension(newFileName);

    while (File.Exists(newFilePath))
    {
        // Modificar o nome do arquivo com um sufixo numérico crescente
        newFilePath = $"{fileWithoutExtension}_{count++}{extension}";
    }

    // Renomear o arquivo original para o novo caminho
    File.Move(originalPath, newFilePath);
}