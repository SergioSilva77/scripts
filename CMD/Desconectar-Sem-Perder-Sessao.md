## Script 1

Usando CMD
``` CMD
for /f "skip=1 tokens=3" %%s in ('query user %USERNAME%') do (
  %windir%\System32\tscon.exe %%s /dest:console
)
```

Usando Powershell
``` powershell
@powershell -NoProfile -ExecutionPolicy unrestricted -Command "$sessionid=((quser $env:USERNAME | select -Skip 1) -split '\s+')[2]; tscon $sessionid /dest:console"
```