CREATE TABLE tabela_aleatoria (
    id SERIAL PRIMARY KEY,    -- Coluna de ID gerada automaticamente
    nome VARCHAR(50),         -- Coluna de nome com um limite de 50 caracteres
    idade INT,                -- Coluna de idade
    data_nascimento DATE,     -- Coluna de data de nascimento
    salario NUMERIC(10, 2)    -- Coluna de salário com 10 dígitos, incluindo 2 decimais
);


INSERT INTO tabela_aleatoria (id, nome, idade, data_nascimento, salario)
VALUES (1, 'João Silva', 30, '1993-05-10', 4500.00)
ON CONFLICT (id) DO UPDATE 
SET nome = EXCLUDED.nome,
    idade = EXCLUDED.idade,
    data_nascimento = EXCLUDED.data_nascimento,
    salario = EXCLUDED.salario;


select * from tabela_aleatoria;