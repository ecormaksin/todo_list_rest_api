package com.example.todo_list_rest_api.task.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel(value = "Task")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //https://teratail.com/questions/128905
@NoArgsConstructor
@Table(name = "tasks")
public class Task implements Comparable<Task> {
	
	public static final int TITLE_MAX_LENGTH = 255;

	private static final String TITLE_NAME = "Title";
	private static final String ERROR_MESSAGE_TITLE_REQUIRED = TITLE_NAME + " cannot be null or empty.";
	private static final String ERROR_MESSAGE_TITLE_LENGTH_OVER = TITLE_NAME + "'s length must be less than equals " + TITLE_MAX_LENGTH + " characters.";
	
	@ApiModelProperty(value = "ID（自動採番）", position = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;
	@ApiModelProperty(value = "タイトル（必須、255文字以内）", required = true, position = 2)
	@Column(nullable = false)
	@Getter
	private String title;
	@ApiModelProperty(value = "内容（任意、文字数制限は1GB(4バイト文字だと268,435,456文字)以内）", required = false, position = 3)
	@Column(nullable = true, columnDefinition = "text")
	@Getter
	@Setter
	private String detail;
	
	public Task(String title, String detail) throws Exception {
		this(null, title, detail);
	}

	public Task(Integer id, String title, String detail) throws Exception {
		this.id = id;
		setTitle(title);
		setDetail(detail);
	}
	
	public void setTitle(String title) throws Exception {
		checkTitle(title);
		this.title = title;
	}
	
	private void checkTitle(String title) throws Exception {
		if("".equals(StringUtils.defaultString(title))) {
			throw new IllegalArgumentException(ERROR_MESSAGE_TITLE_REQUIRED);
		}
		if (TITLE_MAX_LENGTH < title.length()) {
			throw new IllegalArgumentException(ERROR_MESSAGE_TITLE_LENGTH_OVER);
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (null != id) {
			sb.append("id: ");
			sb.append(id);
			sb.append(", ");
		}
		sb.append("title: ");
		sb.append(title);
		sb.append(", detail: ");
		sb.append(detail);
		return sb.toString();
	}
	
	@Override
	public int compareTo(final Task other) {
		
		int titleCompareResult = compareToIgnoreCase(this.title, other.title);
		if (0 != titleCompareResult) {
			return titleCompareResult;
		}
		
		int detailCompareResult =  compareToIgnoreCase(this.detail, other.detail);
		if (0 != detailCompareResult) {
			return detailCompareResult;
		}
		return  ObjectUtils.compare(this.id, other.id);
	}
	
	private int compareToIgnoreCase(final String mine, final String theirs) {
		return StringUtils.defaultString(mine).compareToIgnoreCase(StringUtils.defaultString(theirs));
	}
}
