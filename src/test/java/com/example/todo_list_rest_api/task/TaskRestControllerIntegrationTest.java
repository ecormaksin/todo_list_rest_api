package com.example.todo_list_rest_api.task;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.jayway.restassured.RestAssured;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = TodoListRestApiApplication.class
		, webEnvironment=WebEnvironment.RANDOM_PORT
)
public class TaskRestControllerIntegrationTest {

	@Autowired
	TaskRepository taskRepository;

	@Value("${local.server.port}")
	int port;
	
	Map<Task, Task> searchAllTaskMap = new TreeMap<>();
	Map<Task, Task> searchIncludedTaskMap = new TreeMap<>();
	Map<Task, Task> searchExcludedTaskMap = new TreeMap<>();
	
	{
		setSearchData("あタイトル1", "内容1", true);
		setSearchData("あタイトル2", "内容2あ", true);
		setSearchData("あタイトル3", "内あ容3", true);
		setSearchData("あタイトル4", "あ内容4", true);
		setSearchData("タイあトル5", "内容5", true);
		setSearchData("タイあトル6", "内容6あ", true);
		setSearchData("タイあトル7", "あ内容7", true);
		setSearchData("タイあトル8", "内あ容8", true);
		setSearchData("タイトル9", "あ内容9", true);
		setSearchData("タイトル10あ", "内容10あ", true);
		setSearchData("タイトル11", "内容11", false);
		setSearchData("タイトル12あ", "あ内容12", true);
		setSearchData("タイトル13", "内あ容13", true);
		setSearchData("タイトル14あ", "内あ容14", true);
		setSearchData("タイトル15", "内容15あ", true);
		setSearchData("タイトル16あ", "内容16", true);
		
		searchAllTaskMap.putAll(searchIncludedTaskMap);
		searchAllTaskMap.putAll(searchExcludedTaskMap);
	}
	
	private void setSearchData(String title, String detail, boolean isSearchIncluded) {
		Task task = new Task(null, title, detail);
		Map<Task, Task> target = isSearchIncluded ? searchIncludedTaskMap : searchExcludedTaskMap;
		target.put(task, task);
	}
	
	@Before
	public void setUp() {
		taskRepository.deleteAll();
		RestAssured.port = port;
	}
	
	@Test
	public void testGetTasks() throws Exception {
		List<Task> expected = new ArrayList<Task>(searchAllTaskMap.values());
		taskRepository.saveAll(expected);
		
		when().get("/api/tasks")
			.then()
			.statusCode(HttpStatus.OK.value())
			.body("numberOfElements", is(16));
			;
	}
}
