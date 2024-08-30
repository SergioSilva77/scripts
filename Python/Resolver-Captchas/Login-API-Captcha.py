import requests
import json
import pandas

url = "https://mtrr.cetesb.sp.gov.br/api/mtr/carregaDadosLogin"

downloadFolder = 'C:/Users/sergi/Downloads/test'
#recaptcha = df['tokenText'][0]
recaptcha = '03AL8dmw8907JO6pFR3F-1aXyE51yg83U9o00cGLucJznDyk5eDWFqpTpjeQxEeY1xkQZOq5Uq438iqT8vexpfiDrlHo9SFXFs-qGp5Nexkux01KisC1_sLPosE_mjGzoheYKtqamQ3XJb3K_oRuGO_rcglzdO2DPOmtpDZ730XsSU97MoIZFOTjT5twIzq_lPcEh5A2GoZqXGsZFk7ZK2EJzxmHfI0Imcgiiqti53RTpVP8ui3iJwMMduzmN36MpgHLq4b-Vaq0NO1nkUE_unI8UhImFmqYwyKDM1M1i9x2OgX2-7hiwDNDuOvnDECs-dOPDftTFU9kyr0g8IBCifMmxJaXIK0PhijVCskMtMqwg5phEh5YPxRSzk1wUBgmJIYKqLizKi3IDK9K5GcQcX3KYurWM0fYNy_QDmFDagahV2oPNJCiX2EmKsDeZi9hgrb8wrSQB81q7PstFYByS3mUBEohVhWbgJgf8XDAbQh4dwO5vTQFspBMUHzc3TWsWP-FSg8md5RPHn9SO-DbYPcL3_El-mFxdhKvqIMXFzlraYm5efcGMgECjR3tsItZKHjflNFaKw9T4QxBSIEMA94-k19Qnq0etjbA'
login =  ""
email=""
senha = ""
parCodigo = 2703
sistema = 0

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

url = "https://mtrr.cetesb.sp.gov.br/api/mtr/pesquisaManifestoRelatorioMtr/2703/26/8/26-03-2023/26-06-2023/5/0/9/0"

payload = {}
headers = {
	'Authorization': f'Bearer {token}'
}

response = requests.request("GET", url, headers=headers, data=payload)
if response.status_code != 200:
    raise Exception("Erro consultar Numero MTR")


objetoRespostas = response.json()['objetoResposta']

print("MTRs Obtidas")

for objetoResposta in objetoRespostas:
    try:
        manNumero = objetoResposta['manNumero']
        
        print(f'Consultando MTR: {manNumero}')

        url = f"https://mtrr.cetesb.sp.gov.br/api/mtr/pesquisaManifesto/2703/26/8/null/null/0/all/{manNumero}"
        
        response = requests.request("GET", url, headers=headers, data=payload)
        
        if response.status_code != 200:
            raise Exception("Consulta_PDF_MTR")
               
        manHashCode = response.json()['objetoResposta'][0]['manHashCode']
        
        url = f"https://mtrr.cetesb.sp.gov.br/api/mtr/imprimir/imprimeRecebimentoManifesto/{manHashCode}"
        
        output_file = downloadFolder + str(manNumero) + ".pdf"
        
        response = requests.request("GET", url, headers=headers, data=payload)
        
        if response.status_code != 200:
            raise Exception("Erro consultar PDF")
            
        with open(output_file, "wb") as f:
            
            for chunk in response.iter_content(chunk_size=4096):
                f.write(chunk)
                
            f.close()
        
        print(f'Finalizado MTR: {manNumero}')
        
    except BaseException as e:
        print(f"Error: {str(e)}")