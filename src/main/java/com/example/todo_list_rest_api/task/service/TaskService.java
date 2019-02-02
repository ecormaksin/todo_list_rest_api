package com.example.todo_list_rest_api.task.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.todo_list_rest_api.task.domain.Task;
import com.example.todo_list_rest_api.task.exception.SameTaskExistsException;
import com.example.todo_list_rest_api.task.repository.TaskRepository;

@Service
@Transactional
public class TaskService {

	@Autowired
	TaskRepository taskRepository;
	
	public Page<Task> findAll(Pageable pageable) {
		return taskRepository.findAllOrderByTitle(pageable);
	}
	
	public Page<Task> findByKeyword(Pageable pageable, String keyword) {
		return taskRepository.findByKeyword(pageable, keyword);
	}
	
	public Optional<Task> getById(Integer id) {
		return taskRepository.findById(id);
	}
	
	public Task create(Task task) throws SameTaskExistsException {
		checkSameTask(task);
		return taskRepository.save(task);
	}

	public Task update(Task task) throws SameTaskExistsException {
		checkSameTask(task);
		return taskRepository.save(task);
	}
	
	private void checkSameTask(Task task) throws SameTaskExistsException {
		Task searched = taskRepository.findByAllFields(task.getTitle(), task.getDetail());
		if (null == searched) return;
		throw new SameTaskExistsException("Same task '" + searched.toString() + "' already exists.");
	}
	
	public void delete(Integer id) {
		taskRepository.deleteById(id);
	}
}
