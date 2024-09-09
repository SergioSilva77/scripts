# Criar rota e personalizar o HTML da view


## Criando uma view simples

1. Acesse a pasta ..\resources\views

2. Crie um arquivo com um nome de preferência e que termine com `.blade.php`

3. Acesse a pasta `...\routes` e clique no arquivo `web.php`

4. Crie uma rota dentro desse arquivo, por exemplo

``` php
Route::get('/cadastro', function () {
    return view('cadastro');
});
```

5. Repare que retorna uma view `return view('cadastro');`

6. Para isso vc precisou ter criado uma view `cadastro.blade.php` antes


## Organizar views em pastas
1. Antes temos que criar a view `resources\views\aluno\cadastro`

2. E depois acessar essa pasta:

``` php
Route::get('/cadastro', function () {
    return view('aluno/cadastro');
});
```