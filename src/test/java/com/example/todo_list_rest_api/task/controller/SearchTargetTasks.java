package com.example.todo_list_rest_api.task.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.example.todo_list_rest_api.task.domain.Task;

@Component
public class SearchTargetTasks {

	enum SearchTarget {
		INCLUDED,
		EXCLUDED;
	}
	
	Map<SearchTarget, Map<Task, Task>> allocationMap = new HashMap<>();
	Map<Task, Task> allTaskMap = new TreeMap<>();
	Map<Task, Task> includedMap = new TreeMap<>();
	Map<Task, Task> excludedMap = new TreeMap<>();
	
	@PostConstruct
	public void setUp() throws Exception {
		allocationMap.put(SearchTarget.INCLUDED, includedMap);
		allocationMap.put(SearchTarget.EXCLUDED, excludedMap);
		
		setData("あタイトル1", "内容1");
		setData("あタイトル2", "内容2あ");
		setData("あタイトル3", "内あ容3");
		setData("あタイトル4", "あ内容4");
		setData("タイあトル5", "内容5");
		setData("タイあトル6", "内容6あ");
		setData("タイあトル7", "あ内容7");
		setData("タイあトル8", "内あ容8");
		setData("タイトル9", "あ内容9");
		setData("タイトル10あ", "内容10あ");
		setData("タイトル11", "内容11", SearchTarget.EXCLUDED);
		setData("タイトル12あ", "あ内容12");
		setData("タイトル13", "内あ容13");
		setData("タイトル14あ", "内あ容14");
		setData("タイトル15", "内容15あ");
		setData("タイトル16あ", "内容16");
		
		allTaskMap.putAll(includedMap);
		allTaskMap.putAll(excludedMap);
	}
	
	private void setData(String title, String detail) throws Exception {
		setData(title, detail, SearchTarget.INCLUDED);
	}
	
	private void setData(String title, String detail, SearchTarget searchTarget) throws Exception {
		Task task = new Task(title, detail);
		Map<Task, Task> target = allocationMap.get(searchTarget);
		target.put(task, task);
	}
	
	public List<Task> getAllList() {
		return getUnmodifiableList(allTaskMap);
	}
	
	public List<Task> getIncludedList() {
		return getUnmodifiableList(includedMap);
	}
	
	private List<Task> getUnmodifiableList(Map<Task, Task> map) {
		return Collections.unmodifiableList(new ArrayList<Task>(map.values()));
	}
}
