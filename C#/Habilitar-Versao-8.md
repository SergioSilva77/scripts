# Habilitar C# 8.0

1. Clicar com botão direito em cima do projeto
2. Ir para 'descarregar projeto'
3. Colar o código abaixo conforme a estrutura abaixo, pegue apenas a tag 'PropertyGroup e tudo dentro'


``` xml
<Project Sdk="Microsoft.NET.Sdk.WindowsDesktop">

  <!-- ... outras configurações ... -->

  <PropertyGroup>
    <!-- Adicione a versão da linguagem aqui -->
    <LangVersion>8.0</LangVersion>
  </PropertyGroup>

  <!-- ... outras configurações ... -->

</Project>
```
