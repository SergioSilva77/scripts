
## Exemplo 1
```
Route::get('/news', function() {
    return view('news');
})->name('noticias');
```

## Exemplo 2
```
Route::view('/apocalipse','apocalipse')->name('revelacao');
```

## Redirecionando para uma rota nomeada
```
Route::get('/novidades', function() {
    return redirect()->route('noticias');
});
```