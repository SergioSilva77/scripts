def obter_coluna_by_title(id_table, titulo):
    qtd_col = obter_qtd_colunas(id_table)
    grid = sap.session.findById(id_table)
    for index_col in range(qtd_col):
        nome_col = grid.ColumnOrder(index_col)
        titulo_col = grid.GetDisplayedColumnTitle(nome_col)
        if (titulo_col == titulo):
            return nome_col, index_col

name_col_txt_breve, index_col_txt_breve = obter_coluna_by_title(id_table, "Texto breve material")