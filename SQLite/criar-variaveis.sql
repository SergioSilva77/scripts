--------------------- BATIMENTO DOS TOTAIS ENTRE SITEC E NOTA
with
corretora_atual as (
    select 'bradesco' as corretora
), total_nota as (
    select sum(ntc.quantidade) as total_nota from nota_de_corretagem ntc, corretora_atual ca where ntc.corretora = ca.corretora
),
total_sitec as (
    select stc.corretora, sum(quantidade) as total_sitec from dados_sitec stc, corretora_atual ca where "tipo do negócio" in ('Normal', 'Day-trade') and stc.corretora = ca.corretora
),
total_bateu as (
    select case when ts.total_sitec = tn.total_nota then 'Totais bateram' else 'Totais não bateram' end as total from total_sitec ts, total_nota tn, corretora_atual ca where ts.corretora = ca.corretora
),
geral as (
    select stc.corretora, stc."ajuste de posição"||'='||ntc."ajuste de posição" as compare_total_ajuste_posicao,
        (case when stc."ajuste de posição" = ntc."ajuste de posição" then 'bateu' else 'não bateu' end) as ajuste_posicao,
        stc."total operações"||'='||ntc."valor dos negócios" as compare_total_valor_negocio,
        (case when stc."total operações" = ntc."valor dos negócios" then 'bateu' else 'não bateu' end) as valor_negocio,
        stc."ajuste day trade"||'='||ntc."ajuste day trade" as compare_total_ajuste_day_trade,
        (case when stc."ajuste day trade" = ntc."ajuste day trade" then case when stc."ajuste day trade" = 0 and  ntc."ajuste day trade" = 0 then 'zerado' else 'bateu' end else 'não bateu' end) as ajuste_day_trade,
        tb.total
        from dados_sitec_rodape stc, corretora_atual ca
        inner join nota_de_corretagem_rodape ntc on stc.corretora = ntc.corretora and stc.corretora = ca.corretora
        left join total_bateu tb
) select * from geral;

--------------------- BATIMENTO DOS TOTAIS ENTRE SITEC E NOTA
with 
corretora_atual as (
    select 'bradesco' as corretora
), variaveis as (
    select * from (
    -- NC1, NC2, RB3, NC3, NC4, NC5, NC6, NC7, NC8 
    (select sum(ntc.quantidade) as NC1 from nota_de_corretagem ntc, corretora_atual ca where ntc.corretora = ca.corretora),
    (select ntcR.[valor dos negócios] as NC2 from nota_de_corretagem_rodape ntcR, corretora_atual ca where ntcR.corretora = ca.corretora),
    (select ntcR.[ajuste de posição] as NC3 from nota_de_corretagem_rodape ntcR, corretora_atual ca where ntcR.corretora = ca.corretora),
    (select ntcR.[ajuste day trade] as NC4 from nota_de_corretagem_rodape ntcR, corretora_atual ca where ntcR.corretora = ca.corretora),
    (select (sum(ntcR.[taxa corretora intermediação]) + sum(ntcR.[taxa corretora itaú])) as NC5 from nota_de_corretagem_rodape ntcR, corretora_atual ca where ntcR.corretora = ca.corretora),
    (select ntcR.[taxa registro bm&f] as NC6 from nota_de_corretagem_rodape ntcR, corretora_atual ca where ntcR.corretora = ca.corretora),
    (select (ntcR.[taxas bm&f] - stcR.[taxa de liquidação]) as NC7 from nota_de_corretagem_rodape ntcR, dados_sitec_rodape stcR, corretora_atual ca where ntcR.corretora = stcR.corretora and ntcR.corretora = ca.corretora),
    (select ntcR.[taxas bm&f] as NC8 from nota_de_corretagem_rodape ntcR, corretora_atual ca where ntcR.corretora = ca.corretora),
    -- RB1, RB2, RB3, RB4, RB5, RB4FRCDDI, RB41, RB5FRCDDI 
    (select (sum(b3.[débito])) as RB1 from relatoriob3 b3, corretora_atual ca where desclanc in ('Taxa Registro', 'Tx Registro') and b3.corretora = ca.corretora),
    (select (sum(b3.[débito])) as RB2 from relatoriob3 b3, corretora_atual ca where desclanc in ('Emolumentos N', 'Emolumentos D')and b3.corretora = ca.corretora),
    (select (sum(b3.[débito])) as RB3 from relatoriob3 b3, corretora_atual ca where desclanc in ('Tx de Perman') and b3.corretora = ca.corretora),
    (select (sum(b3.[débito])) as RB4 from relatoriob3 b3, corretora_atual ca where desclanc in ('Taxa Registro', 'Tx Registro') and b3.codmerc = 'DI1' and b3.corretora = ca.corretora),
    (select (sum(b3.[débito])) as RB5 from relatoriob3 b3, corretora_atual ca where desclanc in ('Emolumentos N', 'Emolumentos D') and b3.codmerc = 'DI1' and b3.corretora = ca.corretora),
    (select (sum(b3.[débito])) as RB4FRCDDI from relatoriob3 b3, corretora_atual ca where desclanc in ('Taxa Registro', 'Tx Registro') and b3.codmerc in ('FRC','DDI') and b3.corretora = ca.corretora),
    (select (sum(b3.[débito])) as RB41 from relatoriob3 b3, corretora_atual ca where desclanc in ('Taxa Registro', 'Tx Registro') and b3.codmerc not in ('FRC', 'DI1','DDI') and b3.corretora = ca.corretora),
    (select (sum(b3.[débito])) as RB5FRCDDI from relatoriob3 b3, corretora_atual ca where desclanc in ('Taxa Registro', 'Tx Registro') and b3.codmerc in ('FRC','DDI') and b3.corretora = ca.corretora),
    (select sum(debito) RB51 from (select b3.corretora, sum(b3.[débito]) as debito from relatoriob3 b3, corretora_atual ca where desclanc in ('Emolumentos N', 'Emolumentos D') and b3.codmerc not in ('FRC', 'DI1','DDI') and b3.corretora = ca.corretora)),
    -- TS5, TS6, TS7, TS8, TS82, TS81, OUTRAS
    (select stcR.[taxa operacional] as TS5 from dados_sitec_rodape stcR, corretora_atual ca where stcR.corretora = ca.corretora),
    (select stcR.[Taxa Registro BM&F] as TS6 from dados_sitec_rodape stcR, corretora_atual ca where stcR.corretora = ca.corretora),
    (select stcR.[Taxa BM&F] as TS7 from dados_sitec_rodape stcR, corretora_atual ca where stcR.corretora = ca.corretora),
    (select sum(stc.quantidade) as TS8 from dados_sitec stc, corretora_atual ca where mercadoria1 = 'DI1' and [tipo do negócio] in ('Normal','Day-trade') and stc.corretora = ca.corretora),
    (select sum(stc.quantidade) as TS82 from dados_sitec stc, corretora_atual ca where mercadoria1 = 'DDI' and [tipo do negócio] in ('Normal','Day-trade') and stc.corretora = ca.corretora),
    (select sum(stc.quantidade) as TS81 from dados_sitec stc, corretora_atual ca where mercadoria1 = 'DOL' and [tipo do negócio] in ('Normal','Day-trade') and stc.corretora = ca.corretora),
    (select [taxa de liquidação] as TS9 from dados_sitec_rodape stc, corretora_atual ca where stc.corretora = ca.corretora),
    (select sum(stc.quantidade) as OUTRAS from dados_sitec stc, corretora_atual ca where mercadoria1 not in ('DI1','DDI','DOL', 'DR1','FRC') and [tipo do negócio] in ('Normal','Day-trade') and stc.corretora = ca.corretora),
    -- countSitec 
    (select count(stc.quantidade) * 0.01 as countSitec from dados_sitec stc, corretora_atual ca where stc.corretora = ca.corretora))
), calculo_unitario as ( -- valores unitarios
    select *, 
    (rb4 / ts8) as ValUnTxRegMercDI1,
    (rb4frcddi / ts82) as ValUnTxRegMercDDI,
    (rb41 / ts81) as ValUnTxRegMercDOL,
    (rb5 / ts8) as ValUnTxBMFMercDI1,
    (rb5frcddi / ts82) as ValUnTxBMFMercDDI,
    (rb51 / ts81) as ValUnTxBMFMercDOL    
     from variaveis
), validar_nota_sitec as (
    select countSitec as countSitec,
           ts5||' = '||nc5 as compare_ts5_nc5,
           case when ts5 = nc5 then 'bateu' else 'ts5 com nc5 não bateu' end as 'ts5=nc5',
           ts6||' = '||nc6 as compare_ts6_nc6,
           case when ts6 = nc6 then 'bateu' when ts6-nc6 <= countSitec then 'bateu com diferença' else 'não bateu' end as 'ts6=nc6',
           ts7||' = '||nc7 as compare_ts7_nc7,
           case when ts7 = nc7 then 'bateu' else 'nao bateu' end as 'ts7=nc7-ts9' from variaveis
), dados4 as ( -- multiplicando pelos valores unitarios
    select (case when stc.mercadoria1 = 'DI1' then stc.quantidade * calcUnit.ValUnTxRegMercDI1 
                 when stc.mercadoria1 = 'DDI' then stc.quantidade * calcUnit.ValUnTxRegMercDDI
                 when stc.mercadoria1 = 'DOL' then round(stc.quantidade * calcUnit.ValUnTxRegMercDOL,2) else 0 end) as txregistro, 
   (case when stc.mercadoria1 = 'DI1' then round(stc.quantidade * calcUnit.ValUnTxbmfMercDI1,2)
                 when stc.mercadoria1 = 'DDI' then stc.quantidade * calcUnit.ValUnTxbmfMercDDI
                 when stc.mercadoria1 = 'DOL' then round(stc.quantidade * calcUnit.ValUnTxbmfMercDOL,2) else 0 end) as txbmf, stc.*
    from dados_sitec stc, calculo_unitario calcUnit, corretora_atual ca
    where trim(stc.[tipo do negócio]) not like '%liq%' and stc.[tipo do negócio] in ('Normal', 'Day-trade')
    and stc.mercadoria1 not in ('DR1','FRC')
    and stc.corretora = ca.corretora
    order by cast(stc.linha as integer)
) --select round(txregistro,2) as txregistro, round(txbmf,2) as txbmf from dados4;
select * from validar_nota_sitec;


--------------------- SE NAO BATEU, 
with corretora_atual as (
    select 'bradesco' as corretora
), obter_diferencas as (
    SELECT qry.mercadoria, 
    qry.tipo_negocio, 
    qry.total_itens_nota, 
    qry.soma_itens_nota, 
    qry.total_itens_sitec, 
    qry.soma_itens_sitec,
    (qry.total_itens_nota - qry.total_itens_sitec) diferenca_itens, 
    printf("%.2f", (qry.soma_itens_nota - qry.soma_itens_sitec)) diferenca_notas,
    (SELECT min(stc.linha) FROM dados_sitec stc, corretora_atual ca WHERE stc.mercadoria2 = qry.mercadoria AND stc.[tipo do negócio] = qry.tipo_negocio and stc.corretora = ca.corretora) primeira_linha,
    (SELECT [taxa operacional] FROM dados_sitec stc, corretora_atual ca WHERE stc.mercadoria2 = qry.mercadoria AND stc.[tipo do negócio] = qry.tipo_negocio and stc.corretora = ca.corretora) taxa_op_linha_sitec
    
    FROM (
    
        SELECT ntc.mercadoria, 
        ntc.tipo_negocio1 as tipo_negocio, 
        printf("%.2f", sum(ntc.quantidade)) total_itens_nota, 
        printf("%.2f", sum(ntc.taxa_operacional)) soma_itens_nota,    
        (SELECT printf("%.2f", sum(stc.quantidade)) FROM dados_sitec stc, corretora_atual ca WHERE stc.mercadoria = ntc.mercadoria AND stc.[tipo do negócio] = ntc.tipo_negocio and stc.corretora = ca.corretora) total_itens_sitec,
        
        (SELECT printf("%.2f", sum(stc.[taxa operacional])) FROM dados_sitec stc, corretora_atual ca WHERE stc.mercadoria = ntc.mercadoria AND stc.[tipo do negócio] = ntc.tipo_negocio and stc.corretora = ca.corretora) soma_itens_sitec
        
        FROM NOTA_DE_CORRETAGEM ntc, corretora_atual ca where ntc.corretora = ca.corretora
        
        GROUP BY ntc.mercadoria, ntc.tipo_negocio
    
    ) qry
), final as (
    select taxa_op_linha_sitec as linha, diferenca_notas+taxa_op_linha_sitec as taxa_op_linha_sitec_att from obter_diferencas
) select * from obter_diferencas;

---------------------  GERANDO JSON 
select json_group_array((json_object('corretora', corretora, 'cv', [C/V], 'meracdo', Mercado, 'mercadoria', Mercadoria, 'mercadoria1', Mercadoria1, 'mercadoria2', Mercadoria2, 'cod_isin', [Cod. ISIN], 'vencimento', Vencimento, 'quantidade', Quantidade, 'preco_ajuste', [Preço/Ajuste], 'tipo_ajuste', [Tipo do Negócio], 'dc', [D/C], 'valor_operacao_ajuste', [Valor Operação/Ajuste], 'taxa_operacional', [Taxa Operacional], 'taxa_registro_bmf', [Taxa Registro BM&F], 'taxa_bmf', [Taxa BM&F], 'taxa_liquidacao', [Taxa de Liquidação], 'linha', linha))) from dados_sitec;
select json_group_array((json_object('corretora', corretora, 'cv', [C/V], 'mercadoria', mercadoria, 'mercadoria1', Mercadoria1, 'mercadoria2', Mercadoria2, 'quantidade', quantidade, 'preco', preco, 'tipo_negocio', tipo_negocio, 'tipo_negocio1', tipo_negocio1, 'valor_operacao', valor_operacao, 'dc', dc, 'taxa_operacional', taxa_operacional, 'vencimento', vencimento))) from nota_de_corretagem;
select json_group_array((json_object('corretora', corretora, 'venda_disponivel', [Venda Disponível], 'compra_disponivel', [Compra Disponível], 'venda_opcoes', [Venda opçoes], 'compra_opcoes', [Compra opçoes], 'valor_negocios', [Valor dos negócios], 'irrf', IRRF, 'taxa_corretora_intermediacao', [Taxa Corretora Intermediação], 'taxa_corretora_itau', [Taxa Corretora Itaú], 'taxa_registro_bmf', [Taxa registro BM&F], 'taxas_bmf', [Taxas BM&F], 'irrf_day_trade', [IRRF Day Trade], 'outros_custos', [Outros Custos], 'iss', [I.S.S], 'ajuste_posicao', [Ajuste de posição], 'ajuste_day_trade', [Ajuste day trade], 'total_despesas', [Total das despesas], 'outros', Outros, 'irrf_corretagem', [IRRF Corretagem], 'total_conta_investimento', [Total Conta Investimento], 'total_conta_normal', [Total Conta Normal], 'total_liquido', [Total líquido], 'total_liquido_nota', [Total Líquido da nota]))) from nota_de_corretagem_rodape;
select json_group_array((json_object('corretora', corretora, 'total_operacoes', [Total Operações], 'taxa_registro_bmf', [Taxa Registro BM&F], 'ajuste_posicao', [Ajuste de Posição], 'total_despesas', [Total das Despesas], 'taxa_permanencia', [Taxa Permanência], 'imposto', Imposto, 'taxa_operacional', [Taxa Operacional], 'taxa_bmf', [Taxa BM&F], 'ajuste_day_trade', [Ajuste Day Trade], 'taxa_liquidacao', [Taxa de Liquidação], 'total_liquido_nota', [Total Líquido da Nota], 'imposto_calculado', [Imposto Calculado]))) from dados_sitec_rodape;
select json_group_array((json_object('corretora', corretora, 'cod_cliente', CodCliente, 'desc_cliente', DescCliente, 'cod_lanc', CodLanç, 'desc_lanc', DescLanc, 'cod_merc', CodMerc, 'desc_merc', DescMerc, 'debito', Débito, 'credito', Crédito, 'saldo', Saldo, 'dc', DC))) from relatoriob3;




CREATE TABLE dados_sitec_rodape (
    corretora               TEXT,
    [Total Operações]       TEXT,
    [Taxa Registro BM&F]    TEXT,
    [Ajuste de Posição]     TEXT,
    [Total das Despesas]    TEXT,
    [Taxa Permanência]      TEXT,
    Imposto                 TEXT,
    [Taxa Operacional]      TEXT,
    [Taxa BM&F]             TEXT,
    [Ajuste Day Trade]      TEXT,
    [Taxa de Liquidação]    TEXT,
    [Total Líquido da Nota] TEXT,
    [Imposto Calculado]     TEXT
);
