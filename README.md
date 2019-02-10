# 概要

- ToDoリストを操作する REST APIです。

- 検索、登録、更新、削除の操作が行えます。

# 使い方

- データをDB（PostgreSQL）へ保存するため、DockerでDB用のコンテナを作成します。
- [プロジェクト内のdockerフォルダ](/docker) へ移動して以下のコマンドを実行します。

```
docker build -t pg-todo-list .
docker run -it -e "TZ=Asia/Tokyo" -e POSTGRES_PASSWORD=secret -h pg-todo-list01 --name=pg-todo-list01 --privileged -p 5432:5432 -v $(pwd)/sql:/docker-entrypoint-initdb.d pg-todo-list
```
- DB用コンテナを起動した後は、[プロジェクト内の「com.example.todo_list_rest_api.TodoListRestApiApplication.main(String[])」](/src/main/java/com/example/todo_list_rest_api/TodoListRestApiApplication.java)か、Maven packageで生成した「todo_list_rest_api.jar」を実行します。

- プロジェクトまたはjar実行後に「http://localhost:8080/swagger-ui.html 」へアクセスすると、[SpringFox](http://springfox.github.io/springfox/)で生成されたSwagger-UIで、APIの仕様と動作が確認できます。起動時にMavenプロファイル「swagger」を指定（-Dspring.profiles.active=swagger）すると、実際の動作環境とは別のDBへ100件分のサンプルデータを格納した状態でAPIの動作を確認できます。

- APIの仕様については上記の「http://localhost:8080/swagger-ui.html 」へアクセスいただくか、オフラインでは[プロジェクト内にbootprint-swaggerで作成したドキュメント](/doc/bootprint-swagger/index.html)をご参照ください。

# テスト

## テストコード

- REST APIのテストは[com.example.todo_list_rest_api.task.controller.TaskRestControllerTest](/src/test/java/com/example/todo_list_rest_api/task/controller/TaskRestControllerTest.java)です。

- ToDoタスクを保持するクラス(Task)のテストは[com.example.todo_list_rest_api.task.domain.TaskTest](/src/test/java/com/example/todo_list_rest_api/task/domain/TaskTest.java)です。

## REST APIの検索機能に関するテストケース
- Microsoft社が開発したペア・ワイズ法によるテストケース抽出ツール[PICT](https://github.com/Microsoft/pict/blob/master/doc/pict.md)を使用しました。

- PICTへ受け渡したパターンのパラメーターは以下の通りです。
```
タイトル: 前方一致, 中央一致, 後方一致, 一致なし
内容: 前方一致, 中央一致, 後方一致, 一致なし
```

- PICTを実行して生成されたテストパターン表は以下の通りです。キーワードを指定した場合にNo.11以外が抽出されることをテストしています。

| No. | タイトル | 内容 |検索対象 | 
|:---:|:---:|:---:|:---:|
| 1 | 前方一致 | 一致なし |○ |
| 2 | 前方一致 | 後方一致 |○ |
| 3 | 前方一致 | 中央一致 |○ |
| 4 | 前方一致 | 前方一致 |○ |
| 5 | 中央一致 | 一致なし |○ |
| 6 | 中央一致 | 後方一致 |○ |
| 7 | 中央一致 | 前方一致 |○ |
| 8 | 中央一致 | 中央一致 |○ |
| 9 | 一致なし | 前方一致 |○ |
| 10 | 後方一致 | 後方一致 |○ |
| 11 | 一致なし | 一致なし |✕ |
| 12 | 後方一致 | 前方一致 |○ |
| 13 | 一致なし | 中央一致 |○ |
| 14 | 後方一致 | 中央一致 |○ |
| 15 | 一致なし | 後方一致 |○ |
| 16 | 後方一致 | 一致なし |○ |

## テストデータ
- ユニットテストおよびswagger-ui用のデータを[test_data.xlsx](/doc/test_data.xlsx)で作成しています。
