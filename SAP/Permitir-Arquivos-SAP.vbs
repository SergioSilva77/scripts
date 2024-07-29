```python
Dim WshShell, WinTitle

Set WshShell = WScript.CreateObject("WScript.Shell")

WScript.Sleep 1000

If WshShell.AppActivate("SAP GUI Security") Or WshShell.AppActivate("Segurança SAPGUI") Then
 
	WshShell.SendKeys "%M"
	WshShell.SendKeys "%P"
End If
```