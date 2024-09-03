import fitz  # PyMuPDF
import os

def extract_pages_as_images(pdf_path, output_folder, dpi=200):
    # Abre o arquivo PDF
    doc = fitz.open(pdf_path)

    # Cria o diretório de saída se ele não existir
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)

    # Itera sobre cada página do PDF
    for page_number in range(len(doc)):
        # Obtém a página
        page = doc.load_page(page_number)

        # Define a escala de pixels por polegada (DPI)
        zoom = dpi / 72  # A resolução padrão do PDF é 72 DPI
        mat = fitz.Matrix(zoom, zoom)

        # Renderiza a página como uma imagem
        pix = page.get_pixmap(matrix=mat)

        # Cria um nome de arquivo para a imagem
        image_filename = f"page_{page_number + 1}.png"

        # Caminho completo onde a imagem será salva
        full_path = os.path.join(output_folder, image_filename)

        # Salva a imagem
        pix.save(full_path)
    
    print(f"Páginas do PDF extraídas como imagens na pasta: {output_folder}")

# Caminho para o arquivo PDF
pdf_path = r"C:\Users\sserg\OneDrive\Área de Trabalho\tema2.pdf"

# Diretório onde as imagens serão salvas
output_folder = r'C:\Users\sserg\OneDrive\Área de Trabalho\Nova pasta'

# Chamada da função
extract_pages_as_images(pdf_path, output_folder)
