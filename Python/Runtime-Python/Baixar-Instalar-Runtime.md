# Baixar python para Runtime
## Usando Windows
### Download 
1. Acesse o link https://www.python.org/downloads/windows/ e baixe a versão **Windows embeddable package (64-bit)** se seu windows for x64

### Baixe o arquivo get-pip
1. Baixe através desse link https://bootstrap.pypa.io/get-pip.py
2. Instale usando o comando com o python baixado
    ```python
    python get-pip.py
    ```

### Configure o arquivo python312._pth
Vá na pasta do Python e configure o arquivo `python312._pth` e adicione a linha abaixo na última linha do arquivo:

```
Lib\site-packages
```


### Adicione ao PATH do sistema
Se vc quiser pode adicionar ao path do sistema. Aponte o caminho da pasta do python descompactada para o Path
