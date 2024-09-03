
connectionString = "Provider=Microsoft.ACE.OLEDB.16.0; Data Source=C:\Temp\Dados.accdb;"
sql = "delete from Dados"
set cn = createobject("ADODB.Connection")
set cmd = createobject("ADODB.Command")
cn.open connectionString
cmd.ActiveConnection = cn
cmd.CommandText = sql
cmd.execute
cn.Close