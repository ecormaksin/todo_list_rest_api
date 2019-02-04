docker run -d -h pg_todo_list01 --name=pg_todo_list01 --privileged -p 55432:5432 postgres:11.1

create user todolist;

alter role postgres with password 'reality7776';

create user todolist;
alter role todolist with password 'todolist';

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

curl http://localhost:8080/api/tasks/4 -v -X DELETE


## テストケース

| No. | title | detail |
|:----:|----|----|
| 1 | 後方一致 | 前方一致 |
| 2 | 一致なし | 中央一致 |
| 3 | 後方一致 | 中央一致 |
| 4 | 前方一致 | 中央一致 |
| 5 | 中央一致 | 前方一致 |
| 6 | 一致なし | 後方一致 |
| 7 | 前方一致 | 前方一致 |
| 8 | 中央一致 | 中央一致 |
| 9 | 後方一致 | 一致なし |
| 10 | 前方一致 | 一致なし |
| 11 | 中央一致 | 一致なし |
| 12 | 一致なし | 前方一致 |
| 13 | 後方一致 | 後方一致 |
| 14 | 前方一致 | 後方一致 |
| 15 | 中央一致 | 後方一致 |
| 16 | 一致なし | 一致なし |
