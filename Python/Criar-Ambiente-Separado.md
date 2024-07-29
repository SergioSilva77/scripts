# Comandos para ativar o ambiente virtual do python

## USO
Aplique o comando abaixo para configurar a política de execução
``` powershell
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```

Entre no diretório do seu projeto python
``` powershell
cd directory
```

Aplique o comando:
``` powershell
python -m venv <nome_que_vc_quer_dar_ao_seu_ambiente_virtual>
```

Em seguida aplique o comando
``` powershell
.\seuprojeto\Scripts\activate.ps1
```