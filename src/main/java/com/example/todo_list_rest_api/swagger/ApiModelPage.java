package com.example.todo_list_rest_api.swagger;

import com.example.todo_list_rest_api.task.domain.Task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Page")
@Data
public class ApiModelPage {

	@ApiModelProperty(value = "タスクの配列", position = 1)
	private Task[] content;
	@ApiModelProperty(position = 2)
	private ApiModelPageable pageable;
	@ApiModelProperty(value = "最終ページの場合はtrue", position = 3)
	private boolean last;
	@ApiModelProperty(value = "トータルページ数", position = 4)
	private int totalPages;
	@ApiModelProperty(value = "トータル件数（キーワード指定時は条件に該当するもの）", position = 5)
	private int totalElements;
	@ApiModelProperty(value = "1ページ分の最大件数", position = 6)
	private int size;
	@ApiModelProperty(value = "ページ番号（0始まり）", position = 7)
	private int number;
	@ApiModelProperty(value = "先頭ページの場合はtrue", position = 8)
	private boolean first;
	@ApiModelProperty(value = "取得された件数", position = 9)
	private int numberOfElements;
	@ApiModelProperty(value = "ソート", position = 10)
	private ApiModelSort sort;
	@ApiModelProperty(value = "タスクが0件の場合はtrue", position = 11)
	private boolean empty;
}
