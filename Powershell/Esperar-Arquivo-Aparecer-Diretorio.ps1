$regex = 'certificado*'
$path = '?{caminho}'
$timeout = 30

function encontrar-arquivos{
    param([string]$path, [string]$regex)
    $files = Get-ChildItem -Path $path -Filter $regex | Sort-Object LastWriteTime -Descending | Select-Object -First 1 | Where-Object {$_.Name -match '.pdf$'}

    if ($files.Length -gt 0){
        
        return $true
    } else {
        return $false
    }
}

while (-not(encontrar-arquivos -path $path -regex $regex) -and $timeout -gt 0){
    Write-Host $timeout
    $timeout = $timeout - 1
    Start-Sleep -Milliseconds 1
}

$files = Get-ChildItem -Path $path -Filter $regex
$caminhoCompleto = ''

if ($files.length -gt 0){
    $caminhoCompleto = $files[0].FullName
    $msg = 'nao encontrou'
} else {
    $msg = 'encontrou'
}