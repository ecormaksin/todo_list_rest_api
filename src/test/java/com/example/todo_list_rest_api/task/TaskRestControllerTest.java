package com.example.todo_list_rest_api.task;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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

	@Before
	public void setUp() {
		taskRepository.deleteAll();
		RestAssured.port = port;
	}
	
	@Test
	public void test検索全件() throws Exception {
		SearchTargetTasks searchTargetTasks = saveSearchTargetTasks();
		List<Task> expectedTasks = searchTargetTasks.getAllList();
		
		Response response = get("/api/tasks")
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("numberOfElements", is(16))
				.extract().response();

		compareList(expectedTasks, response);
	}

	@Test
	public void test検索絞り込み() throws Exception {
		SearchTargetTasks searchTargetTasks = saveSearchTargetTasks();
		List<Task> expectedTasks = searchTargetTasks.getIncludedList();
		
		String keyword = URLEncoder.encode("あ", "UTF-8");
		
		Response response = get("/api/tasks?keyword=" + keyword)
				.then()
					.statusCode(HttpStatus.OK.value())
					.body("numberOfElements", is(15))
					.extract().response();

		compareList(expectedTasks, response);
	}
	
	private SearchTargetTasks saveSearchTargetTasks() {
		SearchTargetTasks searchTargetTasks = new SearchTargetTasks();
		taskRepository.saveAll(searchTargetTasks.getAllList());
		return searchTargetTasks;
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
	public void test登録() throws Exception {
		Task task = new Task(null, "追加テストタイトル", "追加テスト内容");
		
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
	public void test更新() throws Exception {
		Task task = new Task(null, "更新テストタイトル", "更新テスト内容");
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
}
