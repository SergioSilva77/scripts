# Criar conta de serviço Google Sheet

## Acessar o `Console Cloud`

Acesse o link https://console.cloud.google.com/ e faça o login com a conta do google

## Criar um novo Projeto

1. Logo acima, veremos uma combobox com o nome do projeto. Clique nessa combobox

2. Depois clique em **New Project**

3. Dê um nome para o seu projeto e clique em **Create**

4. 

## Habilitar APIs e Serviços

1. Clique no menu lateral esquedo para expandir as ferramentas

2. Acesse a opção APIs & Services / Enable APIs & Services

3. Na caixa de pesquisa digite: **Google Sheets API** e aperte **Enter**

4. Clique em **Enable**

5. Na caixa de pesquisa digite: **Google Drive API** e aperte **Enter**

6. Clique em **Enable**

## Habilitando OAuth - Tipo de autenticação do Google
- Essa opção serve para definir se a autenticação é interna ou externa
- Interna: Para a organização
- Externa: Disponivel para qualquer usuario que tenha conta google
1. Acesse a opção APIs & Services / OAuth consent screen (Tela de permissão OAuth)
2. Clique na opção **Externo**
3. Clique no botão **Criar**
4. Preencha os campos:
	- **Nome do App**
	- **E-mail para suporte do usuário
	- **Endereços de e-mail
5. Clique no botão **Salvar e Continuar**
6. Na tela **Escopo** não altere nada. Se vc quiser limitar o usuário pode definir por exemplo apenas um escopo de leitura
7. Clique em **Salvar e continuar**
8. Na tela **Usuários de testes** pode pular tudo
9. Clique em **Salvar e continuar**
10. Na tela **Resumo** será exibida algumas informações, Clique em **Voltar para o painel**

## Criando uma Credencial

1. Acesse a opção APIs & Services / Credentials
2. Clique na opção **Create Credentials / Service Account**
3. Dê um nome para a sua conta de serviço
4. Enquanto vc digita o nome da sua conta de serviço, abaixo vai aparecendo o e-mail com esse nome
5. Copie esse e-mail e ao finalizar os passos, vc irá precisar baixar o arquivo credentials.json
6. Ao terminar de criar, clique na sua conta de serviço ainda em **Credentials**
7. Vá na opção **Permissions**
8. Marque o check com seu e-mail criado

## Dando permissão para o e-mail
1. Entre na planilha do Google Sheets criada
2. Compartilhe essa planilha para o e-mail criado

## Obtendo o ID da planilha
1. Todas as planilhas do Google Sheet tem um ID
	- Por exemplo: https://docs.google.com/spreadsheets/d/1em7-EoAmnKrObpR3t3XiyOBSQ7tgByOmNciJQMzeqN4/edit?gid=1689681666#gid=1689681666
	- O id nesse caso seria: 1em7-EoAmnKrObpR3t3XiyOBSQ7tgByOmNciJQMzeqN4

## Aplicação com Python
1. Acesse a URL https://developers.google.com/sheets/api/quickstart/python?hl=pt-br
2. Exemplo para ler planilha. Esse exemplo usa o modo de "API KEY", não siga os exemplos dessa credentials, apenas a forma de leitura (Usando-API-Key.py)[Usando-API-Key.py]
3. Exemplo para modificar a planilha (Script-Conta-Servico.py)[Script-Conta-Servico.py]