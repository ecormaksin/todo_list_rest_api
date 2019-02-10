package com.example.todo_list_rest_api.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ModelBuilder;
import springfox.documentation.schema.Model;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;

//@Component
public class CustomModelBuilderPlugin implements ModelBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public void apply(ModelContext context) {
		ModelBuilder builder = context.getBuilder();
		
		TypeResolver typeResolver = new TypeResolver();
		Model model = builder.build();
		ResolvedType type = model.getType();
//		if (type instanceof Page) {
//			context.alternateFor(typeResolver.resolve(ApiModelPage.class));
//		}
//		try {
//			System.out.println("★");
//			System.out.println("model.getBaseModel(): " + model.getBaseModel());
//			System.out.println("model.getDescription(): " + model.getDescription());
//			System.out.println("model.getDiscriminator(): " + model.getDiscriminator());
//			System.out.println("model.getExample(): " + model.getExample());
//			System.out.println("model.getId(): " + model.getId());
//			System.out.println("model.getName(): " + model.getName());
//			//System.out.println(model.getProperties().toString());
//			System.out.println("model.getQualifiedType(): " + model.getQualifiedType());
//			//System.out.println(model.getSubTypes().toString());
//			System.out.println("model.getType().toString(): " + model.getType().toString());
//			System.out.println("model.getXml().toString(): " + model.getXml().toString());
//			
//			Map<String, ModelProperty> map = model.getProperties();
//			for (Map.Entry<String, ModelProperty> entry : map.entrySet()) {
//				System.out.println(entry.getKey());
//			    System.out.println(entry.getValue().getName());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
