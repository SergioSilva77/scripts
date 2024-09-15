# Criando com nome

Quando utilizamos `create_produtos_table` significa que estamos falando para o larável para criar um migrate com o nome `produtos`. Esse é o esqueleto `create_<produtos>_table`

```cmd
php artisan make:migration create_produtos_table
```

Se caso não quisermos usar a nomenclatura acima então podemos fazer da seguinte forma abaixo

```cmd
php artisan make:migration produtos --create=produtos
```