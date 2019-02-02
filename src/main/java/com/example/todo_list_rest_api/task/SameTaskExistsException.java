package com.example.todo_list_rest_api.task;

public class SameTaskExistsException extends Exception {

	private static final long serialVersionUID = 1L;

	public SameTaskExistsException(String message) {
        super(message);
	}
}
