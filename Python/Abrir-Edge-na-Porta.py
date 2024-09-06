import subprocess


def abrir_edge_com_porta(porta, url):
    """
    Abre o Microsoft Edge com a depuração remota ativada em uma porta específica e acessa a URL fornecida.
    
    :param porta: Porta onde a depuração remota será ativada.
    :param url: URL que será aberta no Edge.
    """
    comando = ['start', 'msedge', f'--remote-debugging-port={porta}', url]
    subprocess.run(comando, shell=True)