import tensorflow as tf

model = tf.keras.models.load_model("model.h5")

# Cria a pasta logs
if not os.path.exists("logs"):
  os.mkdir("logs")

# Inicializa o escritor de logs
writer = tf.keras.callbacks.TensorBoard(log_dir="logs")

# Treina o modelo
model.fit(x_train, y_train, epochs=10, callbacks=[writer])