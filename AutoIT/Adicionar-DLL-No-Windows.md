# Adicionar a DLL do AutoIT no Windows

Entre no diretório do AutoIT instalado, geralmente nesses diretórios:
1. x64 `C:\Program Files (x86)\AutoIt3\AutoItX`

Encontre o arquivo `AutoItX3.dll`

Abra o CMD como admin e aplique o comando abaixo
``` bash
regsvr32 "C:\Program Files (x86)\AutoIt3\AutoItX\AutoItX3.dll"
```