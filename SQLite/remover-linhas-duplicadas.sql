delete from sua_tabela
where rowid not in (select min(rowid) from sua_tabela group by [coluna_duplicada]);