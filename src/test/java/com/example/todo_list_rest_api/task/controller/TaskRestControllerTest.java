package com.example.todo_list_rest_api.task.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.todo_list_rest_api.TodoListRestApiApplication;
import com.example.todo_list_rest_api.task.domain.Task;
import com.example.todo_list_rest_api.task.repository.TaskRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = TodoListRestApiApplication.class
		, webEnvironment=WebEnvironment.RANDOM_PORT
)
public class TaskRestControllerTest {

	@Autowired
	TaskRepository taskRepository;

	@Value("${local.server.port}")
	int port;
	
	SearchTargetTasks searchTargetTasks;

	enum SeachKeyword {
		NONE,
		EXISTS;
	}
	
	@Before
	public void setUp() {
		taskRepository.deleteAll();
		searchTargetTasks = new SearchTargetTasks();
		taskRepository.saveAll(searchTargetTasks.getAllList());
		RestAssured.port = port;
	}
	
	@Test
	public void 検索全件_ページネーションデフォルト20件() throws Exception {
		getWithKeyword(SeachKeyword.NONE, 16);
	}

	@Test
	public void 検索絞り込み_ページネーションデフォルト20件() throws Exception {
		getWithKeyword(SeachKeyword.EXISTS, 15);
	}
	
	private void getWithKeyword(SeachKeyword seachKeyword, int numberOfElements) throws Exception {
		List<Task> expectedTasks = searchTargetTasks.getAllList();
		String endPoint = "/api/tasks";
		
		if (SeachKeyword.EXISTS == seachKeyword) {
			expectedTasks = searchTargetTasks.getIncludedList();
			endPoint += getKeywordRequestParam("あ");
		}
		
		Response response = get(endPoint)
				.then()
					.statusCode(HttpStatus.OK.value())
					.body("numberOfElements", is(numberOfElements))
					.extract().response();

		compareList(expectedTasks, response);
	}
	
	private String getKeywordRequestParam(String keyword) throws UnsupportedEncodingException {
		return "?keyword=" + URLEncoder.encode(keyword, "UTF-8");
	}
	
	@Test
	public void 検索全件_ページネーション3件先頭() throws Exception {
		getWithPagination(0, 4, 0, 3);
	}

	@Test
	public void 検索全件_ページネーション3件末尾() throws Exception {
		getWithPagination(15, 16, 5, 1);
	}
	
	private void getWithPagination(int fromIndex, int toIndex, int page, int numberOfElements) {
		final int countPerPage = 3;
		
		List<Task> allTasks = searchTargetTasks.getAllList();
		List<Task> expectedTasks = allTasks.subList(fromIndex, toIndex);
		
		Response response = get("/api/tasks?size=" + countPerPage + "&page=" + page)
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("numberOfElements", is(numberOfElements))
				.extract().response();

		compareList(expectedTasks, response);
	}
	
	private void compareList(List<Task> expectedTasks, Response response) {
		List<Task> actualTasks = response.jsonPath().getList("content", Task.class);
		for (int i = 0; i < actualTasks.size(); i++) {
			Task expected = expectedTasks.get(i);
			Task actual = actualTasks.get(i);
			
			assertNotNull(actual.getId());
			assertEquals(expected.getTitle(), actual.getTitle());
			assertEquals(expected.getDetail(), actual.getDetail());
		}
	}
	
	@Test
	public void 検索絞り込み_0件() throws Exception {
		get("/api/tasks" + getKeywordRequestParam("い"))
				.then()
					.statusCode(HttpStatus.OK.value())
					.body("numberOfElements", is(0));
	}

	@Test
	public void 取得() throws Exception {
		List<Task> tasks = taskRepository.findAll();
		Task task = tasks.get(0);
		
		get("/api/tasks/" + task.getId())
				.then()
					.statusCode(HttpStatus.OK.value());
	}

	@Test
	public void 取得_該当なし() throws Exception {
		List<Task> tasks = taskRepository.findAll();
		Task task = tasks.get(tasks.size() - 1);
		Integer id = task.getId() + 1;

		get("/api/tasks/" + id)
				.then()
					.statusCode(HttpStatus.NOT_FOUND.value())
					.body(is("Task with id '" + id + "' does not exist."));
	}

	@Test
	public void 登録() throws Exception {
		Task task = new Task("追加テストタイトル", "追加テスト内容");
		
		given().body(task)
			.contentType(ContentType.JSON)
			.and()
			.when().post("/api/tasks")
			.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("id", is(notNullValue()))
				.body("title", is(task.getTitle()))
				.body("detail", is(task.getDetail()));
	}

	@Test
	public void 登録_同タイトル同内容がすでに登録されていた場合はエラー() throws Exception {
		Task task = new Task("追加テストタイトル", "追加テスト内容");
		taskRepository.save(task);
		
		given().body(task)
			.contentType(ContentType.JSON)
			.and()
			.when().post("/api/tasks")
			.then()
				.statusCode(HttpStatus.CONFLICT.value())
				.body("message", is("Same task '" + task.toString() + "' already exists."));
	}

	@Test
	public void 更新() throws Exception {
		Task task = new Task("更新テストタイトル", "更新テスト内容");
		Task created = taskRepository.save(task);
		created.setTitle(created.getTitle() + "あ");
		created.setDetail(created.getDetail() + "あ");
		
		given().body(created)
			.contentType(ContentType.JSON)
			.and()
			.when().put("/api/tasks/{id}", created.getId())
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("id", is(created.getId()))
				.body("title", is(created.getTitle()))
				.body("detail", is(created.getDetail()));
	}

	@Test
	public void 更新_他のタスクのタイトルと内容と同じ場合はエラー() throws Exception {
		Task firstTask = taskRepository.save(new Task("追加テストタイトル", "追加テスト内容"));
		Task secondTask = taskRepository.save(new Task("更新テストタイトル", "更新テスト内容"));
		secondTask.setTitle(firstTask.getTitle());
		secondTask.setDetail(firstTask.getDetail());
		
		given().body(secondTask)
			.contentType(ContentType.JSON)
			.and()
			.when().put("/api/tasks/{id}", secondTask.getId())
			.then()
				.statusCode(HttpStatus.CONFLICT.value())
				.body("message", is("Same task '" + firstTask.toString() + "' already exists."));
	}

	@Test
	public void 削除() throws Exception {
		Task task = new Task("削除テストタイトル", "削除テスト内容");
		Task created = taskRepository.save(task);
		
		delete("/api/tasks/{id}", created.getId())
			.then()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void 削除_削除済みのものを削除しようとした場合はエラー() throws Exception {
		Task task = new Task("削除テストタイトル", "削除テスト内容");
		Task created = taskRepository.save(task);
		Integer id = created.getId();
		taskRepository.delete(created);
		
		delete("/api/tasks/{id}", id)
			.then()
				.statusCode(HttpStatus.NOT_FOUND.value())
				.body(is("Task with id '" + id + "' does not exist."));
	}
}
