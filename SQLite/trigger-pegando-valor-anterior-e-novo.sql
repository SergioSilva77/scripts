--OLD = versao anterior
--NEW = valor novo

create trigger if not exists trigger_pessoas_apagadas 
before delete on pessoas for each row begin insert into pessoas_apagadas values (null, OLD.pessoas_nome)

-- estou criando um trigger "trigger_pessoas_apagadas"
-- e antes de deletar a tabela pessoas vou percorrer cada linha
-- cada linha percorrida da tabela "pessoas" vou inserir na tabela "pessoas_apagadas"
-- como estou deletando então eu vou usar o OLD porque não faz sentido usar o NEW, porque não estou atualizando ou inserindo, estou removendo