# Obter valor passado

## Exemplo 1
```
Route::get('/produto/{id}', function ($id) {
    return "O id do produto é ".$id;
});
```

- Exemplo de chamada:  http://127.0.0.1:8000/produto/123

- Retorno: **O id do produto é 123**

## Exemplo 2
```
Route::get('/produto/{id}/{cat}', function ($id, $cat) {
    return "O id do produto é ".$id.'<br>A categoria é: '.$cat;
});
```

- Exemplo de chamada:  http://127.0.0.1:8000/produto/123/limpeza

- Retorno: 
	- O id do produto é 123
	- A categoria é: limpeza**
	

## Exemplo 3 - Deixar um parametro opcional

1. Abaixo foi colocado `{cat?}` com ponto de interrogação e `$cat=''` para deixar em branco o valor padrão
```
Route::get('/produto/{id}/{cat?}', function ($id, $cat='') {
    return "O id do produto é ".$id.'<br>A categoria é: '.$cat;
});
```

2. Agora posso chamar a url assim
- http://127.0.0.1:8000/produto/123