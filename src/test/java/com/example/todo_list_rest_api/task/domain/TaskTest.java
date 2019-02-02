package com.example.todo_list_rest_api.task.domain;

import org.junit.Test;

import com.example.todo_list_rest_api.task.domain.Task;

public class TaskTest {

	String title = "タイトルテスト";
	String detail = "内容テスト";
	
	@Test
	public void testタイトルと内容に値が設定されている時は例外が発生しない() {
		new Task(title, detail);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testタイトルがNULLの時はIllegalArgumentExceptionが発生する() {
		new Task(null, detail);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testタイトルが長さ0の時はIllegalArgumentExceptionが発生する() {
		new Task("", detail);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test内容がNULLの時はIllegalArgumentExceptionが発生する() {
		new Task(title, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test内容が長さ0の時はIllegalArgumentExceptionが発生する() {
		new Task(title, "");
	}
}
