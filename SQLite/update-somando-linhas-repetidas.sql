with duplicados as (
    select coluna_duplicada, sum(valor) as soma from sua_tabela group by coluna_duplicada having count(coluna_duplicada) > 1
) update sua_tabela set valor = duplicados.soma from duplicados where duplicados.coluna_duplicada = sua_tabela.coluna_duplicada;