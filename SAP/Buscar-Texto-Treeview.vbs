Function SelectNodeByText(tree, searchText)
    Set coll = tree.GetAllNodeKeys()
    For i = 0 to coll.Length - 1
        nodekey = coll.ElementAt(i)
msgbox nodekey
        If tree.GetNodeTextByKey(nodekey) = searchText Then
            Set treeElement = session.findById("wnd[0]/usr/cntlSINWP_CONTAINER/shellcont/shell/shellcont[0]/shell")
            treeElement.selectedNode = nodekey
            Exit For
        End If
    Next
End Function
Set tree = session.findById("wnd[0]/usr/cntlSINWP_CONTAINER/shellcont/shell/shellcont[0]/shell")
'Texto
SelectNodeByText tree, "Agrupado por conteúdo"