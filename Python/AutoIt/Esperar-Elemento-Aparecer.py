import autoit
import time

def aguardar_elemento_autoit(window_title, control, attempts, delay):
    while attempts > 0:
        try:
            if autoit.control_command(window_title, control, "IsEnabled") == "1":
                return True
        except:
            pass

        time.sleep(delay)  # converte 'delay' de milissegundos para segundos
        attempts -= 1
    return False