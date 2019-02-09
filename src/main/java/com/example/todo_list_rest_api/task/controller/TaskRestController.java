package com.example.todo_list_rest_api.task.controller;

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

import com.example.todo_list_rest_api.task.domain.Task;
import com.example.todo_list_rest_api.task.exception.SameIdTaskExistsException;
import com.example.todo_list_rest_api.task.exception.SamePropertiesTaskExistsException;
import com.example.todo_list_rest_api.task.exception.TaskNotFoundException;
import com.example.todo_list_rest_api.task.service.TaskService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

	@Autowired
	TaskService taskService;

	@GetMapping
	@ApiOperation(
			value = "複数のタスクを取得します。"
			, notes = "キーワードによる絞り込みが可能です。</br>パラメーターの指定がない場合はタイトルの昇順、内容の昇順で並べ替えた先頭の20件を取得します。"
			)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "size", value = "取得する件数です。（初期値は20）", required = false, dataType = "int", paramType = "query")
	    , @ApiImplicitParam(name = "page", value = "ページネーションの考え方で○ページ目を取得するかを指定します。</br>取得したいページー1の数値を指定します。(初期値は0＝1ページ目)", required = false, dataType = "int", paramType = "query")
	    })
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Page.class, message = "")
	})
	Page<Task> getTasks(@PageableDefault(size=20) Pageable pageable
			, @ApiParam(
					name = "keyword"
					, value = "検索時のキーワードです。</br>指定した場合はタイトル・内容のいずれかに部分一致するタスクに絞り込まれます。</br>curlなどコマンドから実行する場合はURLエンコードした文字列を渡す必要があります。"
					, required = false)
			@RequestParam(required=false) String keyword) throws UnsupportedEncodingException {
		if (null == keyword) return taskService.findAll(pageable);
		String decodedKeyword = URLDecoder.decode(keyword, "UTF-8");
		return taskService.findByKeyword(pageable, decodedKeyword);
	}
	
	@GetMapping(value = "{id}")
	@ApiOperation(
			value = "パスに指定したIDのタスクを取得します。"
			, notes = "存在した場合は該当のタスクを返却します。存在しない場合はエラーメッセージを返却します。"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Task.class, message = "")
			, @ApiResponse(code = 404, response = ResponseBodyOnlyMessage.class, message = "Task with id({id}) does not exist.")
	})
	ResponseEntity<?> getTask(@ApiParam(value = "タスクID", required = true) @PathVariable Integer id) {
		Task task;
		try {
			task = taskService.getById(id);
			return new ResponseEntity<>(task, HttpStatus.OK);
		} catch (TaskNotFoundException e) {
			return taskNotFoundResponseEntity(e);
		}
	}
	
	@PostMapping
	@ApiOperation(
			value = "タスクを新規登録します。"
			, notes = "IDが同じ、またはタイトルと内容が同じタスクが既に登録されている場合はエラーメッセージを返却します。"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Task.class, message = "")
			, @ApiResponse(code = 409, response = ResponseBodyWithTask.class, message = "Task with same [id | properties](id: {id}, title: {title}, detail: {detail}) already exists.")
	})
	ResponseEntity<?> postTask(@ApiParam(value = "タスク", required = true) @RequestBody Task task, UriComponentsBuilder uriBuilder) {
		
		Task created = null;
		HttpHeaders headers = null;
		
		try {
			created = taskService.create(task);
			URI location = uriBuilder.path("api/tasks/{id}")
					.buildAndExpand(created.getId()).toUri();
			headers = new HttpHeaders();
			headers.setLocation(location);
		} catch (SameIdTaskExistsException 
				| SamePropertiesTaskExistsException e) {
			return new ResponseEntity<>(new ResponseBodyWithTask(e.getMessage(), task), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(created, headers, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "{id}")
	@ApiOperation(
			value = "指定したIDのタスクを更新します。"
			, notes = "タスクが存在しない、またはタイトルと内容が同じタスクが既に登録されている場合はエラーメッセージを返却します。"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Task.class, message = "")
			, @ApiResponse(code = 404, response = ResponseBodyOnlyMessage.class, message = "Task with id({id}) does not exist.")
			, @ApiResponse(code = 409, response = ResponseBodyWithTask.class, message = "Task with same properties(id: {id}, title: {title}, detail: {detail}) already exists.")
	})
	ResponseEntity<?> putTask(@ApiParam(value = "タスクID", required = true) @PathVariable Integer id
			, @ApiParam(value = "タスク", required = true) @RequestBody Task task) {
		Task updated = null;
		try {
			task.setId(id);
			updated = taskService.update(task);
		} catch (TaskNotFoundException e) {
			return taskNotFoundResponseEntity(e);
		} catch (SamePropertiesTaskExistsException e) {
			return new ResponseEntity<>(new ResponseBodyWithTask(e.getMessage(), task), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "{id}")
	@ApiOperation(
			value = "パスに指定したIDのタスクを削除します。"
			, notes = "存在した場合は該当のタスクを削除します。存在しない場合はエラーメッセージを返却します。"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 204, response = String.class, message = "")
			, @ApiResponse(code = 404, response = ResponseBodyOnlyMessage.class, message = "Task with id({id}) does not exist.")
	})
	@ResponseStatus(HttpStatus.NO_CONTENT) // https://github.com/springfox/springfox/issues/908
	ResponseEntity<?> deleteTask(@ApiParam(value = "タスクID", required = true) @PathVariable Integer id) {
		try {
			taskService.delete(id);
		} catch (TaskNotFoundException e) {
			return taskNotFoundResponseEntity(e);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	private ResponseEntity<ResponseBodyOnlyMessage> taskNotFoundResponseEntity(TaskNotFoundException e) {
		return new ResponseEntity<>(new ResponseBodyOnlyMessage(e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
