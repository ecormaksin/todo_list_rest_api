package com.example.todo_list_rest_api.task.exception;

public class SameIdTaskExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public SameIdTaskExistsException(String message) {
        super(message);
	}
}
