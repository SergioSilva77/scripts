# ClosedXML

## Pacote Nuget

- ClosedXML

## Ler planilha

```csharp
using (XLWorkbook workbook = new XLWorkbook("caminhoPlanilha.xls"))
{
}
```

## Obter WorkSheet

Se quiser pegar a primeira planilha, use 1

```csharp
IXLWorksheet worksheet = workbook.Worksheet(1);
IXLWorksheet worksheet = workbook.Worksheet("Nome");
```

## Obter Cell

```csharp
IXLCell cell1 = worksheet.Cell(1, 2);
IXLCell cell2 = worksheet.Cell("A1");
```

## Obter Cells

```csharp
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
```

## Obter linhas usadas

```csharp
 IXLRows linhas = worksheet.RowsUsed();
 foreach (IXLRow dataRow in linhas.Skip(1)) // Pula a linha do cabeçalho
 {
     IXLCell celula = dataRow.Cell(colunaChave);
 }
```