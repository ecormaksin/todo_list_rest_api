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
	
	private static final String VALUE_REQUIRED_MESSAGE = " cannot be null or empty.";
	
	@ApiModelProperty(value = "ID（自動採番）", position = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;
	@ApiModelProperty(value = "タイトル", required = true, position = 2)
	@Column(nullable = false)
	@Getter
	private String title;
	@ApiModelProperty(value = "内容", required = true, position = 3)
	@Column(nullable = false)
	@Getter
	private String detail;
	
	public Task(String title, String detail) {
		this(null, title, detail);
	}

	public Task(Integer id, String title, String detail) {
		this.id = id;
		setTitle(title);
		setDetail(detail);
	}
	
	public void setTitle(String title) {
		if("".equals(StringUtils.defaultString(title))) {
			throw new IllegalArgumentException("title" + VALUE_REQUIRED_MESSAGE);
		}
		this.title = title;
	}
	
	public void setDetail(String detail) {
		if("".equals(StringUtils.defaultString(detail))) {
			throw new IllegalArgumentException("detail" + VALUE_REQUIRED_MESSAGE);
		}
		this.detail = detail;
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
