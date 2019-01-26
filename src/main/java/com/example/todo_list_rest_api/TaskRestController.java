package com.example.todo_list_rest_api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tasks")
public class TaskRestController {

	@Autowired
	TaskService taskService;

	@GetMapping
	List<Task> getTasks(@RequestParam(required=false) String keyword) {
		if (null == keyword) return taskService.findAll();
		return taskService.findByKeyword(keyword);
	}
	
	@GetMapping(value = "{id}")
	Task getTask(@PathVariable Integer id) {
		return taskService.getOne(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	Task postTask(@RequestBody Task task) {
		return taskService.create(task);
	}
	
	@PutMapping(value = "{id}")
	Task putTask(@PathVariable Integer id, @RequestBody Task task) {
		task.setId(id);
		return taskService.update(task);
	}
	
	@DeleteMapping(value = "{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteTask(@PathVariable Integer id) {
		taskService.delete(id);
	}
}
