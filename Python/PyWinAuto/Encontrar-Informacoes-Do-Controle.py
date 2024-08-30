from pywinauto import Application

# Conectar ao processo SAP Logon pelo nome do processo
app = Application(backend="uia").connect(path="saplogon.exe")

# Acessar a janela específica que você deseja manipular
dlg = app.window(title_re="Sistema de informação de ordem - Cabeçalhos de ordem")

# Imprimir todos os identificadores de controle
dlg.print_control_identifiers()
