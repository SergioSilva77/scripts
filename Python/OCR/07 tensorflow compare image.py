import cv2
import numpy as np
import os
import matplotlib.pyplot as plt
from tensorflow.keras import Sequential
from tensorflow.keras.layers import Conv2D
from tensorflow.keras.layers import Flatten
from tensorflow.keras.layers import Dense
from tensorflow.keras.layers import MaxPool2D
from tensorflow.keras.optimizers import RMSprop
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.preprocessing import image

img = image.load_img('train/train/rgs/05.PNG')

train = ImageDataGenerator(rescale=1/255)
validation = ImageDataGenerator(rescale=1/255)

train_dataset = train.flow_from_directory('train/train/',
                                          target_size=(200,200),
                                          batch_size=3,
                                          class_mode='binary')

val_dataset = train.flow_from_directory('train/validation/',
                                          target_size=(200,200),
                                          batch_size=3,
                                          class_mode='binary')

model = Sequential([Conv2D(16,(3,3),activation='relu', input_shape = (200,200,3)),
                                                   MaxPool2D(2,2),
                                                   #
                                                   Conv2D(32,(3,3),activation='relu'),
                                                   MaxPool2D(2,2),
                                                   #
                                                   Conv2D(64, (3, 3), activation='relu'),
                                                   MaxPool2D(2, 2),
                                                   #
                                                   Flatten(),
                                                   #
                                                   Dense(512,activation='relu'),
                                                   #
                                                   Dense(1,activation='sigmoid')
                                                   ])

model.compile(loss='binary_crossentropy',
              optimizer=RMSprop(lr=0.001),
              metrics=['accuracy']
              )

model_fit = model.fit(train_dataset,
                      steps_per_epoch=3,
                      epochs=30,
                      validation_data=val_dataset)

dir_path = 'train/test'
for i in os.listdir(dir_path):
    img = image.load_img(dir_path +'//'+ i,target_size=(200,200))
    plt.imshow(img)
    x = image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    images = np.vstack([x])
    val = model.predict((images))
    print(val)


# print(train_dataset.class_indices)
# print(train_dataset.classes)