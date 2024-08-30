import configparser

config = configparser.ConfigParser()
try:
    with open('config.properties', 'r', encoding='utf-8') as f:
        config.read_file(f)
        driver_path = config.get('DEFAULT', 'caminho_driver')
        print(f"Driver path: {driver_path}")
except (configparser.NoSectionError, configparser.NoOptionError, FileNotFoundError, UnicodeDecodeError) as ex:
    print(f"Erro ao carregar o arquivo de propriedades: {ex}")
