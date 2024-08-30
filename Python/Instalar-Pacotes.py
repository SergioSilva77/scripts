import os
import subprocess

# Caminho para o Python embutido
embedded_python_path = os.path.join(os.path.dirname(__file__), 'Python', 'python.exe')

# Verificar se o Python embutido está presente
if os.path.exists(embedded_python_path): 
    python_executable = embedded_python_path
    print("Usando Python embutido.")
else:
    python_executable = 'python'
    print("Python embutido não encontrado, usando Python do sistema.")

# Lista de bibliotecas a serem instaladas
libraries = [
    'virtualenv'
]

# Instalar as bibliotecas
for library in libraries:
    subprocess.run([python_executable, '-m', 'pip', 'install', library])
    print(f'{library} installed successfully.')

# Criar ambiente virtual
print('---------------- CRIANDO AMBIENTE VIRTUAL ----------------')
subprocess.run([python_executable, '-m', 'virtualenv', 'venv'])