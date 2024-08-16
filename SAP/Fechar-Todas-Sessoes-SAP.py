# coding: utf-8
# # Instalar Bibliotecas
# !pip install pywin32
# !pip install python-dotenv
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
from datetime import datetime, timedelta

warnings.filterwarnings("ignore")
autoit = win32com.client.Dispatch("AutoItX3.Control")
wShell = win32com.client.Dispatch("WScript.Shell")
# Lista de processos em execução
process_name = "saplogon.exe"
user_id = os.getlogin()
# # Criar objeto SAP
class SapGui():
    def __init__(self):
        self.SapGuiAuto = win32com.client.GetObject("SAPGUI")
        if not type(self.SapGuiAuto) == win32com.client.CDispatch:
            raise Exception('Erro ao criar objeto!')
        application = self.SapGuiAuto.GetScriptingEngine
        # Verifique se há alguma conexão aberta
        if application.Connections.Count > 0:
            self.connection = application.Connections.Item(0)  # obtenha a primeira conexão
        else:
            raise Exception('Nenhuma conexão SAP aberta encontrada!')
        # Agora obtenha a sessão
        if self.connection.Sessions.Count > 0:
            self.session = self.connection.Sessions.Item(0)  # obtenha a primeira sessão
        else:
            raise Exception('Nenhuma sessão SAP aberta encontrada!')
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



try:
    # # Iniciar
    sap = SapGui()
    while sap.connection.children.count > 0:
        session = sap.connection.Children(0)
        session.findbyid("wnd[0]").close()
        session.findById("wnd[1]/usr/btnSPOP-OPTION1").press()
except:
    pass