package com.example.todo_list_rest_api.task;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/tasks")
public class TaskRestController {

	@Autowired
	TaskService taskService;

	@GetMapping
	Page<Task> getTasks(@PageableDefault(size=20) Pageable pageable
			, @RequestParam(required=false) String keyword) throws UnsupportedEncodingException {
		if (null == keyword) return taskService.findAll(pageable);
		String decodedKeyword = URLDecoder.decode(keyword, "UTF-8");;
		return taskService.findByKeyword(pageable, decodedKeyword);
	}
	
	@GetMapping(value = "{id}")
	Task getTask(@PathVariable Integer id) {
		return taskService.getOne(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	ResponseEntity<Task> postTask(@RequestBody Task task, UriComponentsBuilder uriBuilder) {
		Task created = taskService.create(task);
		URI location = uriBuilder.path("api/tasks/{id}")
				.buildAndExpand(created.getId()).toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(location);
		return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
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
