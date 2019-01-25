package com.example.todo_list_rest_api;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	@Query("SELECT x FROM Task x ORDER BY x.title, x.content")
	List<Task> findAllOrderByTitle();
}
