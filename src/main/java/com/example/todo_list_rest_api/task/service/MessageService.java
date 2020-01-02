package com.example.todo_list_rest_api.task.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	@Autowired
	MessageSource msg;
	
	public String get(String code, @Nullable Object[] args) throws NoSuchMessageException {
		return msg.getMessage(code, args, Locale.JAPAN);
	}

}
