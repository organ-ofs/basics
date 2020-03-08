package com.ofs.web.config;

import com.ofs.web.constant.StaticConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gaoly
 * @version 2019/9/14
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createSysApi() {
        //在配置好的配置类中增加此段代码
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        //name表示名称，description表示描述
        ticketPar.name("Authorization").description("登录校验")
                .modelRef(new ModelRef("string")).parameterType("header")
                //Basic
                //required表示是否必填，defaultvalue表示默认值
                .required(false).defaultValue(StaticConstant.TOKEN_START).build();
        //添加完此处一定要把下边的带***的也加上否则不生效
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("系统管理")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ofs.sys.web.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("默认分组")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .pathMapping("/api/**");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API文档")
                .description("API文档")
                .version("1.0")
                .build();
    }
}
