# [VBscript] Buscar texto em uma Grid

Buscar texto

```python
'===============================INICIO==================================
'                        procura por nome dentro da lista ao clicar em buscar variante no menu
'=====================================================================

'SETAR A GRID GERAL PADRÃO '
Set GridView = session.findById("wnd[1]/usr/cntlALV_CONTAINER_1/shellcont/shell")
session.findById("wnd[1]/usr/cntlALV_CONTAINER_1/shellcont/shell").setFocus
'Percorrer grid '
For j = 0 To GridView.RowCount - 1
        variante = GridView.GetCellValue(j, GridView.ColumnOrder(0))

'buscar texto'
		If variante = "texto" Then
			session.findById("wnd[1]/usr/cntlALV_CONTAINER_1/shellcont/shell").currentCellRow = j
			session.findById("wnd[1]/usr/cntlALV_CONTAINER_1/shellcont/shell").selectedRows = j	
		End If
Next 
```

Identificar nó

```python
Function TreeNodeExistsByKey(tree, nodekey)
    On Error Resume Next
    tree.GetHierarchyLevel nodekey
    If Err.Number = 0 Then
        result = True
    Else
        result = False
    End If
    TreeNodeExistsByKey = result
End Function
 
Set tree = session.findById("wnd[0]/usr/cntlSINWP_CONTAINER/shellcont/shell/shellcont[0]/shell")
Set coll = tree.GetAllNodeKeys()
 
For i = 0 to coll.Length - 1
    nodekey = coll.ElementAt(i)
    InodeText = tree.GetNodeTextByKey(nodekey)
    MsgBox InodeText
Next
```