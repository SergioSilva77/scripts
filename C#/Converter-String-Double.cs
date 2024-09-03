public double ConverterStringParaDouble(string valor)
{
    decimal valorParsed = decimal.Parse(valor, new CultureInfo("pt-BR"));
    string formatado = valorParsed.ToString("N2", new CultureInfo("en-US"));
    return double.Parse(formatado, new CultureInfo("en-US"));
}