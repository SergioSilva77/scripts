import fitz

def scrape(keyword, filePath):
    results = [] # list of tuples that store the information as (text, font size, font name)
    pdf = fitz.open(filePath) # filePath is a string that contains the path to the pdf
    for page in pdf:
        dict = page.get_text("dict")
        blocks = dict["blocks"]
        for block in blocks:
            if "lines" in block.keys():
                spans = block['lines']
                for span in spans:
                    data = span['spans']
                    for lines in data:
                        if keyword in lines['text'].lower(): # only store font information of a specific keyword
                            results.append((lines['text'], lines['size'], lines['font']))
                            # lines['text'] -> string, lines['size'] -> font size, lines['font'] -> font name
    pdf.close()
    return results

resultado = scrape('',r"C:\Users\sserg\OneDrive\Área de Trabalho\CV - Amanda Souza da Silva.pdf")
print(resultado)