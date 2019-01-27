package com.example.todo_list_rest_api.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Integer> {

	@Query("SELECT x FROM Task x ORDER BY x.title, x.content")
	Page<Task> findAllOrderByTitle(Pageable pageable);
	
	// https://stackoverflow.com/questions/21456494/spring-jpa-query-with-like
	@Query("SELECT x FROM Task x WHERE x.title LIKE CONCAT('%',:keyword,'%') OR x.content LIKE CONCAT('%',:keyword,'%') ORDER BY x.title, x.content")
	Page<Task> findByKeyword(Pageable pageable, @Param("keyword") String keyword);
}
