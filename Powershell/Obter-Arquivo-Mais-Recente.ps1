$diretorio = 'C:\AutomationEdge\Storage\NovaAgri\Busca de CDR'
$regex = 'certificado*'

Get-ChildItem -Path $diretorio -Filter $regex | Sort-Object LastWriteTime -Descending | Where-Object {$_.Name -match '.pdf$'}