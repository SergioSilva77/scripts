import os
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.serialization import pkcs12
import banco_db
import sys
import base64
import gzip
from io import BytesIO
import time
from lxml import etree
from datetime import datetime, timedelta

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

def process_pfx_files(root_directory, pfx_password):
    for subdir, _, files in os.walk(root_directory):
        for file in files:
            if file.lower().endswith('.pfx'):
                pfx_path = os.path.join(subdir, file)
                folder_name = os.path.basename(subdir)
                convert_pfx_to_crt_and_pem(pfx_path, pfx_password, subdir, folder_name)


root_directory = r'C:\TEMP\t1'
pfx_password = '33299779'

process_pfx_files(root_directory, pfx_password)
