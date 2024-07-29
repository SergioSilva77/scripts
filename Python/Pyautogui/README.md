# Método usando o Powershell

## Preparar diretórios
```powershell
$diretorioProjeto = 'diretorioprojeto'
$diretorioImgs = 'diretorioscript'

$caminhoScript = [System.IO.Path]::Combine($diretorioProjeto,"Resources\Scripts\SurfaceAvancada.py")
$caminhoImagem = [System.IO.Path]::Combine($diretorioImgs,"Capture.JPG")
```

## Clicar na Imagem
Clica na imagem normalmente
```powershell
python $caminhoScript clicar_na_imagem $caminhoImagem --direcao center --confidence 0.9 --tentativas 5 --tempo_entre_tentativas 2 --intervalo_maximo 30
```

## Clicar nas Imagens
Clica na imagem normalmente
```powershell
python $caminhoScript clicar_nas_imagens $caminhoImagem --titulo_janela "TituloDaJanela" --tempo_espera 1 --direcao center --confidence 0.9 --tentativas 5 --tempo_entre_tentativas 2 --intervalo_maximo 30
```

## Foca na janela e depois clica na imagem
Foca na janela e depois efetua o clique
```powershell
python $caminhoScript clicar_na_imagem_focus_janela $caminhoImagem --titulo_janela 'TOTVS.*' --direcao center --confidence 0.9 --tentativas 5 --tempo_entre_tentativas 2 --intervalo_maximo 30
```

## Escrever Texto
Escreve um texto na tela.
```powershell
python $caminhoScript escrever "Texto a ser escrito" --tipo a --intervalo 0 --enter
```

## Esperar Imagem Aparecer
Espera até que uma imagem apareça na tela.
```powershell
python $caminhoScript esperar_imagem_aparecer $caminhoImagem --confidence 0.9 --tentativas 5 --tempo_entre_tentativas 2 --intervalo_maximo 30
```

## Verificar se Imagem está Presente
Verifica se uma imagem está presente na tela.
```powershell
python $caminhoScript imagem_presente $caminhoImagem --confidence 0.9
```

## Obter Posições das Imagens
Obtém as coordenadas das imagens encontradas.
```powershell
python $caminhoScript obter_posicoes $caminhoImagem --titulo_janela "TituloDaJanela" --confidence 0.9 --tentativas 5 --tempo_entre_tentativas 2 --intervalo_maximo 30
```



## Comandos Disponíveis

Abaixo está uma lista dos comandos disponíveis e suas opções.

| Comando                   | Descrição                                   | Argumentos                                                                                        |
|---------------------------|---------------------------------------------|---------------------------------------------------------------------------------------------------|
| `clicar_nas_imagens`      | Clica em todas as imagens encontradas       | `imagem_path`, `--titulo_janela`, `--tempo_espera`, `--direcao`, `--confidence`, `--tentativas`, `--tempo_entre_tentativas`, `--intervalo_maximo` |
| `clicar_na_imagem`        | Clica na primeira imagem encontrada         | `caminho_img`, `--direcao`, `--confidence`, `--tentativas`, `--tempo_entre_tentativas`, `--intervalo_maximo`                          |
| `escrever`                | Escreve um texto na tela                    | `value`, `--tipo`, `--intervalo`, `--enter`                                                      |
| `esperar_imagem_aparecer` | Espera até que uma imagem apareça na tela   | `caminho_img`, `--confidence`, `--tentativas`, `--tempo_entre_tentativas`, `--intervalo_maximo`   |
| `imagem_presente`         | Verifica se uma imagem está presente na tela| `image_path`, `--confidence`                                                                      |
| `obter_posicoes`          | Obtém as coordenadas das imagens encontradas| `imagem_path`, `--titulo_janela`, `--confidence`, `--tentativas`, `--tempo_entre_tentativas`, `--intervalo_maximo`                     |
