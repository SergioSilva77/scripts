# Documentação do Gerador de JWT

Este script em Python é usado para gerar um token JWT (JSON Web Token) que pode ser utilizado para autenticação segura e troca de informações em aplicações web. O token é assinado usando o algoritmo RS256, garantindo que apenas partes com a chave privada correspondente possam emitir tokens válidos.

## Bibliotecas Usadas

- `jwt`: Biblioteca para codificar, decodificar e validar tokens JWT.
- `time`: Biblioteca para manipular tempos e datas, usada aqui para definir a validade do token.

## Funcionamento do Script

1. **Obtenção do Timestamp Atual**:
   - O script usa a função `time.time()` para obter o timestamp atual em segundos desde a época Unix.

2. **Definição de `exp` e `iat`**:
   - `exp`: O campo "exp" (expiration time) é definido para 1 hora após a emissão do token.
   - `iat`: O campo "iat" (issued at) registra o momento de emissão do token.

3. **Leitura da Chave Privada**:
   - A chave privada usada para assinar o token é lida de um arquivo no sistema. É essencial manter esta chave segura.

4. **Construção do Payload**:
   - `iss`: Identificador do emissor do token.
   - `aud`: URI da entidade para a qual o token é destinado.
   - `scope`: Define o escopo de acesso do token. Aqui, configurado para `*` que representa um escopo irrestrito.
   - `iat`: Timestamp de quando o token foi emitido.
   - `exp`: Timestamp de quando o token expirará.

5. **Geração e Impressão do JWT**:
   - O token é gerado e assinado usando a chave privada e o algoritmo RS256.
   - O token assinado é impresso na saída padrão.

## Exemplo de Saída

A saída do script será o token JWT codificado, que pode ser utilizado em headers de autenticação HTTP ou em outros contextos onde tokens JWT são aceitos.