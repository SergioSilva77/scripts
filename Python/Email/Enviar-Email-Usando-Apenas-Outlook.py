import win32com.client as win32

def send_email(account_name, to, subject, body):
    # Criar uma instância do Outlook
    outlook = win32.Dispatch('outlook.application')
    
    # Obter a conta correta
    namespace = outlook.GetNamespace("MAPI")
    account = None
    for acc in namespace.Accounts:
        if acc.DisplayName == account_name:
            account = acc
            break
    
    if not account:
        raise ValueError(f"Account with name {account_name} not found.")
    
    # Criar um novo e-mail
    mail = outlook.CreateItem(0)

    # Configurar os parâmetros do e-mail
    mail._oleobj_.Invoke(*(64209, 0, 8, 0, account))  # Definir a conta específica
    mail.To = to
    mail.Subject = subject
    mail.Body = body

    # Enviar o e-mail
    mail.Send()

# Exemplo de uso
conta_nome = "email@email"  # Nome da conta conforme aparece no Outlook
destinatario = "email@email"
assunto = "Assunto do e-mail"
corpo = "Corpo do e-mail"

send_email(conta_nome, destinatario, assunto, corpo)
