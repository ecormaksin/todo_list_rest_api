package com.example.todo_list_rest_api.task.controller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

@Value
public class ResponseBodyOnlyMessage {
	@ApiModelProperty(value = "メッセージ")
	private String message;
}
