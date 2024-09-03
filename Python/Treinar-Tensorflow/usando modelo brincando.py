from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
import numpy as np

# Carregar imagem
img_path = r'C:\temp\t\rg.jpg'
img = image.load_img(img_path, target_size=(256, 256))

# Converter para array e fazer rescaling
img_array = image.img_to_array(img)
img_array = img_array / 255.0

# Expandir dimensões para se adaptar ao formato de entrada do modelo (batch_size, height, width, channels)
img_batch = np.expand_dims(img_array, axis=0)


model = load_model('meu_modelo_salvo.h5')
predictions = model.predict(img_batch)
predicted_class = np.argmax(predictions[0])

# Presumindo que seus diretórios foram lidos em ordem alfabética:
class_names = ['comprovante_residencia', 'cpf', 'rg']
predicted_class_name = class_names[predicted_class]
print(f"A imagem é provavelmente um(a) {predicted_class_name}.")


for idx, class_name in enumerate(class_names):
    probability = predictions[0][idx] * 100
    print(f"A imagem tem uma probabilidade de {probability:.2f}% de ser um(a) {class_name}.")
