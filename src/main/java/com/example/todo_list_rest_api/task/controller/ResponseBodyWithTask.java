package com.example.todo_list_rest_api.task.controller;

import com.example.todo_list_rest_api.task.domain.Task;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBodyWithTask {
	@ApiModelProperty(value = "メッセージ", position = 1)
	private String message;
	@ApiModelProperty(value = "タスク", position = 2)
	private Task task;
}
