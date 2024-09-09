# Tipos de rotas permitido pelo larável

Abaixo estão as principais sintaxes

## Get
```php
Route::get('/', function () {
    return view('welcome');
});
```

## Any
```php
Route::any('/', function () {
    return "Permite todo tipo de acesso http (put, delete, get, post)";
});
```

## Match
```php
Route::match(['get', 'post'], '/', function () {
    return "Permite apenas acessos definidos";
});
```

## Post
```php
Route::post('/', function () {
    return "Tipo post";
});
```
