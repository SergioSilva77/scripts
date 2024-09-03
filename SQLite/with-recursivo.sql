-- (x,y) significa que sao x e y sao colunas que terao valor inicial igual a 1 e 2
-- abaixo do union all eu vou pegar essas mesmas colunas e ir incrementando os valores ja existentes

WITH b(x,y) AS 
(
    SELECT 1,2  --variavel com os numeros iniciais
    UNION ALL 
    SELECT x + 1, y + 1  -- vou incrementar as variaveis (x,y)
    FROM b 
    WHERE x < 20 -- o limite vai até 20
) SELECT * FROM b;