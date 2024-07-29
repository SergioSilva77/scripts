'------------------------------------------------------Loop para buscar o VÁLIDO ATÉ na lista e seleciona-lo--------------------------------------------------------
Dim IndValido
IndValido = 0
scroll = 0

Do While IndValido=<20
valido_ate = session.findById("wnd[1]/usr/tblSAPLSVIXTCTRL_SEL_FLDS/txtFIELD_TB-SCRTEXT_L[0,"&IndValido&"]").Text
	scroll = scroll+1

'se ocorrer erro/tela imprevista ---------- código adicionado, devido ao SAP voltar ao indice 0 após mudar o scroll-------------------------------

on error resume next

'----------------------------------------- se ocorrer erro, voltar ao ID 1
IndValido = 1
valido_ate = session.findById("wnd[1]/usr/tblSAPLSVIXTCTRL_SEL_FLDS/txtFIELD_TB-SCRTEXT_L[0,"&IndValido&"]").Text

'-----------------------------------se não ocorrer erro, seguir----------------------------------------------------
on error goto 0

session.findById("wnd[1]/usr/tblSAPLSVIXTCTRL_SEL_FLDS").verticalScrollbar.position = scroll

    If InStr(valido_ate,"Válido até") > 0 Then
       session.findById("wnd[1]/usr/tblSAPLSVIXTCTRL_SEL_FLDS/txtFIELD_TB-SCRTEXT_L[0,"&IndValido&"]").setFocus
		session.findById("wnd[1]/usr/tblSAPLSVIXTCTRL_SEL_FLDS").getAbsoluteRow(scroll).selected = -1
       IndValido=20
		
    End If

   IndValido=IndValido+1

Loop
