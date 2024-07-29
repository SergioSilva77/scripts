# Exibir linhas e colunas

```python
Dim objXl
Dim objWorkbook
Dim objSheet

Set objXl = CreateObject("Excel.Application")
Set objWorkbook = objXl.Workbooks.open("excel.xlsx") 'caminho do excel
Set objSheet = objWorkbook.Worksheets(1) 'sheet'

objXl.Visible = True

With objSheet

Col=objSheet.UsedRange.columns.count
Row=objSheet.UsedRange.Rows.Count

 For  i= 1 to Row
      For j=1 to Col
          Msgbox  objSheet.cells(i,j).Address
      Next
  Next
End With
```

Pesquisar palavras em um intervalo

```python
Dim objXl
Dim objWorkbook
Dim objSheet

Set objXl = CreateObject("Excel.Application")
Set objWorkbook = objXl.Workbooks.open("excel.xlsx") 'caminho do excel
Set objSheet = objWorkbook.Worksheets(1) 'sheet'

objXl.Visible = True

With objSheet
objSheet.Range("A1:C1").Find("Palavra").Column 'n° coluna
objSheet.Range("A1:C1").Find("Palavra").Address ' coluna\linha
objSheet.Range("A1:C1").Find("Palavra").Value 'valor da celula
End With
```