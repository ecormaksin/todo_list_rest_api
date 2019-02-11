package com.example.todo_list_rest_api.task.exception;

public class SameIdTaskExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SameIdTaskExistsException(String message) {
        super(message);
	}
}
