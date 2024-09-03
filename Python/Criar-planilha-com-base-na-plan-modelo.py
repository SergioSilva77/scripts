import os
import pandas as pd
from openpyxl import load_workbook
import threading

def create_excel_from_model_inventario(model_path, path_inventario, sheet_name_contact_list, sheet_name_inventario, path_contact_list, output_dir):
    # Ler a planilha de dados usando pandas
    df_inventario = pd.read_excel(path_inventario, sheet_name=sheet_name_inventario, engine='openpyxl', header=7)
    df_contact_list = pd.read_excel(path_contact_list, sheet_name=sheet_name_contact_list, engine='openpyxl', header=0)   

   # Verificar se cada valor da coluna G (índice 6) do df_inventario está na coluna A (índice 0) do df_contact_list
    for _, row in df_inventario.iterrows():
        nome_proveedor = row.iloc[6]

        # Verificar se o e-mail do fornecedor em df_inventario está presente em df_contact_list
        if pd.isna(nome_proveedor):
            continue
        elif nome_proveedor not in df_contact_list.iloc[:, 0].values:
            dados_relatorio.append({
                'Customer': nome_proveedor,
                'TipoDocumento': 'Inventário',
                'DocumentoEnviado': '',
                'EmailEnviadoPara': '',
                'Status': 'Erro',
                'Descricao': f"E-mail do fornecedor {nome_proveedor} inválido!"
            })
        else:
            matched_rows_with_X = df_contact_list[(df_contact_list.iloc[:, 0] == nome_proveedor) & (df_contact_list.iloc[:, 14].str.lower() == 'x')]
            values_from_column_G = matched_rows_with_X.iloc[:, 6].tolist()

            if not(len(values_from_column_G) >= 0):
                dados_relatorio.append({
                    'Customer': nome_proveedor,
                    'TipoDocumento': 'Inventário',
                    'DocumentoEnviado': '',
                    'EmailEnviadoPara': '',
                    'Status': 'Erro',
                    'Descricao': f"E-mail do fornecedor {nome_proveedor} sem marcação 'X' em 'Inventory'!"
                })
    
    threads = []

    for provider, group in df_inventario.groupby('PROVEEDOR'):
        # Adquire o semaphore (se já houver 3 threads em execução, isso irá bloquear até que uma delas termine)
        thread_limiter.acquire()
        
        thread = threading.Thread(target=process_provider, args=(provider, group, 'Inventory'))
        thread.start()
        threads.append(thread)

    # Aguarda todas as threads terminarem
    for thread in threads:
        thread.join()