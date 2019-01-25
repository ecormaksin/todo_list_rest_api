package com.example.todo_list_rest_api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {

	@Autowired
	TaskRepository taskRepository;
	
	public List<Task> findAll() {
		return taskRepository.findAllOrderByTitle();
	}
	
	public Optional<Task> findOne(Integer id) {
		return taskRepository.findById(id);
	}
	
	public Task create(Task task) {
		return taskRepository.save(task);
	}

	public Task update(Task task) {
		return taskRepository.save(task);
	}
	
	public void delete(Integer id) {
		taskRepository.deleteById(id);
	}
}
