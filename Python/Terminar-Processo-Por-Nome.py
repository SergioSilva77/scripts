import psutil


def terminate_process_by_name(process_name):
    try:
        for proc in psutil.process_iter(['name']):
            if proc.info['name'].startswith(process_name):
                proc.terminate()  # Fecha o processo
                proc.wait()  # Aguarda até que o processo seja finalizado
                print(f"Processo {proc.info['name']} finalizado.")
    except Exception as e:
        print(f"Ocorreu um erro: {e}")