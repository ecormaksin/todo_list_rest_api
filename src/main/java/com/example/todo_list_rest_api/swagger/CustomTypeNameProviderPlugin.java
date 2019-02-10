package com.example.todo_list_rest_api.swagger;

import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.TypeNameProviderPlugin;

//@Component
public class CustomTypeNameProviderPlugin implements TypeNameProviderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public String nameFor(Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

}
