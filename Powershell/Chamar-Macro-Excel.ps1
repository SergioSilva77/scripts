$excel = new-object -comobject excel.application
$excelFiles = Get-ChildItem -Path 'C:\Users\Quality\Desktop\Pasta1.xlsm' -Include *.xls, *.xlsm -Recurse #caminho do arquivo excel com macro
Foreach($file in $excelFiles)
{   $workbook = $excel.workbooks.open($file.fullname)   $worksheet = $workbook.worksheets.item(1)   $excel.Run("Macro1") #nome da macro   $workbook.save()   $workbook.close()
}
$excel.quit()