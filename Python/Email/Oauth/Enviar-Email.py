import json
import msal
import requests

def read_config_from_file(file_path):
    config = {}
    with open(file_path, 'r') as file:
        lines = file.readlines()
        for line in lines:
            key, value = line.strip().split('=')
            config[key] = value
    return config

def read_emails_from_file(file_path):
    with open(file_path, 'r') as file:
        content = file.read()
    return [email.strip() for email in content.split(';') if email.strip()]

def enviar(subject, content, config_path, email_path):
    config = read_config_from_file(config_path)

    client_id = config['client_id']
    client_secret = config['client_secret']
    tenant_id = config['tenant_id']
    authority = f"https://login.microsoftonline.com/{tenant_id}"
    username = config['username']
    password = config['password']
    userId = config['userId']
    scopes = ["https://graph.microsoft.com/.default"]

    to_user_emails = read_emails_from_file(email_path)

    app = msal.ConfidentialClientApplication(
        client_id=client_id,
        client_credential=client_secret,
        authority=authority)

    # Adquira o token com email e senha
    result = app.acquire_token_by_username_password(
        username=username,
        password=password,
        scopes=scopes
    )

    if not result:
        print("No suitable token exists in cache. Let's get a new one from Azure Active Directory.")
        result = app.acquire_token_for_client(scopes=scopes)

    if "access_token" in result:
        endpoint = f'https://graph.microsoft.com/v1.0/users/{userId}/sendMail'
        
        email_msg = {'Message': {'Subject': f"{subject}",
                                'Body': {'ContentType': 'Text', 'Content': f"{content}"},
                                'ToRecipients': [{'EmailAddress': {'Address': email}} for email in to_user_emails]
                                },
                    'SaveToSentItems': 'true'}
        r = requests.post(endpoint,
                        headers={'Authorization': 'Bearer ' + result['access_token']}, json=email_msg)
        if r.ok:
            print('Sent email successfully')
        else:
            print(r.json())
    else:
        print(result.get("error"))
        print(result.get("error_description"))
        print(result.get("correlation_id"))