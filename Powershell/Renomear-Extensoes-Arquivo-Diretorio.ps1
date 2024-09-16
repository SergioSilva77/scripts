Get-ChildItem -Path C:\Users\sergi\Documents\git\scripts\javascript\*.* | ForEach-Object {
    $newName = $_.Name -replace '\..*$', '.txt'
    Rename-Item $_.FullName -NewName $newName
}
