Route::prefix('admin1')->group(function(){
    Route::get('/dashboard', function() {
        return 'dashboard';
    });

    Route::get('/users', function() {
        return 'users';
    });

    Route::get('/clientes', function() {
        return 'clientes';
    });

    Route::get('/novidades', function() {
        return 'noticias';
    })->name('admin.noticias');
}); 


Route::name('admin2.')->group(function(){
    Route::get('admin2/dashboard', function() {
        return 'dashboard';
    });

    Route::get('admin2/users', function() {
        return 'users';
    });

    Route::get('admin2/clientes', function() {
        return 'clientes';
    });

    Route::get('admin2/novidades', function() {
        return 'noticias';
    })->name('noticias');
}); 

Route::group([
    'prefix'=>'admin3',
    'as'=>'admin.'
], function(){
    Route::get('dashboard', function() {
        return 'dashboard';
    });

    Route::get('users', function() {
        return 'users';
    });

    Route::get('clientes', function() {
        return 'clientes';
    });

    Route::get('novidades', function() {
        return 'noticias';
    })->name('admin.noticias');
});
