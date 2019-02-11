package com.example.todo_list_rest_api.task.domain;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TaskTest {

	String titleMaxLengthValue = StringUtils.repeat("a", Task.TITLE_MAX_LENGTH);
	String titleLengthOverValue = titleMaxLengthValue + "a";
	String detail = "内容テスト";
	
	@Rule
    public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void タイトルに上限文字数の値が設定されている時は例外が発生しない_内容はNULL() throws Exception {
		Task.createWithoutId(titleMaxLengthValue, null);
	}

	@Test
	public void タイトルに上限文字数の値が設定されている時は例外が発生しない_内容は長さ0() throws Exception {
		Task.createWithoutId(titleMaxLengthValue, "");
	}

	@Test
	public void タイトルに上限文字数の値が設定されている時は例外が発生しない_内容は値あり() throws Exception {
		Task.createWithoutId(titleMaxLengthValue, detail);
	}

	@Test
	public void タイトルがNULLの時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title cannot be null or empty.");
		Task.createWithoutId(null, detail);
	}

	@Test
	public void タイトルが長さ0の時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title cannot be null or empty.");
		Task.createWithoutId("", detail);
	}

	@Test
	public void タイトルが半角スペースのみの時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title must not be blank.");
		Task.createWithoutId(" ", detail);
	}

	@Test
	public void タイトルが全角スペースのみの時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title must not be blank.");
		Task.createWithoutId("　", detail);
	}

	@Test
	public void タイトルが255文字を超えている時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title's length must be less than equals 255 characters.");
		Task.createWithoutId(titleLengthOverValue, detail);
	}
}
