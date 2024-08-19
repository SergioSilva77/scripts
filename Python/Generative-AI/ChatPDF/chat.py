import requests

files = [
    ('file', ('file', open('/content/105221-2.199,20.pdf', 'rb'), 'application/octet-stream'))
]
headers = {
    'x-api-key': 'sec_HsLsMdMRjMvcQHwO0i2auy8nNbvpt9cm'
}

response = requests.post(
    'https://api.chatpdf.com/v1/sources/add-file', headers=headers, files=files)

if response.status_code == 200:
    print('Source ID:', response.json()['sourceId'])
else:
    print('Status:', response.status_code)
    print('Error:', response.text)

sourceID = response.json()['sourceId']

data = {
    'sourceId': sourceID,
    'messages': [
        {
            'role': "user",
            'content': "separe as colunas e o valores das colunas desse pdf, pois vou pegar o texto e transformar para csv, preciso que vc separe por ponto e virgula. não mande desculpa, tente fazer, isso é o que importa",
        }
    ]
}

response = requests.post(
    'https://api.chatpdf.com/v1/chats/message', headers=headers, json=data)

if response.status_code == 200:
    resposta1 = response.json()['content']
    print('Result:', resposta1)
else:
    print('Status:', response.status_code)
    print('Error:', response.text)