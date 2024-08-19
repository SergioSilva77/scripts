import boto3

# Configuração das credenciais da AWS
aws_access_key_id = 'AKIAUNITHKZSO2G45ANF'
aws_secret_access_key = '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'
aws_region = 'us-east-1'  # Substitua pela região AWS apropriada

# Inicialize o cliente Textract
textract_client = boto3.client('textract', region_name=aws_region, aws_access_key_id=aws_access_key_id, aws_secret_access_key=aws_secret_access_key)

# Abra a imagem que você deseja analisar
with open('/content/Sem título.png', 'rb') as image:
    response = textract_client.analyze_document(
        Document={
            'Bytes': image.read()
        },
        FeatureTypes=['FORMS']  # Especifique os tipos de recursos que você deseja extrair (pode ser 'TABLES', 'FORMS', 'LINES', 'WORDS', etc.)
    )

# Verifique o resultado
for item in response['Blocks']:
    if item['BlockType'] == 'LINE':
        print(item)