@echo off
set /p nome=Informe o nome ou IP do destino: 
echo O nome escolhido foi '%nome%'.
ping.exe %nome%