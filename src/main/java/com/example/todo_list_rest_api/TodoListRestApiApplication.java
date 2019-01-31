package com.example.todo_list_rest_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class TodoListRestApiApplication implements CommandLineRunner {

	@Override
	public void run(String... strings) {
		System.out.println("起動");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TodoListRestApiApplication.class, args);
	}

}

