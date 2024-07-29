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



# # Iniciar
sap = SapGui()

index = st['index'][0]
contrato = st['contrato'][0]
item_contrato = st['item_contrato'][0]
rc = st['rc'][0]
item_rc = st['item_rc'][0]



# RC
sap.session.findById(f"wnd[0]/usr/subSUB0:SAPLMEGUI:0013/subSUB2:SAPLMEVIEWS:1100/subSUB2:SAPLMEVIEWS:1200/subSUB1:SAPLMEGUI:1211/tblSAPLMEGUITC_1211/ctxtMEPO1211-BANFN[27,{index}]").text = rc
# ITEM RC
sap.session.findById(f"wnd[0]/usr/subSUB0:SAPLMEGUI:0013/subSUB2:SAPLMEVIEWS:1100/subSUB2:SAPLMEVIEWS:1200/subSUB1:SAPLMEGUI:1211/tblSAPLMEGUITC_1211/txtMEPO1211-BNFPO[28,{index}]").text = item_rc

# CONTRATO
sap.session.findById(f"wnd[0]/usr/subSUB0:SAPLMEGUI:0013/subSUB2:SAPLMEVIEWS:1100/subSUB2:SAPLMEVIEWS:1200/subSUB1:SAPLMEGUI:1211/tblSAPLMEGUITC_1211/ctxtMEPO1211-KONNR[31,{index}]").text = contrato
# ITEM CONTRATO
sap.session.findById(f"wnd[0]/usr/subSUB0:SAPLMEGUI:0013/subSUB2:SAPLMEVIEWS:1100/subSUB2:SAPLMEVIEWS:1200/subSUB1:SAPLMEGUI:1211/tblSAPLMEGUITC_1211/txtMEPO1211-KTPNR[32,{index}]").text = item_contrato