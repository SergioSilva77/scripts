def iniciar_edge():
    caminho_config = os.path.abspath(os.path.join(os.path.dirname(__file__), 'config.properties'))
    options = webdriver.EdgeOptions()
    try:
        # Configura o serviço do Edge WebDriver
        service = EdgeService(EdgeChromiumDriverManager().install())

        # Opções para o Edge
        options.use_chromium = True
        options.add_experimental_option("detach", True)
        
        # Inicia o driver do Edge
        driver = webdriver.Edge(service=service, options=options)

        # Abre a URL desejada
        url = obter_valor_propriedade(caminho_config, 'SITE', 'url')
        driver.get(url)
        
        # Obter acessos
        usuario = obter_valor_propriedade(caminho_config, 'SITE', 'usuario')
        senha = obter_valor_propriedade(caminho_config, 'SITE', 'senha')

        # Setar acessos
        driver.find_element(By.CSS_SELECTOR, 'input#DomainUserName').send_keys(usuario)
        driver.find_element(By.CSS_SELECTOR, 'input#UserPass').send_keys(senha)
        driver.find_element(By.CSS_SELECTOR, 'input#UserPass').send_keys(Keys.ENTER)
        driver.find_element(By.XPATH, "//div[@class='tswa_ttext' and contains(text(),'Automotivo')]").click()
        
        return driver

    except Exception as e:
        print(f"Ocorreu um erro: {e}")
       
        driver_path = obter_valor_propriedade(caminho_config, 'DESKTOP', 'caminho_driver')
        url = obter_valor_propriedade(caminho_config, 'SITE', 'url')
        usuario = obter_valor_propriedade(caminho_config, 'SITE', 'usuario')
        senha = obter_valor_propriedade(caminho_config, 'SITE', 'senha')

        if driver_path is None or str(driver_path).strip() == '':
            print('Não foi definida a propriedade')

        if driver_path:
            print(f"Usando o driver localizado em: {driver_path}")
            service = EdgeService(executable_path=driver_path)
            driver = webdriver.Edge(service=service, options=options)
            driver.get(url)

            driver.find_element(By.CSS_SELECTOR, 'input#DomainUserName').send_keys(usuario)
            driver.find_element(By.CSS_SELECTOR, 'input#UserPass').send_keys(senha)
            driver.find_element(By.CSS_SELECTOR, 'input#UserPass').send_keys(Keys.ENTER)
            driver.find_element(By.XPATH, "//div[@class='tswa_ttext' and contains(text(),'Automotivo')]").click()
            return driver
        else:
            print("Não foi possível iniciar o Edge WebDriver.")
            return None