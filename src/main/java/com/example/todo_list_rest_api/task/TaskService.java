package com.example.todo_list_rest_api.task;

import java.util.List;

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
	
	public List<Task> findByKeyword(String keyword) {
		return taskRepository.findByKeyword(keyword);
	}
	
	public Task getOne(Integer id) {
		return taskRepository.getOne(id);
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
