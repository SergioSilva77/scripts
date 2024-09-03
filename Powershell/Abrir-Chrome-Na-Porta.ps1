# Caminho para o executável do Google Chrome
$chromePath = "C:\Program Files\Google\Chrome\Application\chrome.exe"

# URL que você deseja abrir
$url = "http://localhost:8080"

# Porta que você deseja usar
$port = 9222

# Inicia o Google Chrome com as opções desejadas
Start-Process $chromePath -ArgumentList "--remote-debugging-port=$port", $url