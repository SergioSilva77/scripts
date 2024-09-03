import os
import json
import sys
import requests
import re
import pandas as pd, unicodedata, re
from datetime import datetime, timedelta
from dateutil.relativedelta import relativedelta
import time
import shutil
import pdfplumber


data_atual = datetime.now()
data_inicio = (data_atual.replace(day=1) - relativedelta(months=4)).strftime('%d-%m-%Y')
data_fim = (data_atual - timedelta(days=1)).strftime('%d-%m-%Y')

login =  "13464032000190"
#--------------(VARIAVEIS AE)
recaptcha = df['TokenText'][0]
caminho = str(df['CurrentDirectory'][0]).replace('file:///','')
caminhoOndrive = str(df['caminhoOnedrive'][0])
email=df['Username'][0]
senha = df['Password'][0]
#--------------(VARIAVEIS AE)
parCodigo = 2703
sistema = 0

#--------------(MOCK)
# recaptcha = '03AL8dmw8907JO6pFR3F-1aXyE51yg83U9o00cGLucJznDyk5eDWFqpTpjeQxEeY1xkQZOq5Uq438iqT8vexpfiDrlHo9SFXFs-qGp5Nexkux01KisC1_sLPosE_mjGzoheYKtqamQ3XJb3K_oRuGO_rcglzdO2DPOmtpDZ730XsSU97MoIZFOTjT5twIzq_lPcEh5A2GoZqXGsZFk7ZK2EJzxmHfI0Imcgiiqti53RTpVP8ui3iJwMMduzmN36MpgHLq4b-Vaq0NO1nkUE_unI8UhImFmqYwyKDM1M1i9x2OgX2-7hiwDNDuOvnDECs-dOPDftTFU9kyr0g8IBCifMmxJaXIK0PhijVCskMtMqwg5phEh5YPxRSzk1wUBgmJIYKqLizKi3IDK9K5GcQcX3KYurWM0fYNy_QDmFDagahV2oPNJCiX2EmKsDeZi9hgrb8wrSQB81q7PstFYByS3mUBEohVhWbgJgf8XDAbQh4dwO5vTQFspBMUHzc3TWsWP-FSg8md5RPHn9SO-DbYPcL3_El-mFxdhKvqIMXFzlraYm5efcGMgECjR3tsItZKHjflNFaKw9T4QxBSIEMA94-k19Qnq0etjbA'
# caminho = r'C:\Users\sergi\Desktop\cef21\test'
# caminhoOndrive = r'C:\Users\sergi\Desktop\cef21\onedrive'
# email='angela_higuchi@ttsrb.com.br'
# senha = 'automationedge'
#--------------(MOCK)


#--------------(CRIA AS PASTAS)
if not os.path.exists(caminhoOndrive):
    os.makedirs(caminhoOndrive)

if not os.path.exists(caminho):
    os.makedirs(caminho)
#--------------(CRIA AS PASTAS)

caminho_excel = rf'{caminho}\Resultado.xlsx'
arrStatus = []

def obter_dados_planilha(token):
    xl = pd.read_excel(caminho_excel)
    xl['Status'] = ''
    linhas_recebidas = xl[xl['Situação'] == 'Recebido']
    linhas_nao_recebidas = xl[xl['Situação'] != 'Recebido']

    for indice, linha in linhas_nao_recebidas.iterrows():       
        numero_mtr = linha['Nº MTR']  
        xl.loc[xl['Nº MTR'] == int(linha['Nº MTR']), 'Status'] = f'Situação diferente de "Recebido"'

    for indice, linha in linhas_recebidas.iterrows():        
        try:           
            numero_mtr = linha['Nº MTR'] 
            numero_cdf = linha['CDF Nº']
            data_recebimento = linha['Data Recebimento']
            data_recebimento = datetime.strptime(data_recebimento, "%d/%m/%Y")   
            data_recebimento = data_recebimento.strftime("%Y%m")     
            if (not pd.isna(numero_cdf)):
                numero_cdf = int(numero_cdf)
                
            destinoEmpresa = remove_acentos_e_simbolos(str(linha['Destinador (nome)']).replace(' ', '_'))
            destinoEmpresa = destinoEmpresa.split('_')[0]
            observacao = linha['Observação Gerador']
            destinoOrigem = str(observacao).split('_')
            
            if (len(destinoOrigem) >= 3):
                destinoOrigem = remove_acentos_e_simbolos(destinoOrigem[2].strip().replace(' ','_'))
            else:
                destinoOrigem = ''

            pasta_origem = fr'{caminhoOndrive}\{destinoOrigem}'
            output_file = rf'{caminhoOndrive}\{destinoOrigem}\{data_recebimento} {numero_cdf} {destinoEmpresa} {destinoOrigem}.pdf'
            [status_code, status] = api_baixar_mtr(numero_cdf, pasta_origem, output_file, token)
            data_pdf = ''

            # if (status_code != -1):
            #     [status_code, data_pdf, msg] = obter_data_pdf(output_file)
            #     if status_code == -1:
            #         status = msg
            #     else:
            #         directory, file_name = os.path.split(output_file)   
            #         data_pdf = datetime.strptime(data_pdf, "%d/%m/%Y")   
            #         data_pdf = data_pdf.strftime("%Y%m%d")          
            #         new_file_name = f'{data_pdf} {file_name}'
            #         new_file_path = os.path.join(directory, new_file_name)
            #         shutil.move(output_file, new_file_path)

            xl.loc[xl['Nº MTR'] == int(numero_mtr), 'Status'] = status
            print(f"Situacao {linha['Situação']} - MTR {linha['Nº MTR']} - Status {status}")
        except BaseException as e:
            xl.loc[xl['Nº MTR'] == int(linha['Nº MTR']), 'Status'] = f'Erro: {str(e)}'
            print(f"Situacao {linha['Situação']} - MTR {linha['Nº MTR']} - Status Erro: {e}")

    xl.to_excel(caminho_excel, index=False)
    pass

def obter_data_pdf(output_file):
    for i in range(10):
        try:
            # converter pdf para texto
            text = ''
            with pdfplumber.open(output_file) as pdf:
                for page in pdf.pages:
                    text += page.extract_text()

            # procurar data dentro do pdf
            pattern = r"(\d{2}/\d{2}/\d{4})\nRespons[áaA]vel"
            match = re.search(pattern, text)
            if match:
                matched_content = match.group(1)
                return [1, matched_content.strip(),'']
            else:
                return [-1,'','Não foi possível encontrar a data do pdf']
        except BaseException as e:
            time.sleep(1)
            continue
    return [-1,'',f'Não foi possível encontrar: {output_file}']

def remove_acentos_e_simbolos(input_str):
    nfkd_form = unicodedata.normalize('NFKD', input_str)
    only_ascii = nfkd_form.encode('ASCII', 'ignore')
    return re.sub(r'[^A-Za-c \_]', '', only_ascii.decode())

def fazer_checkin():
    pass

def api_gerar_token():
    print('Gerando token')
    url = "https://mtrr.cetesb.sp.gov.br/api/mtr/carregaDadosLogin"
    payload = json.dumps({
        "sistema": sistema,
        "login": login,
        "email": email,
        "senha": senha,
        "parCodigo": parCodigo,
        "recaptcha": recaptcha
    })
    headers = {
        'Content-Type': 'application/json'
    }
    response = requests.request("POST", url, headers=headers, data=payload)
    if response.status_code != 200:
        raise Exception("Erro ao logar")
    token = response.json()['objetoResposta']['token']
    print('Login Efetuado!')
    return token

def api_baixar_planilha(token):
    print('Baixando planilha')
    url = fr"https://mtrr.cetesb.sp.gov.br/api/mtr/pesquisaManifestoRelatorioMtrAnalitico/2703/26/8/{data_inicio}/{data_fim}/5/0/9/0"
    payload = {}
    headers = {
        'Authorization': f'Bearer {token}'
    }
    response = requests.request("GET", url, headers=headers, data=payload)
    if (response.status_code != 200):
        raise Exception("Não foi possível baixar o excel")
    
    if not os.path.exists(os.path.dirname(caminho_excel)):
        os.makedirs(os.path.dirname(caminho_excel))

    with open(caminho_excel, "wb") as f:            
        for chunk in response.iter_content(chunk_size=4096):
            f.write(chunk)
            
        f.close()
    pass



def api_baixar_mtr(numero_cdf, pastaOrigem, output_file, token):
    if os.path.exists(output_file):
        return [-1, 'O arquivo já existe'] 

    payload = {}
    headers = {
        'Authorization': f'Bearer {token}'
    }

    if len(str(numero_cdf).strip()) <= 0 or pd.isna(numero_cdf):
        return [-1, 'CDF não emitido']

    url = f"https://mtrr.cetesb.sp.gov.br/api/mtr/imprimir/imprimeCertificadoByCodigo/{int(numero_cdf)}"        
    response = requests.request("GET", url, headers=headers, data=payload)        
    if response.status_code != 200:
        for i in range(5):
            time.sleep(1)
            response = requests.request("GET", url, headers=headers, data=payload)
            if response.status_code != 200:
                return [-1, f"Não foi possível baixar CDF: Status API: {response.status_code}"]
    
    if response.status_code != 200:
        return [-1, 'Erro consultar PDF']
    
    if not os.path.exists(fr'{pastaOrigem}'):
        os.makedirs(fr'{pastaOrigem}')

    with open(output_file, "wb") as f:            
        for chunk in response.iter_content(chunk_size=4096):
            f.write(chunk)
            
        f.close()

    return [1, 'Sucesso']

token = api_gerar_token()
api_baixar_planilha(token)
obter_dados_planilha(token)