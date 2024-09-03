import tensorflow as tf
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Flatten, Conv2D, MaxPooling2D


# Criar o diretório de dados
data_dir = r'C:\temp\t'

# Criar os conjuntos de dados de treinamento e teste
train_data = tf.keras.preprocessing.image_dataset_from_directory(
    data_dir,
    validation_split=0.2,
    seed=42,
    shuffle=True,
    image_size=(256, 256),
    subset='training'
)

test_data = tf.keras.preprocessing.image_dataset_from_directory(
    data_dir,
    validation_split=0.2,
    seed=42,
    shuffle=True,
    image_size=(256, 256),
    subset='validation'
)

# Criar um gerador de imagens para pré-processar as imagens
data_generator = ImageDataGenerator(
    rescale=1./255,
    rotation_range=40,
    width_shift_range=0.2,
    height_shift_range=0.2,
    horizontal_flip=True,
    vertical_flip=True
)

train_data_gen = data_generator.flow_from_directory(
    data_dir,
    target_size=(256, 256),
    batch_size=32,
    class_mode='categorical',
    subset='training'
)

print(train_data.class_names)

test_data_gen = data_generator.flow_from_directory(
    data_dir,
    target_size=(256, 256),
    batch_size=32,
    class_mode='categorical',
    subset='validation'
)


# Pré-processar as imagens
# train_data = data_generator.flow(train_data)
# test_data = data_generator.flow(test_data)

# Criar um modelo de rede neural
model = Sequential([
    Conv2D(16, (3, 3), padding='same', activation='relu', input_shape=(256, 256, 3)),
    MaxPooling2D((2, 2)),
    Conv2D(32, (3, 3), padding='same', activation='relu'),
    MaxPooling2D((2, 2)),
    Flatten(),
    Dense(128, activation='relu'),
    Dense(3, activation='softmax')
])

# Compilar o modelo
model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

# Treinar o modelo
# model.fit(train_data, epochs=10)
model.fit(train_data_gen, epochs=10)

model.save('meu_modelo_salvo.h5')


