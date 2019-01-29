package com.example.todo_list_rest_api.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SearchTargetTasks {

	Map<Boolean, Map<Task, Task>> allocationMap = new HashMap<>();
	Map<Task, Task> allTaskMap = new TreeMap<>();
	Map<Task, Task> includedMap = new TreeMap<>();
	Map<Task, Task> excludedMap = new TreeMap<>();
	
	{
		allocationMap.put(Boolean.TRUE, includedMap);
		allocationMap.put(Boolean.FALSE, excludedMap);
		
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
		setData("タイトル11", "内容11", false);
		setData("タイトル12あ", "あ内容12");
		setData("タイトル13", "内あ容13");
		setData("タイトル14あ", "内あ容14");
		setData("タイトル15", "内容15あ");
		setData("タイトル16あ", "内容16");
		
		allTaskMap.putAll(includedMap);
		allTaskMap.putAll(excludedMap);
	}
	
	private void setData(String title, String detail) {
		setData(title, detail, true);
	}
	
	private void setData(String title, String detail, boolean isIncluded) {
		Task task = new Task(null, title, detail);
		Map<Task, Task> target = isIncluded ? includedMap : excludedMap;
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
