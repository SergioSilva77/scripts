 IXLRows linhas = worksheet.RowsUsed();
 foreach (IXLRow dataRow in linhas.Skip(1)) // Pula a linha do cabeçalho
 {
     IXLCell celula = dataRow.Cell(colunaChave);
 }