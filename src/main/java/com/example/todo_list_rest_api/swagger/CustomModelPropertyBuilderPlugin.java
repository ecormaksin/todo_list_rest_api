package com.example.todo_list_rest_api.swagger;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.google.common.base.Optional;

import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.service.AllowableRangeValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

@Component
//@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER)
public class CustomModelPropertyBuilderPlugin implements ModelPropertyBuilderPlugin {

	@Override
	public boolean supports(DocumentationType delimiter) {
		return true;
	}

	@Override
	public void apply(ModelPropertyContext context) {
		ModelPropertyBuilder builder = context.getBuilder();

		// プロパティのgetterを取得する
		Optional<BeanPropertyDefinition> beanPropDef = context.getBeanPropertyDefinition();
		BeanPropertyDefinition beanDef = beanPropDef.get();
		if (null == beanDef) return;
		AnnotatedMethod method = beanDef.getGetter();
		if (null == method) return;
		
		// 必須・非必須を取得する
		setRequiredAndAllowEmptyValue(builder, method);
		
		// 範囲制約を取得する
		Size size = method.getAnnotation(Size.class);
		if (size != null) {
			builder.allowableValues(new AllowableRangeValues(
					Long.toString(size.min()), Long.toString(size.max())));
		}
	}
	
	enum EndLoop {
		TRUE,
		FALSE,
		;
	}
	
	private void setRequiredAndAllowEmptyValue(ModelPropertyBuilder builder, AnnotatedMethod method) {
		
		NotBlank notBlank = method.getAnnotation(NotBlank.class);
		setRequiredFromAnnotations(builder, method, notBlank);
		setAllowEmptyValue(builder, notBlank);
	}
	
	private void setRequiredFromAnnotations(ModelPropertyBuilder builder
			, AnnotatedMethod method
			, NotBlank notBlank) {
		
		for (Annotation annotation : getAnnotationsForRequiredProperty(method, notBlank)) {
			EndLoop endLoop = setRequiredFromAnnotation(builder, annotation);
			if (endLoop == EndLoop.TRUE) break;
		}
	}
	
	private List<Annotation> getAnnotationsForRequiredProperty(AnnotatedMethod method, NotBlank notBlank) {

		List<Annotation> annotations = new ArrayList<>();

		annotations.add(method.getAnnotation(NotNull.class));
		annotations.add(method.getAnnotation(NotEmpty.class));
		annotations.add(notBlank);
		
		return annotations;
	}
	
	private EndLoop setRequiredFromAnnotation(ModelPropertyBuilder builder, Annotation annotation) {
		
		if (null == annotation) {
			return EndLoop.FALSE;
		}
		builder.required(true);
		return EndLoop.TRUE;
	}
	
	private void setAllowEmptyValue(ModelPropertyBuilder builder, NotBlank notBlank) {
		if (null == notBlank) return;
		builder.allowEmptyValue(false);
	}
}
