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
	public void 検索全件() throws Exception {
		SearchTargetTasks searchTargetTasks = new SearchTargetTasks();
		List<Task> expectedTasks = searchTargetTasks.getAllList();
		taskRepository.saveAll(expectedTasks);
		
		Response response = get("/api/tasks")
			.then()
				.statusCode(HttpStatus.OK.value())
				.body("numberOfElements", is(16))
				.extract().response();

		compareList(expectedTasks, response);
	}

	@Test
	public void 検索絞り込み() throws Exception {
		SearchTargetTasks searchTargetTasks = new SearchTargetTasks();
		List<Task> allTasks = searchTargetTasks.getAllList();
		taskRepository.saveAll(allTasks);
		List<Task> expectedTasks = searchTargetTasks.getIncludedList();
		
		String keyword = URLEncoder.encode("あ", "UTF-8");
		
		Response response = get("/api/tasks?keyword=" + keyword)
				.then()
					.statusCode(HttpStatus.OK.value())
					.body("numberOfElements", is(15))
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
}
