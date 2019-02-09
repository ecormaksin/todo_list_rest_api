package com.example.todo_list_rest_api.task.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseBodyOnlyMessage {
	@ApiModelProperty(value = "メッセージ")
	private String message;
}
