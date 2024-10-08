import base64
import requests
import json

from google.cloud import language

# OpenAI API Key
api_key = "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"

# Function to encode the image
def encode_image(image_path):
    with open(image_path, "rb") as image_file:
        return base64.b64encode(image_file.read()).decode('utf-8')

# Path to your image
image_path = r"/content/a.png"

# Getting the base64 string
base64_image = encode_image(image_path)

headers = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {api_key}"
}

payload = {
    "model": "gpt-4-vision-preview",
    "messages": [
      {
        "role": "user",
        "content": [
          {
            "type": "text",
            "text": "oi"
          },
          {
            "type": "image_url",
            "image_url": {
              "url": f"data:image/jpeg;base64,{base64_image}",
              "detail": "high"
            }
          }
        ]
      }
    ],
    "max_tokens": 99999
}

response = requests.post("https://api.openai.com/v1/chat/completions", headers=headers, json=payload)