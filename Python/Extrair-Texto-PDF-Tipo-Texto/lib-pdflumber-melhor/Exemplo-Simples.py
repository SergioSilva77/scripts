import pdfplumber

# Substitua 'caminho_para_seu_arquivo.pdf' pelo caminho real do seu arquivo PDF
caminho_pdf = '/content/105221-2.199,20.pdf'

# Usando pdfplumber para abrir o PDF e extrair texto
with pdfplumber.open(caminho_pdf) as pdf:
    texto_total = ''
    for pagina in pdf.pages:
        # Extrair texto de cada página
        texto_pagina = pagina.extract_text()
        if texto_pagina:
            texto_total += texto_pagina

# Agora você pode imprimir o texto ou fazer o que precisar com ele
print(texto_total)