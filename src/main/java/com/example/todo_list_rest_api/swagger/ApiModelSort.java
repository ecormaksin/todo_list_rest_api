package com.example.todo_list_rest_api.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Sort")
@Data
public class ApiModelSort {

	@ApiModelProperty(value = "ソートキーが未指定の場合はtrue（※今回はソート指定に対応していないため常にtrue）")
	private boolean empty;
	@ApiModelProperty(value = "ソートされている場合はtrue（※今回はソート指定に対応していないため常にfalse）")
	private boolean sorted;
	@ApiModelProperty(value = "ソートされていない場合はtrue（※今回はソート指定に対応していないため常にtrue）")
	private boolean unsorted;
}
