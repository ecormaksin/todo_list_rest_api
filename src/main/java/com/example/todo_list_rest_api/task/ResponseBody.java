package com.example.todo_list_rest_api.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBody {
	private String message;
	private Task task;
}
