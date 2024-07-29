# Mover arquivo para o diretório

```python
public static void MoverArquivoParaDiretorio(string arquivo, string diretorio, bool substituir)
{
    if (string.IsNullOrEmpty(arquivo))
    {
        throw new ArgumentException("O caminho do arquivo não pode ser nulo ou vazio.", nameof(arquivo));
    }

    if (string.IsNullOrEmpty(diretorio))
    {
        throw new ArgumentException("O caminho do diretório não pode ser nulo ou vazio.", nameof(diretorio));
    }

    if (!File.Exists(arquivo))
    {
        throw new FileNotFoundException("O arquivo especificado não foi encontrado.", arquivo);
    }

    if (!Directory.Exists(diretorio))
    {
        Directory.CreateDirectory(diretorio);
    }

    var fileName = Path.GetFileName(arquivo);
    var destinationPath = Path.Combine(diretorio, fileName);

    if (File.Exists(destinationPath))
    {
        if (substituir)
        {
            File.Delete(destinationPath);
        }
        else
        {
            throw new IOException("O arquivo de destino já existe.");
        }
    }

    File.Move(arquivo, destinationPath);
}
```