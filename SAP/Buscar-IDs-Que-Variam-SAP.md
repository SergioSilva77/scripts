# [VBScript] Buscar IDs que variam no SAP

Em casos que o ID fica variando no SAP, é possível utilizar o VBScript com regex. Existe a possibilidade do ID mudar após rodar em outro ambiente do servidor do cliente.

Para contornar isso, segue um código feito em VBScript como exemplo:

```vbnet
'-Begin-----------------------------------------------------------------

'-Function FindByIdPart-------------------------------------------------
'-
'- Function to find an UI element by its Id via Regular Expressions,
'- independently from program names and screen numbers
'- oApp = SAP application
'- oArea = Container to be searched
'- regexId = Regular Expression of Id of UI element which is searched
'-
'-----------------------------------------------------------------------
Function FindByIdPart(oApp, oArea, regexId)

  Set oRegEx = New RegExp
  oRegEx.Pattern = regexId
  oRegEx.IgnoreCase = True
  oRegEx.Global = False

  On Error Resume Next
  If oArea.Children.Count() > 0 Then
    For i = 0 To oArea.Children.Count() - 1
      Set Child = oArea.Children.Item(CLng(i))
      If oRegEx.Test(Child.Id) Then
        FindByIdPart = Child.Id
        On Error GoTo 0
        Exit Function
      End If
      If Child.ContainerType() And Child.Children().Count() > 0 Then
        FindByIdPart = _
          FindByIdPart(oApp, oApp.findByID(Child.Id), regexId)
        If FindByIdPart <> "" Then
          On Error GoTo 0
          Exit Function
        End If
      End If
    Next
  End If
  On Error Goto 0
  FindByIDPart = ""

End Function

'-Sub Main--------------------------------------------------------------
Sub Main()

  Set SapGuiAuto = GetObject("SAPGUI")
  If Not IsObject(SapGuiAuto) Then
    Exit Sub
  End If

  Set app = SapGuiAuto.GetScriptingEngine
  If Not IsObject(app) Then
    Exit Sub
  End If

  app.HistoryEnabled = False

  Set connection = app.Children(0)
  If Not IsObject(connection) Then
    Exit Sub
  End If

  If connection.DisabledByServer = True Then
    Exit Sub
  End If

  Set session = connection.Children(0)
  If Not IsObject(session) Then
    Exit Sub
  End If

  If session.Info.IsLowSpeedConnection = True Then
    Exit Sub
  End If

  'Procurar por
  'wnd[0]/usr/subSSA1:SAPLBRF_MAINTENANCE:3006/txtSBRF170-VERSION
  Id = FindByIDPart(app, session.findById("wnd[0]/usr"), _
    "wnd\[0\]\/usr\/subSSA1:.*txtSBRF170-VERSION")

  text = session.findById(Id).text

  app.HistoryEnabled = True

End Sub

'-Main------------------------------------------------------------------
Main

'-End-------------------------------------------------------------------
```

No código acima, vimos um exemplo com o ID `wnd[0]/usr/subSSA1:SAPLBRF_MAINTENANCE:3006/txtSBRF170-VERSION` . Note que o número `3006`pode variar dependendo do ambiente, no lugar do `3006`, foi utilizado `.*`

```vbnet
Id = FindByIDPart(app, session.findById("wnd[0]/usr"), "wnd\[0\]\/usr\/subSSA1:.*txtSBRF170-VER
```