--versao 1 ================================================

-- dias uteis para tras
select min(data_dia_util) from (select * from dias_uteis 
where data_dia_util <= date('now', '-1 day', 'localtime') 
and observacao = 'dia util' order by data_dia_util desc limit 88) dados;

-- dias uteis para frete
select max(data_dia_util) from (select * from dias_uteis 
where data_dia_util >= date('now', '+1 day', 'localtime') 
and observacao = 'DIA UTIL' order by data_dia_util asc limit 3);

--versao 2 ================================================
with datas(data) as (
    select date('now', 'localtime') as dt union all
    select date(data, '-1 day') from datas where exists(select * from dias_uteis where observacao = 'dia util' and date(data_dia_util,'+1 day') = data)
), dia_util_anterior as (select min(datas.data) as data from datas)
select min(dados.data_dia_util) as data from (select * from dia_util_anterior a, dias_uteis b 
where b.data_dia_util <= a.data and a.data and b.observacao = 'dia util' order by data_dia_util desc limit 8) dados;

with datas(data) as (
    select date('now', '+1 day', 'localtime') as dt union all
    select date(data, '+1 day') from datas where exists(select * from dias_uteis where observacao = 'dia util' and date(data_dia_util,'-1 day') = data)
), dia_util_anterior as (select min(datas.data) as data from datas)
select max(dados.data_dia_util) from (select * from dia_util_anterior a, dias_uteis b 
where b.data_dia_util >= a.data and a.data and b.observacao = 'dia util' order by data_dia_util asc limit 2) dados;