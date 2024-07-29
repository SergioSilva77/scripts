# Converter de WEBM para MP4

Converter WEBM para MP4
```BASH
ffmpeg -i input.webm -c:v libx264 -preset slow -crf 22 -c:a aac -b:a 128k output.mp4
```

Converter WEBM para MP3
```BASH
-i \"{arquivoOriginal}\" -i \"{caminhoImagemCapa}\" -map 0:a -map 1:v -metadata title=\"{titulo}\" -metadata artist=\"{autor}\" -metadata album=\"{album}\" -metadata genre=\"{genero}\" -c:v copy -acodec libmp3lame -b:a 320k -id3v2_version 3 \"{outputFilePath}\"
```

