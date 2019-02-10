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


- ToDo（タスク）は次の要素（項目）を保持します。

    - タイトル（必須、255文字以内）
    - 内容（任意、文字数制限は1GB(4バイト文字だと268,435,456文字)以内）

## 検索

- ID指定による1件の検索（取得）とキーワード指定による複数件の検索が行えます。

- ID指定による1件の検索

    - 該当するものがない場合はエラーのレスポンスが返ってきます。

- キーワード指定による複数件の検索

    - キーワードがタイトルまたは内容に含まれるものが表示対象となります。
    - 


https://qiita.com/kimullaa/items/70eaec61c02d2513e76c

docker build -t pg-todo-list .

docker run -it -e "TZ=Asia/Tokyo" -e POSTGRES_PASSWORD=secret -h pg-todo-list01 --name=pg-todo-list01 --privileged -p 5432:5432 -v $(pwd)/sql:/docker-entrypoint-initdb.d pg-todo-list

docker run -d -e "TZ=Asia/Tokyo" -e POSTGRES_PASSWORD=secret -h pg-todo-list01 --name=pg-todo-list01 --privileged -p 5432:5432 postgres:11


localedef -i ja_JP -c -f UTF-8 -A /usr/share/locale/locale.alias ja_JP.UTF-8

psql -U postgres　

create user todolist PASSWORD 'todolist';
create database todolist OWNER todolist ENCODING 'UTF8' LC_COLLATE 'ja_JP.utf8' LC_CTYPE 'ja_JP.utf8';
create database todolist-swagger OWNER todolist ENCODING 'UTF8' LC_COLLATE 'ja_JP.utf8' LC_CTYPE 'ja_JP.utf8';

create database todolist;
create user todolist;
alter role todolist with password 'todolist';
GRANT ALL ON database todolist TO todolist;

create database todolist_swagger;
GRANT ALL ON database todolist_swagger TO todolist;

curl http://localhost:8080/api/tasks -v -X GET
curl "http://localhost:8080/api/tasks?keyword=1" -v -X GET
#※↑でダブルクォーテーションで囲まないと「zsh: no matches found: http://localhost:8080/api/tasks/search?keyword=1」というエラーになった。

curl "http://localhost:8080/api/tasks/7" -v -X GET
#※↑でダブルクォーテーションで囲まないと以下のようなエラーになった。
{"timestamp":"2019-02-02T10:03:16.513+0000","status":500,"error":"Internal Server Error","message":"Could not write JSON: could not initialize proxy [com.example.todo_list_rest_api.task.Task#7] - no Session; nested exception is com.fasterxml.jackson.databind.JsonMappingException: could not initialize proxy [com.example.todo_list_rest_api.task.Task#7] - no Session (through reference chain: com.example.todo_list_rest_api.task.Task_$$_jvstbb5_0[\"title\"])","path":"/api/tasks/7"}%

curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": \"タイトル追加テスト\", \"detail\": \"内容追加テスト\"}"

curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": null, \"detail\": null}"

curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": \"タイトル追加テスト\", \"detail\": null}"

curl http://localhost:8080/api/tasks -v -X POST -H "Content-Type: application/json" -d "{\"title\": null, \"detail\": \"内容追加テスト\"}}"

curl http://localhost:8080/api/tasks/4 -v -X PUT -H "Content-Type: application/json" -d "{\"title\": \"タイトル変更テスト\", \"detail\": \"内容変更テスト\"}"

curl "http://localhost:8080/api/tasks/7" -v -X DELETE

