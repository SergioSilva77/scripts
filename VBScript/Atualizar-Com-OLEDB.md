# Atualizar usando OLEDB

Banco Access

```python
connectionString = "Provider=Microsoft.ACE.OLEDB.16.0; Data Source=C:\Temp\Dados.accdb;"
sql = "delete from Dados"
set cn = createobject("ADODB.Connection")
set cmd = createobject("ADODB.Command")
cn.open connectionString
cmd.ActiveConnection = cn
cmd.CommandText = sql
cmd.execute
cn.Close
```

Atualizar em massa

```python
Set objFileToRead = CreateObject("Scripting.FileSystemObject").OpenTextFile("C:\Users\sergi\Documents\Dev\projects-java\projetos-gerais\src\main\resources\anderson\_MIC.MCN.GIBSA.BBX0.SB0990.D220927.S8891.txt",1)
Dim strLine, sql

Set objConn = CreateObject("ADODB.Connection")
Set shell = CreateObject( "WScript.Shell" )
objConn.open "Provider=Microsoft.ACE.OLEDB.16.0; Data Source=C:\Temp\Dados.accdb;"
Set oRS = CreateObject("ADODB.Recordset")
Const adOpenKeyset = 1
Const adLockOptimistic = 3
oRS.Open "Dados", objConn, adOpenKeyset, adLockOptimistic

do while not objFileToRead.AtEndOfStream
	strLine = objFileToRead.ReadLine()
	unidade=Mid(strLine,1,9)
	dataMovimentacao=Mid(strLine,10,10)
	dataEfetiva=Mid(strLine,22,10)
	origem=Mid(strLine,33,6)
	evento=Mid(strLine,40,7)
	docto=Mid(strLine,49,5)
	ol=Mid(strLine,56,7)
	debito=Mid(strLine,90,24)
	credito=Mid(strLine,114, 25)
	
	oRS.AddNew
	oRS("unidade") = unidade
	oRS("data_movimentacao") = dataMovimentacao
	oRS("data_efetiva") = dataEfetiva
	oRS("origem") = origem
	oRS("evento") = evento
	oRS("docto") = docto
	oRS("ol") = ol
	oRS("debito") = debito
	oRS("credito") = credito	
	oRS.Update
Loop

objFileToRead.Close
Set objFileToRead = Nothing

oRS.Close
objConn.Close
```