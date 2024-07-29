import jwt
import time

current_time = int(time.time())
exp = current_time + 3600  # expira em 1 hora
iat = current_time  # emitido agora

private_key = open("C:\\Users\\sergi\\Desktop\\Jaque\\privateKey.pem", "r").read()

payload = {
    "iss": "exemplo@db........iam.acesso.io",
    "aud": "https://identity.acesso.io",
    "scope": "*",
    "iat": iat,
    "exp": exp
}
signed = jwt.encode(payload, private_key, algorithm='RS256')

print(signed)