package com.example.todo_list_rest_api.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //https://teratail.com/questions/128905
public class Task implements Comparable<Task> {
	
	private static final String VALUE_REQUIRED_MESSAGE = " cannot be null or empty.";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;
	@Column(nullable = false)
	@Getter
	private String title;
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
