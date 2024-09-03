import unicodedata

def remove_acentos_e_simbolos(input_str):
    nfkd_form = unicodedata.normalize('NFKD', input_str)
    only_ascii = nfkd_form.encode('ASCII', 'ignore')
    return re.sub(r'[^A-Za-c \_]', '', only_ascii.decode())