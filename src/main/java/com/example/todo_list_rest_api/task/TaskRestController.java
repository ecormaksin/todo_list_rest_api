package com.example.todo_list_rest_api.task;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
	ResponseEntity<?> getTask(@PathVariable Integer id) {
		Optional<Task> task = taskService.getById(id);
		if (task.isPresent()) return new ResponseEntity<>(task, HttpStatus.OK);
		return taskNotFoundResponseEntity(id);
	}
	
	@PostMapping
	ResponseEntity<?> postTask(@RequestBody Task task, UriComponentsBuilder uriBuilder) {
		
		Task created = null;
		HttpHeaders headers = null;
		
		try {
			created = taskService.create(task);
			URI location = uriBuilder.path("api/tasks/{id}")
					.buildAndExpand(created.getId()).toUri();
			headers = new HttpHeaders();
			headers.setLocation(location);
		} catch (SameTaskExistsException e) {
			return new ResponseEntity<>(new ResponseBody(e.getMessage(), task), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "{id}")
	ResponseEntity<?> putTask(@PathVariable Integer id, @RequestBody Task task) {
		Task updated = null;
		try {
			task.setId(id);
			updated = taskService.update(task);
		} catch (SameTaskExistsException e) {
			return new ResponseEntity<>(new ResponseBody(e.getMessage(), task), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "{id}")
	ResponseEntity<String> deleteTask(@PathVariable Integer id) {
		try {
			taskService.delete(id);
		} catch (EmptyResultDataAccessException e) {
			return taskNotFoundResponseEntity(id);
		}
		return new ResponseEntity<>("Task with id '" + id + "' is deleted.", HttpStatus.NO_CONTENT);
	}
	
	private ResponseEntity<String> taskNotFoundResponseEntity(Integer id) {
		return new ResponseEntity<>("Task with id '" + id + "' does not exist.", HttpStatus.NOT_FOUND);
	}
}
