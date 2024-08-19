import requests

files = [
    ('file', ('file', open('/content/105207-532,85.pdf', 'rb'), 'application/octet-stream'))
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