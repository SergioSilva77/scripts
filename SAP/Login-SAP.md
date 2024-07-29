# [VBScript] Login SAP

Login pelo ambiente

```python
set WshShell = CreateObject("WScript.Shell")

'WshShell.Exec("C:\Program Files (x86)\SAP\FrontEnd\SAPgui\saplogon.exe")

WshShell.Exec("?{SAP_PATH}")
WScript.Sleep 5000
Set SapGuiAuto = GetObject("SAPGUI")
Set application = SapGuiAuto.GetScriptingEngine
WScript.Sleep 2000
Set Connection = application.Openconnection("?{CredentialsSAP.Parameter1}", True)
WScript.Sleep 2000

If Not IsObject(application) Then
   Set SapGuiAuto  = GetObject("SAPGUI")
   Set application = SapGuiAuto.GetScriptingEngine
End If
If Not IsObject(connection) Then
   Set connection = application.Children(0)
End If
If Not IsObject(session) Then
   Set session    = connection.Children(0)
End If
If IsObject(WScript) Then
   WScript.ConnectObject session,     "on"
   WScript.ConnectObject application, "on"
End If

session.findById("wnd[0]").maximize
session.findById("wnd[0]/usr/txtRSYST-BNAME").text = "?{CredentialsSAP.Username}"
session.findById("wnd[0]/usr/pwdRSYST-BCODE").text = "?{CredentialsSAP.Password}"
session.findById("wnd[0]/usr/txtRSYST-LANGU").text = "PT"
session.findById("wnd[0]/usr/pwdRSYST-BCODE").setFocus
session.findById("wnd[0]/usr/pwdRSYST-BCODE").caretPosition = 2
session.findById("wnd[0]").sendVKey 0
```

Login SAP em vários ambientes

```python
' Dicionário para armazenar as informações dos ambientes

Set ambientes = CreateObject("Scripting.Dictionary")
 
'Informações dos ambientes
 
'----------------------------------------------------- SAP BW 7.0 --------------------------------------------------------------------
'nome do ambiente que virá de variavel-----------usuario------------- senha ---------------- nome ambiente no SAP
ambientes("?{SAP_BQ0.Parameter2}") = Array("?{SAP_BQ0.Username}", "?{SAP_BQ0.Password}", "?{SAP_BQ0.Parameter1}")

ambientes("?{SAP_BP0.Parameter2") = Array("?{SAP_BP0.Username}", "?{SAP_BP0.Password}", "?{SAP_BP0.Parameter1}")

ambientes("?{SAP_BD0.Parameter2}") = Array("?{SAP_BD0.Username}", "?{SAP_BD0.Password}", "?{SAP_BD0.Parameter1}")
 
'Adicionar mais aimbientes se necessário-----

'Ambiente 

ambiente = "NOME_VARIAVEL"

msgbox ambiente
 
' Verificar se o ambiente existe no dicionário

If ambientes.Exists(ambiente) Then

    ' Obtendo os valores do ambiente

    valores = ambientes(ambiente)

    usuario = valores(0)

    senha = valores(1)

	SID = valores(2)
 
    msgbox "Ambiente: " & SID

    'msgbox "Usuário: " & usuario
 
	
 
Set WshShell = CreateObject("WScript.Shell")
 
WshShell.Exec("C:\Program Files (x86)\SAP\FrontEnd\SAPgui\saplogon.exe")
 
WScript.Sleep 5000

Set SapGuiAuto = GetObject("SAPGUI")

Set application = SapGuiAuto.GetScriptingEngine

WScript.Sleep 2000
 
Set Connection = application.Openconnection(SID, True)

WScript.Sleep 2000
 
If Not IsObject(application) Then

   Set SapGuiAuto  = GetObject("SAPGUI")

   Set application = SapGuiAuto.GetScriptingEngine

End If

If Not IsObject(connection) Then

   Set connection = application.Children(0)

End If

If Not IsObject(session) Then

   Set session    = connection.Children(0)

End If

If IsObject(WScript) Then

   WScript.ConnectObject session,     "on"

   WScript.ConnectObject application, "on"

End If
 
session.findById("wnd[0]").maximize

session.findById("wnd[0]/usr/txtRSYST-BNAME").text = usuario

session.findById("wnd[0]/usr/pwdRSYST-BCODE").text = senha

session.findById("wnd[0]/usr/txtRSYST-LANGU").text = "PT"

session.findById("wnd[0]/usr/pwdRSYST-BCODE").setFocus

session.findById("wnd[0]/usr/pwdRSYST-BCODE").caretPosition = 2

session.findById("wnd[0]").sendVKey 0
 
Else

    msgbox "Ambiente não encontrado."

End If
```

Login pelo servidor

```python
Option Explicit

'-Variaveis-----------------------------------------------------------
Dim WSHShell, SAPGUIPath, SID, InstanceNo, WinTitle, SapGuiAuto, application, connection, session

'-Main----------------------------------------------------------------
Set WSHShell = WScript.CreateObject("WScript.Shell")

If IsObject(WSHShell) Then

'-Diretorio do SAPGUI---------------------------
SAPGUIPath = "C:\Program Files (x86)\SAP\FrontEnd\SAPgui\"

'-SAP system ID-------------------------------------------
SID = "hda0386"

'- instancia SAP-----------------------
InstanceNo = "00"

'-Iniciar SAP GUI----------------------------------------------
WSHShell.Exec SAPGUIPath & "sapgui.exe " & SID & " " & InstanceNo

'-Titulo da janela SAP ------------------------

 WinTitle = "SAP"
While Not WSHShell.AppActivate(WinTitle)
WScript.Sleep 250
Wend
Set WSHShell = Nothing
End If

Set SapGuiAuto = GetObject("SAPGUI")
Set application = SapGuiAuto.GetScriptingEngine
WScript.Sleep 2000

If Not IsObject(application) Then
   Set SapGuiAuto  = GetObject("SAPGUI")
   Set application = SapGuiAuto.GetScriptingEngine
End If
If Not IsObject(connection) Then
   Set connection = application.Children(0)
End If
If Not IsObject(session) Then
   Set session    = connection.Children(0)
End If
If IsObject(WScript) Then
   WScript.ConnectObject session,     "on"
   WScript.ConnectObject application, "on"
End If

Session.FindById("wnd[0]").maximize
Session.FindById("wnd[0]/usr/txtRSYST-BNAME").text = "?{CredentialsSAP.Username}"
session.findById("wnd[0]/usr/pwdRSYST-BCODE").text = "?{CredentialsSAP.Password}"
session.findById("wnd[0]/usr/txtRSYST-LANGU").text = "PT"
session.findById("wnd[0]/usr/pwdRSYST-BCODE").setFocus
session.findById("wnd[0]/usr/pwdRSYST-BCODE").caretPosition = 2
session.findById("wnd[0]").sendVKey 0
```