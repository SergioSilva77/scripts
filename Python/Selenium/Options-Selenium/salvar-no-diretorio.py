 # Preferencias
    prefs = {
        "download.default_directory": caminho_downloads,  # Define o diretório de download
        "download.prompt_for_download": False,  # Desativa o prompt de download
        "plugins.always_open_pdf_externally": True  # Abre PDFs diretamente em vez de visualizá-los no Chrome
    }
    options.add_experimental_option("prefs", prefs)