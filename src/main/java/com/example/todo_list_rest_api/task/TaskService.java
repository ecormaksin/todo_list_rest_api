package com.example.todo_list_rest_api.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public Task getOne(Integer id) {
		return taskRepository.getOne(id);
	}
	
	public Task create(Task task) throws SameTaskExistsException {
		Task searched = taskRepository.findByAllFields(task.getTitle(), task.getDetail());
		if (null != searched) {
			throw new SameTaskExistsException("Same task '" + searched.toString() + "' already exists.");
		}
		return taskRepository.save(task);
	}

	public Task update(Task task) {
		return taskRepository.save(task);
	}
	
	public void delete(Integer id) {
		taskRepository.deleteById(id);
	}
}
