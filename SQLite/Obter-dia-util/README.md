# Encontrar o dia util

Para que funcione, execute a query antes [Criar tabelas necessarias](Criar-Tabelas.sql)

## Encontrar o dia útil anterior

Para encontrar o dia útil anterior, é necessário alterar o `limit` da query. Se caso vc quiser igualar o dia com o dia atual, tire a opção `'-1 day'`

``` sql
-- dias uteis para tras, altere o limit para retroceder
select min(data) from (select * from dias_uteis 
where data <= date('now', '-1 day', 'localtime') 
and descricao = 'Dia Util' order by data desc limit 1)
```

## Encontrar o dia útil posterior

Para encontrar o dia útil posterior, é necessário alterar o `limit` da query. Se caso vc quiser igualar o dia com o dia atual, tire a opção `'+1 day'`

``` sql
-- dias uteis para frente, altere o limit para avançar a frente
select max(data) from (select * from dias_uteis 
where data >= date('now', '+1 day', 'localtime') 
and descricao = 'Dia Util' order by data asc limit 1);
```
