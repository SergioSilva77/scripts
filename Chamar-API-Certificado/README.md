# Chamar API e passar certificado

Instale o pacote `pip install cryptography` e execute o comando abaixo para converter o arquivo.pfx em .crt e .pem

``` python
def convert_pfx_to_crt_and_pem(pfx_path, pfx_password, output_directory, output_name):
    # Create the output directory if it doesn't exist
    os.makedirs(output_directory, exist_ok=True)
    
    # Define the paths for the .crt and .pem files
    crt_path = os.path.join(output_directory, f"{output_name}.crt")
    pem_path = os.path.join(output_directory, f"{output_name}.pem")

    # Read the PFX file
    with open(pfx_path, 'rb') as pfx_file:
        pfx_data = pfx_file.read()

    # Load the PFX file
    private_key, certificate, additional_certificates = pkcs12.load_key_and_certificates(
        pfx_data, pfx_password.encode()
    )

    # Write the certificate to a .crt file
    with open(crt_path, 'wb') as crt_file:
        crt_file.write(certificate.public_bytes(serialization.Encoding.PEM))

    # Write the private key and the certificate to a .pem file
    with open(pem_path, 'wb') as pem_file:
        pem_file.write(private_key.private_bytes(
            encoding=serialization.Encoding.PEM,
            format=serialization.PrivateFormat.TraditionalOpenSSL,
            encryption_algorithm=serialization.NoEncryption()
        ))
        pem_file.write(certificate.public_bytes(serialization.Encoding.PEM))
```

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
