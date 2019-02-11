package com.example.todo_list_rest_api.task.exception;

public class SamePropertiesTaskExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SamePropertiesTaskExistsException(String message) {
        super(message);
	}
}
