public async Task<FileInfo> EsperarArquivo(int attempts, int delay)
{
    DirectoryInfo di = new DirectoryInfo(diretorioDownlaod);
    while (attempts-- > 0)
    {
        foreach (FileInfo file in di.GetFiles())
        {
            if (Regex.IsMatch(file.Name, @"^\d+\.txt$"))
            {
                return file;
            }
        }
        Thread.Sleep(delay);
    }
    return null; // retorna false se o controle não se tornar habilitado.
}