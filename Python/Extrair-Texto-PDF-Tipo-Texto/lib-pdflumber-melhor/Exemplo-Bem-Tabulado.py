import re
import json
import pdfplumber

caminho_pdf = r"C:\Users\sserg\OneDrive\Área de Trabalho\CV - Amanda Souza da Silva.pdf"

# Esta função é apenas um exemplo genérico que você precisará adaptar
def processar_texto_para_csv(texto):
    # Substitua isso pela sua própria lógica de separação de colunas
    # Aqui estamos assumindo que as colunas são separadas por dois ou mais espaços
    linhas = texto.split('\n')
    linhas_csv = []
    for linha in linhas:
        colunas = re.split(r'\s{2,}', linha)  # Separa a linha por dois ou mais espaços
        linhas_csv.append(';'.join(colunas))  # Junta as colunas com ponto e vírgula
    return '\n'.join(linhas_csv)

with pdfplumber.open(caminho_pdf) as pdf:
    texto_total = []
    for pagina in pdf.pages:
        texto_pagina = pagina.extract_text()
        if texto_pagina:
            # Processa o texto da página para o formato CSV
            texto_csv_pagina = processar_texto_para_csv(texto_pagina)
            texto_total.append(texto_csv_pagina)

# Agora temos todas as páginas processadas em formato CSV
texto = '\n'.join(texto_total)
print(texto)