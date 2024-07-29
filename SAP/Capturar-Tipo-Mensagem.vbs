'Pegando tipo da mensagem.
typeMessage = Session.findById("wnd[0]/sbar").messageType

'mandar a informação para o AE
Set objStdOut = wscript.StdOut
objStdOut.WriteLine "<<>>typeMessage::"&typeMessage&"<<>>"