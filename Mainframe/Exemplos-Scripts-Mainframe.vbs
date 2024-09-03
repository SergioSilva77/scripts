Set objStdOut = wscript.StdOut
Set System = CreateObject("EXTRA.System") 
Set activeSession=System.ActiveSession()
Set MyScreen = activeSession.Screen

' (LINHA_INICIAL, COLUNA_INICIAL, LINHA_FINAL, COLUNA_FINAL)
Dim textSaldo
textSaldo = MyScreen.Area(10, 57, 10, 74)


' ENVIAR ATALHO
MyScreen.SendKeys "<PF6>"

' ESPERAR STRING
msgSucesso = MyScreen.WaitForString("DESBLOQUEIO DE VALOR EFETUADO COM SUCESSO!",23, 02)

' FOCAR EM UMA LINHA E COLUNA
MyScreen.MoveTo 16,09

'------------------------------------------------- EXEMPLO GERAL

Set objStdOut = wscript.StdOut
Set System = CreateObject("EXTRA.System") 
Set activeSession=System.ActiveSession()
Set MyScreen = activeSession.Screen

Dim textMsg


telaLimpaPesquisa = MyScreen.WaitForString("KM12 - DESBLOQUEIO",1, 02)

if telaLimpaPesquisa then
else
	Err.Raise vbObjectError + 1000, "", textMsg
end if

Function PreencherZerosEsquerda(numero, tamanho)
    Dim numeroString
    Dim zerosFaltantes

    numeroString = CStr(numero)
    zerosFaltantes = tamanho - Len(numeroString)

    If zerosFaltantes > 0 Then
        PreencherZerosEsquerda = String(zerosFaltantes, "0") & numeroString
    Else
        PreencherZerosEsquerda = numeroString
    End If
End Function


Dim ag_formatado
Dim oper_formatado
Dim conta_formatada

ag_formatado = PreencherZerosEsquerda(?{ag}, 4) 
oper_formatado = PreencherZerosEsquerda(?{oper}, 4) 
conta_formatada = PreencherZerosEsquerda(?{conta}, 12) 


if telaLimpaPesquisa then
	MyScreen.MoveTo 4,11
	MyScreen.SendKeys ag_formatado
	WScript.Sleep 250
	MyScreen.SendKeys oper_formatado
	WScript.Sleep 250
	MyScreen.SendKeys conta_formatada
	WScript.Sleep 250

	MyScreen.SendKeys "<Enter>"
End If

telaBloqueio = MyScreen.WaitForString("DIGITE VALOR A DESBLOQUEAR. E TECLE",23, 02)

if telaBloqueio then
else
	Err.Raise vbObjectError + 1000, "", textMsg
end if

MyScreen.MoveTo 16,09
MyScreen.SendKeys ?{saldo_disp}
WScript.Sleep 250
MyScreen.SendKeys "<ENTER>"
MyScreen.SendKeys "<PF2>"



msgSucesso = MyScreen.WaitForString("DESBLOQUEIO DE VALOR EFETUADO COM SUCESSO!",23, 02)

if msgSucesso then
else	
	Err.Raise vbObjectError + 1000, "", textMsg
end if

