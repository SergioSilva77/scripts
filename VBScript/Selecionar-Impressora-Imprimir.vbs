'Função para buscar as impressoras nos módulos padrões
Function FindPrinter(ByVal PrinterName As String) As String
  Dim arrH, Pr, Printers, Printer As String
  Dim RegObj As Object, RegValue As String
  Const HKEY_CURRENT_USER = &H80000001
    Set RegObj = GetObject("winmgmts:{impersonationLevel=impersonate}!\\.\root\default:StdRegProv")
    RegObj.Enumvalues HKEY_CURRENT_USER, "Software\Microsoft\Windows NT\CurrentVersion\Devices", Printers, arrH
      For Each Pr In Printers
      'Ativar a impressora para salvar como PDF
        Application.ActivePrinter = FindPrinter("Microsoft Print to PDF")
        RegObj.getstringvalue HKEY_CURRENT_USER, "Software\Microsoft\Windows NT\CurrentVersion\Devices", Pr, RegValue
        Printer = Pr & " on " & Split(RegValue, ",")(1)
        If InStr(1, Printer, PrinterName, vbTextCompare) > 0 Then
           FindPrinter = Printer
           Exit Function
        End If
      Next
End Function