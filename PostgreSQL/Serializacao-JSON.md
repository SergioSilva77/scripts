
# PostgreSQL JSON Functions: `json_build_object` and `json_agg`

## Introdução

O PostgreSQL fornece várias funções e operadores para manipular dados JSON. Duas dessas funções úteis são `json_build_object` e `json_agg`. Esta documentação explica como usar essas funções para criar e agregar objetos JSON em suas consultas SQL.

## `json_build_object`

A função `json_build_object` é usada para construir um objeto JSON a partir de uma lista de pares chave/valor. Ela é útil quando você deseja criar um objeto JSON diretamente dentro de sua consulta SQL.

### Sintaxe

```sql
json_build_object(key1, value1, key2, value2, ...)
```

### Exemplo

Vamos supor que temos uma tabela `usuarios` com as seguintes colunas: `id`, `nome` e `email`.

```sql
SELECT json_build_object(
    'id', id,
    'nome', nome,
    'email', email
) AS usuario_json
FROM usuarios;
```

Resultado:

```json
{
    "id": 1,
    "nome": "João",
    "email": "joao@example.com"
}
```

## `json_agg`

A função `json_agg` agrega os valores de entrada em uma matriz JSON. É útil para agrupar resultados de várias linhas em uma única matriz JSON.

### Sintaxe

```sql
json_agg(expression)
```

### Exemplo

Suponha que temos a mesma tabela `usuarios` e queremos obter uma matriz JSON de todos os usuários.

```sql
SELECT json_agg(json_build_object(
    'id', id,
    'nome', nome,
    'email', email
)) AS usuarios_json
FROM usuarios;
```

Resultado:

```json
[
    {
        "id": 1,
        "nome": "João",
        "email": "joao@example.com"
    },
    {
        "id": 2,
        "nome": "Maria",
        "email": "maria@example.com"
    }
]
```

## Uso Combinado

Essas funções podem ser combinadas para criar objetos JSON complexos e aninhados. Por exemplo, se quisermos adicionar um campo `usuarios` que contém uma matriz de usuários em um objeto JSON, podemos fazer isso da seguinte maneira:

```sql
SELECT json_build_object(
    'status', 'success',
    'usuarios', json_agg(json_build_object(
        'id', id,
        'nome', nome,
        'email', email
    ))
) AS resultado_json
FROM usuarios;
```

Resultado:

```json
{
    "status": "success",
    "usuarios": [
        {
            "id": 1,
            "nome": "João",
            "email": "joao@example.com"
        },
        {
            "id": 2,
            "nome": "Maria",
            "email": "maria@example.com"
        }
    ]
}
```

## Conclusão

As funções `json_build_object` e `json_agg` são ferramentas poderosas para trabalhar com dados JSON no PostgreSQL. Elas permitem criar e agregar objetos JSON diretamente em suas consultas SQL, facilitando a construção de estruturas de dados JSON complexas.

Utilize essas funções para transformar seus dados relacionais em formatos JSON apropriados para consumo em APIs, aplicativos web e outras integrações que requerem dados em formato JSON.
