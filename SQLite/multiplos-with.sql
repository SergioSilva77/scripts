-- cada cte with e uma coluna que vai ser gerada no ultimo select

with 
t1 as (select 'sergio '),
t2 as (select 'souza '),
t3 as (select 'da '),
t4 as (select 'silva')
select * from t1, t2, t3, t4