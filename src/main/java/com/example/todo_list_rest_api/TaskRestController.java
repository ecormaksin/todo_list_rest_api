package com.example.todo_list_rest_api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tasks")
public class TaskRestController {

	@Autowired
	TaskService taskService;
	
	@GetMapping
	List<Task> getTasks() {
		return taskService.findAll();
	}
	
	@GetMapping(value = "{id}")
	Optional<Task> getTask(@PathVariable Integer id) {
		return taskService.findOne(id);
	}
}
