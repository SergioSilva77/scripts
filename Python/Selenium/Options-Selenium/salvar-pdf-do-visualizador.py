options = webdriver.ChromeOptions()
    options.add_argument(f"user-agent={my_user_agent}")
    options.add_argument("--kiosk-printing")
    options.add_argument("--kiosk-pdf-printing")
    options.add_experimental_option("detach", True)

    # Configurações de impressão
    settings = {
        "recentDestinations": [
            {
                "id": "Save as PDF",
                "origin": "local",
                "account": ""
            }
        ],
        "selectedDestinationId": "Save as PDF",
        "version": 2
    }

    # Configurações do Chrome
    chrome_prefs = {
        "printing.print_preview_sticky_settings.appState": json.dumps(settings),
        "download.prompt_for_download": False,
        "plugins.always_open_pdf_externally": False,
        "download.open_pdf_in_system_reader": False,
        "profile.default_content_settings.popups": 0,
        "printing.print_to_pdf": True,
        "download.default_directory": caminhoDownloads,
        "savefile.default_directory": caminhoDownloads
    }
    options.add_experimental_option("prefs", chrome_prefs)

    # Setar capabilities
    caps = DesiredCapabilities.CHROME.copy()
    caps['acceptInsecureCerts'] = True
    for key, value in caps.items():
        options.set_capability(key, value)

    driver = webdriver.Chrome(service=service, options=options)