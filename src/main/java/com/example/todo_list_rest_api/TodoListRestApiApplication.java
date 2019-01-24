package com.example.todo_list_rest_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TodoListRestApiApplication implements CommandLineRunner {

	@Autowired
	TaskRepository taskRepository;
	
	@Override
	public void run(String... strings) {
		Task task = taskRepository.save(new Task(null, "タイトルテスト", "内容テスト"));
		System.out.println(task + " is created!");
		taskRepository.findAll()
			.forEach(System.out::println);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TodoListRestApiApplication.class, args);
	}

}

