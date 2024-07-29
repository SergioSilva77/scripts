'trecho para verificar se a tela do SAP esta ativa
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

'se ocorrer erro/tela imprevista
on error resume next

'enviando enter
Session.findById("wnd[0]").sendVKey 0

'se não ocorrer
on error goto 0