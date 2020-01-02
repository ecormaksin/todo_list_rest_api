package com.example.todo_list_rest_api.task.domain;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class TaskTest {

	static String titleMaxLengthValue = StringUtils.repeat("a", Task.TITLE_MAX_LENGTH);
	static String titleLengthOverValue = titleMaxLengthValue + "a";
	static String detailMaxLengthValue = StringUtils.repeat("a", Task.DETAIL_MAX_LENGTH);
	static String detailLengthOverValue = detailMaxLengthValue + "a";
	
	public static class タイトル {
		@Rule
	    public ExpectedException expectedException = ExpectedException.none();

		@Test
		public void 上限255文字が設定されている時は例外が発生しない_内容はNULL() throws Exception {
			Task.createWithoutId(titleMaxLengthValue, null);
		}

		@Test
		public void 上限255文字が設定されている時は例外が発生しない_内容は長さ0() throws Exception {
			Task.createWithoutId(titleMaxLengthValue, "");
		}

		@Test
		public void 上限255文字が設定されている時は例外が発生しない_内容は値あり() throws Exception {
			Task.createWithoutId(titleMaxLengthValue, detailMaxLengthValue);
		}

		@Test
		public void NULLの時はIllegalArgumentExceptionが発生する() throws Exception {
			expectedException.expect(IllegalArgumentException.class);
			expectedException.expectMessage("Title must not be null or empty or blank.");
			Task.createWithoutId(null, detailMaxLengthValue);
		}

		@Test
		public void 長さ0の時はIllegalArgumentExceptionが発生する() throws Exception {
			expectedException.expect(IllegalArgumentException.class);
			expectedException.expectMessage("Title must not be null or empty or blank.");
			Task.createWithoutId("", detailMaxLengthValue);
		}

		@Test
		public void 半角スペースのみの時はIllegalArgumentExceptionが発生する() throws Exception {
			expectedException.expect(IllegalArgumentException.class);
			expectedException.expectMessage("Title must not be null or empty or blank.");
			Task.createWithoutId(" ", detailMaxLengthValue);
		}

		@Test
		public void 全角スペースのみの時はIllegalArgumentExceptionが発生する() throws Exception {
			expectedException.expect(IllegalArgumentException.class);
			expectedException.expectMessage("Title must not be null or empty or blank.");
			Task.createWithoutId("　", detailMaxLengthValue);
		}

		@Test
		public void 上限255文字を超えている時はIllegalArgumentExceptionが発生する() throws Exception {
			expectedException.expect(IllegalArgumentException.class);
			expectedException.expectMessage("Title must be within 255 characters.");
			Task.createWithoutId(titleLengthOverValue, detailMaxLengthValue);
		}
	}
	
	public static class 内容 {
		@Rule
	    public ExpectedException expectedException = ExpectedException.none();

		@Test
		public void 上限2000文字を超えている時はIllegalArgumentExceptionが発生する() throws Exception {
			expectedException.expect(IllegalArgumentException.class);
			expectedException.expectMessage("Detail must be within 2,000 characters.");
			Task.createWithoutId(titleMaxLengthValue, detailLengthOverValue);
		}
	}
}
