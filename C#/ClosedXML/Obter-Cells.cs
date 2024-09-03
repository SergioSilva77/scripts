// Usar o método Cells dentro de um intervalo
var range = worksheet.Range("A1:C3");
foreach (var cell in range.Cells())
{
    Console.WriteLine("Valor da célula " + cell.Address + ": " + cell.Value);
}

// Usar a sobrecarga Cells(string cells) para retornar células específicas
var specificCells = range.Cells("A1, B2, C3");
foreach (var cell in specificCells)
{
    Console.WriteLine("Valor da célula específica " + cell.Address + ": " + cell.Value);
}

// Usar a sobrecarga Cells(params string[] cells) para retornar células específicas
var specificCellsArray = range.Cells("A1", "B2", "C3");
foreach (var cell in specificCellsArray)
{
    Console.WriteLine("Valor da célula específica " + cell.Address + ": " + cell.Value);
}