package com.example.todo_list_rest_api.task.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo_list_rest_api.task.domain.Task;
import com.example.todo_list_rest_api.task.exception.SameIdTaskExistsException;
import com.example.todo_list_rest_api.task.exception.SamePropertiesTaskExistsException;
import com.example.todo_list_rest_api.task.exception.TaskNotFoundException;
import com.example.todo_list_rest_api.task.repository.TaskRepository;

@Service
@Transactional(readOnly = true)
public class TaskService {

	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	MessageService messageService;

	public Page<Task> findAll(Pageable pageable) {
		return taskRepository.findAllOrderByTitle(pageable);
	}
	
	public Page<Task> findByKeyword(Pageable pageable, String keyword) {
		return taskRepository.findByKeyword(pageable, keyword);
	}
	
	public Task getById(Integer id) throws TaskNotFoundException {
		Optional<Task> searched = null;
		if (null != id) searched = taskRepository.findById(id);
		if (null != searched && searched.isPresent()) return searched.get();
		throw new TaskNotFoundException(messageService.get("error.task.not.exist", new Object[] {id}));
	}
	
	@Transactional(readOnly = false)
	public Task create(Task task) throws SameIdTaskExistsException, SamePropertiesTaskExistsException {
		Integer id = task.getId();
		Task searched = null;
		try {
			searched = getById(id);
		} catch (TaskNotFoundException e) {
			return saveTask(task);
		}
		throw new SameIdTaskExistsException(messageService.get("error.task.same.id.exists", new Object[] {searched.toString()}));
	}
	
	@Transactional(readOnly = false)
	public Task update(Task task) throws TaskNotFoundException, SamePropertiesTaskExistsException {
		getById(task.getId());
		return saveTask(task);
	}
	
	private Task saveTask(Task task) throws SamePropertiesTaskExistsException {
		checkSamePropertiesTask(task);
		return taskRepository.save(task);
	}
	
	private void checkSamePropertiesTask(Task task) throws SamePropertiesTaskExistsException {
		Task searched = taskRepository.findByAllFields(task.getTitle(), task.getDetail());
		if (null == searched) return;
		throw new SamePropertiesTaskExistsException(messageService.get("error.task.same.properties.exists", new Object[] {searched.toString()}));
	}
	
	@Transactional(readOnly = false)
	public void delete(Integer id) throws TaskNotFoundException {
		getById(id);
		taskRepository.deleteById(id);
	}
}
