package com.example.todo_list_rest_api.swagger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "Pageable")
@Data
public class ApiModelPageable {
	@ApiModelProperty(position = 1)
	private ApiModelSort sort;
	@ApiModelProperty(value = "先頭からのオフセット", position = 2)
	private int offset;
	@ApiModelProperty(value = "1ページ分の最大件数", position = 3)
	private int pageSize;
	@ApiModelProperty(value = "ページ番号（0始まり）", position = 4)
	private int pageNumber;
	@ApiModelProperty(value = "現状のPageableにページネーション情報が含まれている場合はtrue、それ以外はfalse", position = 5)
	private boolean paged;
	@ApiModelProperty(value = "現状のPageableにページネーション情報が含まれていない場合はtrue、それ以外はfalse ", position = 6)
	private boolean unpaged;
}
