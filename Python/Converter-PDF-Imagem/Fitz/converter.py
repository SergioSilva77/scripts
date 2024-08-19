import fitz  # PyMuPDF

# Caminho para o arquivo PDF
caminho_pdf = r"C:\Users\sserg\OneDrive\Área de Trabalho\CV - Amanda Souza da Silva.pdf"

# Abrir o documento PDF
documento = fitz.open(caminho_pdf)

# Iterar sobre todas as páginas do PDF
for numero_pagina in range(len(documento)):
    # Selecionar a página
    pagina = documento.load_page(numero_pagina)
    
    # Definir a resolução (zoom_x e zoom_y aumentam a qualidade)
    zoom_x = 2.0  # 2 significa 200% de zoom
    zoom_y = 2.0
    matriz = fitz.Matrix(zoom_x, zoom_y)

    # Renderizar a página como imagem
    imagem_pix = pagina.get_pixmap(matrix=matriz)
    
    # Salvar a imagem
    imagem_pix.save(f'pagina_{numero_pagina + 1}.png')

# Fechar o documento
documento.close()
