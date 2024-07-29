# Chamar API e passar certificado

Instale o pacote `pip install cryptography` e execute o comando que está no arquivo [PFX-To-CRT-PEM](PFX-To-CRT-PEM.py) para converter o arquivo.pfx em .crt e .pem




Após configure o powershell para passar o certificado abaixo:

``` powershell
# Definir o caminhos dos certificados
$certPath = 'C:\path\to\your\certificate.crt'
$keyPath = 'C:\path\to\your\certificate.key'
 
# Carregar o certificado
$cert = New-Object System.Security.Cryptography.X509Certificates.X509Certificate2
$cert.Import($certPath, $null, 'DefaultKeySet')

Invoke-WebRequest -Certificate $cert -Uri 'https://'
```
