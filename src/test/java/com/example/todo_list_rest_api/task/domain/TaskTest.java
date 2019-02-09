package com.example.todo_list_rest_api.task.domain;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TaskTest {

	String titleMaxLengthValue = StringUtils.repeat("a", Task.TITLE_MAX_LENGTH);
	String titleLengthOverValue = titleMaxLengthValue + "a";
	String detailMaxValue = StringUtils.repeat("あ", Task.DETAIL_MAX_BYTES / 3 );
	String detailBytesOverValue = detailMaxValue + "a";
	
	@Rule
    public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void タイトルに上限文字数の値が設定されている時は例外が発生しない_内容はNULL() throws Exception {
		new Task(titleMaxLengthValue, null);
	}

	@Test
	public void タイトルに上限文字数の値が設定されている時は例外が発生しない_内容は長さ0() throws Exception {
		new Task(titleMaxLengthValue, "");
	}

	@Test
	public void タイトルに上限文字数の値が設定されている時は例外が発生しない_内容は1GB() throws Exception {
		new Task(titleMaxLengthValue, detailMaxValue);
	}

	@Test
	public void タイトルがNULLの時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title cannot be null or empty.");
		new Task(null, detailMaxValue);
	}

	@Test
	public void タイトルが長さ0の時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title cannot be null or empty.");
		new Task("", detailMaxValue);
	}

	@Test
	public void タイトルが255文字を超えている時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Title's length must be less than equals 255 characters.");
		new Task(titleLengthOverValue, detailMaxValue);
	}

	@Test
	public void 内容が1GBを超えている時はIllegalArgumentExceptionが発生する() throws Exception {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("Detail's size must be less than equals 1073741824 bytes.");
		new Task(titleMaxLengthValue, detailBytesOverValue);
	}
}
