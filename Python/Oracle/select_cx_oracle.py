import cx_Oracle
import os
import utilidades as ut
import oracledb

# Método para executar uma consulta SELECT
def execute_select_query(query):
    caminho_config = os.path.abspath(os.path.join(os.path.dirname(__file__), 'config.properties'))
    username = ut.obter_valor_propriedade(caminho_config, 'ORACLE', 'usuario')
    password = ut.obter_valor_propriedade(caminho_config, 'ORACLE', 'senha')
    dsn = ut.obter_valor_propriedade(caminho_config, 'ORACLE', 'dsn')
    
    # Criar a conexão
    connection = cx_Oracle.connect(username, password, dsn)
    print("Conectado ao Oracle Database:", connection.version)

    try:
        cursor = connection.cursor()
        cursor.execute(query)
        colunas = [desc[0] for desc in cursor.description]
        valores = cursor.fetchall()
        cursor.close()
        return colunas, valores
    except cx_Oracle.DatabaseError as e:
        print(f"Erro ao executar SELECT: {e}")
        return None, None
    finally:
        # Fechar a conexão
        connection.close()
        print("Conexão fechada.")