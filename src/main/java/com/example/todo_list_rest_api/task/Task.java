package com.example.todo_list_rest_api.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //https://teratail.com/questions/128905
public class Task implements Comparable<Task> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String detail;
	
	@Override
	public int compareTo(Task other) {
		
		int titleCompareResult = this.title.compareToIgnoreCase(other.title);
		if (0 != titleCompareResult) {
			return titleCompareResult;
		}
		
		int detailCompareResult = this.detail.compareToIgnoreCase(other.detail);
		if (0 != detailCompareResult) {
			return detailCompareResult;
		}
		
		return 0;
	}
}
