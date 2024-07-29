# # Importar Bibliotecas
import win32com.client
import sys
import subprocess
import requests
import os
import json
from base64 import b64encode
import pandas as pd
import datetime
import psutil
import openpyxl
import time
import warnings

warnings.filterwarnings("ignore")

autoit = win32com.client.Dispatch("AutoItX3.Control")
wShell = win32com.client.Dispatch("WScript.Shell")

# Lista de processos em execução
process_name = "saplogon.exe"
user_id = os.getlogin()

# # Criar objeto SAP
class SapGui():

    def __init__(self):

        sleep = 5000

        self.path = r"C:\Program Files (x86)\SAP\FrontEnd\SAPgui\saplogon.exe"

        for proc in psutil.process_iter(['name', 'username']):
            # Verifica se o nome do processo é igual a "saplogon.exe"
            
            if "\\" in str(proc.info['username']):
                user = str(proc.info['username']).split("\\")[1]
            else:
                user = ""
                
            if proc.info['name'] == process_name and user == str(user_id):
                break
        else:        
            subprocess.Popen(self.path)

        if autoit.WinWait("SAP Logon", "", sleep) == 0:

            raise Exception('Janela SAP Logon não apareceu!')

        else:

            self.SapGuiAuto = win32com.client.GetObject("SAPGUI")

            if not type(self.SapGuiAuto) == win32com.client.CDispatch:
                raise Exception('Erro ao criar objeto!')

            application = self.SapGuiAuto.GetScriptingEngine
            self.connection = application.OpenConnection(ConnectionSAP, True)

            if autoit.WinWait("[CLASS:SAP_FRONTEND_SESSION]", "", sleep) == 0:
                raise Exception('Janela sessão SAP não apareceu!')
            else:
                autoit.WinActivate("[CLASS:SAP_FRONTEND_SESSION]", "")
                self.session = self.connection.Children(0)
                self.session.findbyId("wnd[0]").maximize()

    def Login(self):

        try:
        
            self.session.findById("wnd[0]/usr/txtRSYST-MANDT").Text = MANDT
            self.session.findById("wnd[0]/usr/txtRSYST-BNAME").Text = BNAME
            self.session.findById("wnd[0]/usr/pwdRSYST-BCODE").Text = BCODE
            self.session.findById("wnd[0]/usr/txtRSYST-LANGU").Text = LANGU
            self.session.findById("wnd[0]").sendVKey(0)

            try:
                self.session.findById("wnd[1]/usr/radMULTI_LOGON_OPT2").select()
                self.session.findById("wnd[1]/usr/radMULTI_LOGON_OPT2").setFocus()
                self.session.findById("wnd[1]/tbar[0]/btn[0]").press()
            except:
                pass

        except:
            raise Exception('Erro login SAP: ' + str(sys.exc_info()[0]))


# User/Password
MANDT = '100'
BNAME = 'usuario'
BCODE = 'senha'
LANGU = 'PT'
ConnectionSAP = 'nome_conexao_sap'

# # Iniciar
sap = SapGui()
sap.Login()
autoit.WinActivate("[CLASS:SAP_FRONTEND_SESSION]", "")

# SETAR TRANSAÇÃO
sap.session.findById("wnd[0]").maximize()
sap.session.findById("wnd[0]/tbar[0]/okcd").text = "ME23N"
sap.session.findById("wnd[0]").sendVKey(0)

# CRIAR NOVO
sap.session.findById("wnd[0]/tbar[1]/btn[6]").press()
try:
    sap.session.findById("wnd[1]/usr/btnSPOP-OPTION2").press()
except Exception:
    pass

index = st['index'][0]


# RC
sap.session.findById(f"wnd[0]/usr/subSUB0:SAPLMEGUI:0013/subSUB2:SAPLMEVIEWS:1100/subSUB2:SAPLMEVIEWS:1200/subSUB1:SAPLMEGUI:1211/tblSAPLMEGUITC_1211/ctxtMEPO1211-BANFN[27,{index}]").text = "222"