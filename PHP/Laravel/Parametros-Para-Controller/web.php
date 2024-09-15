<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\ProdutoController;



Route::get('/', [ProdutoController::class, 'index'])->name('produto.index');
Route::get('/produto/{id?}', [ProdutoController::class, 'show'])->name('produto.show');

