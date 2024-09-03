## Instalação dos pacotes
```bash
npm i json-server uuid react-icons react-scripts web-vitals react-dom react-native-sqlite-storage
```

## json-server:

Inserir no package.json
```json
"backend": "json-server --watch src/api/db.json --port 5000",
```

Rodar com `npm run backend`

## Quando houver conflito de dependencias:
npm install --legacy-peer-deps