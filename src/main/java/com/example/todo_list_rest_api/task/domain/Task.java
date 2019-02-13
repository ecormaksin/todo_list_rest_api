package com.example.todo_list_rest_api.task.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //https://teratail.com/questions/128905
@ApiModel(value = "Task")
public class Task implements Comparable<Task> {
	
	public static final int TITLE_MAX_LENGTH = 255;
	public static final int DETAIL_MAX_LENGTH = 2000;

	private static final String ERROR_MESSAGE_LENGTH_OVER = "{0} must be within {1} characters.";

	private static final String TITLE_NAME = "Title";
	private static final String ERROR_MESSAGE_TITLE_REQUIRED = TITLE_NAME + " must not be null or empty or blank.";

	private static final String DETAIL_NAME = "Detail";
	
	@ApiModelProperty(value = "ID（自動採番）", position = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;
	@ApiModelProperty(value = "タイトル", position = 2)
	@Column(nullable = false)
	@NotBlank
	@Size(min = 1, max = TITLE_MAX_LENGTH)
	@Getter
	private String title;
	@ApiModelProperty(value = "内容", position = 3)
	@Size(max = DETAIL_MAX_LENGTH)
	@Getter
	private String detail;
	
	public Task() {
	}

	public Task(Integer id
			, @NotBlank @Size(min = 1, max = TITLE_MAX_LENGTH) String title
			, @Size(max = DETAIL_MAX_LENGTH) String detail) throws IllegalArgumentException {
		this.id = id;
		setTitle(title);
		setDetail(detail);
	}

	public static Task createWithoutId(
			@NotBlank @Size(min = 1, max = TITLE_MAX_LENGTH) String title
			, @Size(max = DETAIL_MAX_LENGTH) String detail) throws IllegalArgumentException {
		return new Task(null, title, detail);
	}

	public void setTitle(String title) throws IllegalArgumentException {
		checkTitle(title);
		this.title = title;
	}
	
	private static void checkTitle(String title) throws IllegalArgumentException {
		if("".equals(StringUtils.defaultString(title))
				|| StringUtils.containsOnly(title, ' ', '　')) {
			throw new IllegalArgumentException(ERROR_MESSAGE_TITLE_REQUIRED);
		}
		checkLength(TITLE_NAME, title, TITLE_MAX_LENGTH);
	}
	
	public void setDetail(String detail) throws IllegalArgumentException {
		checkDetail(detail);
		this.detail = detail;
	}
	
	private static void checkDetail(String detail) throws IllegalArgumentException {
		if (null == detail) return;
		checkLength(DETAIL_NAME, detail, DETAIL_MAX_LENGTH);
	}
	
	private static void checkLength(String fieldName, String value, int length) throws IllegalArgumentException {
		if (length >= value.length()) return;
		throw new IllegalArgumentException(
				java.text.MessageFormat.format(ERROR_MESSAGE_LENGTH_OVER, fieldName, length)
				);
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
