- 通常起動
```
java -jar todo_list_rest_api.jar
```

- Swagger-UI用起動
```
java -jar todo_list_rest_api.jar --spring.profiles.active=swagger
```

- 起動直後
```
curl http://localhost:8080/api/tasks -v
```
- タイトルがnullでエラー

```
curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": null, \"detail\": \"内容テスト\"}"
```

- タイトルが長さ0でエラー

```
curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": \"\", \"detail\": \"内容テスト\"}"
```

- タイトルがスペースでエラー

```
curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": \" \", \"detail\": \"内容テスト\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": \"　\", \"detail\": \"内容テスト\"}"
```

- 正常系データの追加
```
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルな\",\"detail\":\"内容は\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルと\",\"detail\":\"内容ひ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルて\",\"detail\":\"内容ふ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルつ\",\"detail\":\"内容へ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルち\",\"detail\":\"内容ほ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルた\",\"detail\":\"内容ま\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルそ\",\"detail\":\"内容み\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルせ\",\"detail\":\"内容む\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルす\",\"detail\":\"内容め\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルし\",\"detail\":\"内容も\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルさ\",\"detail\":\"内容ら\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルこ\",\"detail\":\"内容り\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルけ\",\"detail\":\"内容る\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルく\",\"detail\":\"内容れ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルき\",\"detail\":\"内容ろ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルか\",\"detail\":\"内容や\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルお\",\"detail\":\"内容ゆ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルえ\",\"detail\":\"内容よ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルう\",\"detail\":\"内容わ\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルい\",\"detail\":\"内容を\"}"
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルあ\",\"detail\":\"内容ん\"}"
```

- 既存のidで追加エラー
```
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"id\": 23,\"title\":\"タイトル追加テスト\",\"detail\":\"内容追加テスト\"}"
```

- 同じタイトル・内容のタスクありでエラー
```
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"タイトルあ\",\"detail\":\"内容ん\"}"
```

- 特定idを指定して取得
```
curl "http://localhost:8080/api/tasks/3" -v -X GET
```

- 更新
```
curl "http://localhost:8080/api/tasks/3" -v -X PUT -H "Content-Type: application/json" -d "{\"title\": \"タイトルな変更テスト\", \"detail\": \"内容は変更テスト\"}"
```

- 更新時タイトルバリデーションエラー
```
curl "http://localhost:8080/api/tasks/3" -v -X PUT -H "Content-Type: application/json" -d "{\"title\": null, \"detail\": \"内容は変更テスト\"}"
curl "http://localhost:8080/api/tasks/3" -v -X PUT -H "Content-Type: application/json" -d "{\"title\": \"\", \"detail\": \"内容は変更テスト\"}"
curl "http://localhost:8080/api/tasks/3" -v -X PUT -H "Content-Type: application/json" -d "{\"title\": \" \", \"detail\": \"内容は変更テスト\"}"
curl "http://localhost:8080/api/tasks/3" -v -X PUT -H "Content-Type: application/json" -d "{\"title\": \"　\", \"detail\": \"内容は変更テスト\"}"
```

- 更新時タスク重複でエラー
```
curl "http://localhost:8080/api/tasks/3" -v -X PUT -H "Content-Type: application/json" -d "{\"title\": \"タイトルあ\", \"detail\": \"内容ん\"}"
```

- 削除

```
curl http://localhost:8080/api/tasks -v -X POST -H "content-type:application/json" -d "{\"title\":\"あ\",\"detail\":\"い\"}"
curl "http://localhost:8080/api/tasks/24" -v -X DELETE
```
