package com.ofs.web.config;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


//通过configuration注解自动注入配置文件
@Configuration
//开启swagger功能
@EnableSwagger2
//如果有多个配置文件，以这个为准
@Primary
public class DocumentConfig implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;

    public DocumentConfig(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        routes.forEach(route -> {
            resources.add(swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"), "1.0"));
        });
        return resources;
    }

//    @Override
//    public List<SwaggerResource> get() {
//        List resources = new ArrayList<>();
//        resources.add(swaggerResource("管理模块", "/v2/api-docs", "2.0"));
//        resources.add(swaggerResource("测试模块", "/v2/api-docs", "2.0"));
//        return resources;
//    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
