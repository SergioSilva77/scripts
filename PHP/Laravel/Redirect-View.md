# Redirecionamento e View

## Redirecionando para uma URL

1. Quando a pessoa entrar na pagina inicial, logo será redirecionada para a pagina '/apocalipse'.

2. Antes deve ser criada a rota '/apocalipse' abaixo, senão, irá redirecionar com erros
```
Route::get('/', function () {
    return redirect('/apocalipse');
});

Route::get('/apocalipse', function() {
    return view('apocalipse');
});
```

## Redirecionamento simples

Podemos redirecionar dessa forma
```
Route::redirect('/sobre', 'empresa');
```