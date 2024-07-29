'Se houver uma tela sobrepondo a principal, neste caso um popup, irá clicar em OK.
Do While session.ActiveWindow.Name = "wnd[1]"
	'Clicar no OK
	session.findById("wnd[1]/tbar[0]/btn[0]").press
Loop