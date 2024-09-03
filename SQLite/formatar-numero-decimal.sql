--para fazer a soma do numero abaixo precisa estar sem ponto e virgula
--se sua coluna estiver com ponto ou virgula, considere tirar fazendo replace(coluna, string, novastring)
--e após o replace faça a formatação como estava antes

-- arquivo formata
with teste as (
    select '2222' as valor union all
    select '2222' as valor
) select printf('%,d.%02d',sum(replace(replace(teste.valor, '.',''),',',''))) from teste