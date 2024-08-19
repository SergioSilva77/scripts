import google.generativeai as genai

API_KEY = 'AIzaSyAGucnkRkla_kXd6iOXjgZh5Lv3ApSFv6Q'
genai.configure(api_key=API_KEY)
model = genai.GenerativeModel('gemini-pro')
response = model.generate_content('Por que o céu é azul?')
print(response.text)