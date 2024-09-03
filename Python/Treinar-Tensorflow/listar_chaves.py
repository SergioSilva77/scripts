import h5py

def list_all_keys(h5_file, prefix=""):
    keys = []
    for key in h5_file.keys():
        current_key = f"{prefix}/{key}"
        keys.append(current_key)
        if isinstance(h5_file[key], h5py.Group):
            keys.extend(list_all_keys(h5_file[key], current_key))
    return keys

filename = "meu_modelo_salvo.h5"

with h5py.File(filename, 'r') as f:
    all_keys = list_all_keys(f)
    for key in all_keys:
        print(key)
