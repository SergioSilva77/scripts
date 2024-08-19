import google.generativeai as genai
import PIL.Image

genai.configure(api_key="AIzaSyAGucnkRkla_kXd6iOXjgZh5Lv3ApSFv6Q")

for m in genai.list_models():
    if 'generateContent' in m.supported_generation_methods:
        print(m.name)

model = genai.GenerativeModel('gemini-pro-vision')

img = PIL.Image.open('/content/Screenshot_69.png')

response = model.generate_content(img)

response = model.generate_content(["cite qual os valores dessas colunas e organize em csv com ';' e cabeçalho (serviço, descrição do serviço, cod mecanico, quant, vlr unitario, desc, %, valr total) e as colunas (Produto, Descrição do Item, Quant., Vlr Unitário, Vlr Total). Não escreva nada além dessas informações", img])
response.resolve()
print(response.text)