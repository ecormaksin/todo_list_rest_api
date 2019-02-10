package com.example.todo_list_rest_api;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.todo_list_rest_api.swagger.ApiModelPage;
import com.example.todo_list_rest_api.task.domain.Task;
import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
 * ＜参考URL＞
 * https://qiita.com/YutaKase6/items/52ea048c5352c77330eb
 * https://qiita.com/NagaokaKenichi/items/b6d4d55a202e6a93d047
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

	@Autowired
	private TypeResolver typeResolver;
	  
    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("todo-api")
            	.ignoredParameterTypes(Pageable.class) // https://github.com/springfox/springfox/issues/1139
            	.genericModelSubstitutes(Page.class)
            	.alternateTypeRules(
            			AlternateTypeRules.newRule(typeResolver.resolve(Page.class, 
            	                typeResolver.resolve(Page.class, Task.class)),
            	                typeResolver.resolve(Task.class)))
//            	.directModelSubstitute(Pageable.class, ApiModelPageable.class)
            	.directModelSubstitute(Page.class, ApiModelPage.class)
            	.produces(produces())
        		.select()
                	.paths(PathSelectors.regex("/api/tasks.*"))
                	.build()
        		.useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                ;
    }
    
    private Set<String> produces() {
    	Set<String> produces = new HashSet<>();
    	produces.add("application/json");
    	return produces;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ToDo List API")
                .description("検索、登録、更新、削除の操作が行えるTodo リスト用 REST APIです。")
                .version("0.0.1")
                .build();
    }
}
