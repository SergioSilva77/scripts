import os
import oracledb as oracle
import utilidades as ut

# Método para executar um comando UPDATE
def execute_update_query(query):
    caminho_config = os.path.abspath(os.path.join(os.path.dirname(__file__), 'config.properties'))
    username = ut.obter_valor_propriedade(caminho_config, 'ORACLE', 'usuario')
    password = ut.obter_valor_propriedade(caminho_config, 'ORACLE', 'senha')
    dsn = ut.obter_valor_propriedade(caminho_config, 'ORACLE', 'dsn')
    
    # Criar a conexão
    try:
        connection = oracle.connect(user=username, password=password, dsn=dsn)
        print("Conectado ao Oracle Database:", connection.version)

        cursor = connection.cursor()
        cursor.execute(query)
        connection.commit()
        cursor.close()
        print("UPDATE executado com sucesso.")
    except oracle.DatabaseError as e:
        print(f"Erro ao executar UPDATE: {e}")
        connection.rollback()
    finally:
        # Fechar a conexão
        connection.close()
        print("Conexão fechada.")

