package org.ospic.platform.config.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * This file was created by eli on 10/10/2020 for org.ospic.platform.configurations.swagger
 * --
 * --
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
@EnableSwagger2
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerApiDocumentationConfigurations {

    private final ServletContext servletContext;

    @Autowired
    public SwaggerApiDocumentationConfigurations(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public static ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("(Ospic) Hospital Management System")
                .description("Ospic Spring platform is a spring boot API based for hospital management system")
                .license("Apache License 2.0").licenseUrl("https://github.com/ospic/platform/blob/master/LICENSE")
                .contact(new Contact("Elirehema Paul", "https://app.ospicx.com/", "elirehemapaulo@gmail.com"))
                .version("1.0")
                .build();
    }

    @Bean
    public Docket api() throws IOException, URISyntaxException {
        final List<ResponseMessage> globalResponses = Arrays.asList(
                new ResponseMessageBuilder()
                        .code(200)
                        .message("OK")
                        .build(),
                new ResponseMessageBuilder()
                        .code(204)
                        .message("No Content")
                        .build(),
                new ResponseMessageBuilder()
                        .code(400)
                        .message("Bad Request")
                        .build(),
                new ResponseMessageBuilder()
                        .code(500)
                        .message("Internal server Error")
                        .build(),
                new ResponseMessageBuilder()
                        .code(404)
                        .message("Entity Not Found")
                        .build(),
                new ResponseMessageBuilder()
                        .code(403)
                        .message("Forbidden")
                        .build(),
                new ResponseMessageBuilder()
                        .code(401)
                        .message("UnAuthorized")
                        .build());
        return new Docket(DocumentationType.SWAGGER_2)
                .host("ospicapi.herokuapp.com")
                .pathProvider(new RelativePathProvider(servletContext){
                    @Override
                    protected String getDocumentationPath() {
                        return "/api";
                    }
                })
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, globalResponses)
                .globalResponseMessage(RequestMethod.POST, globalResponses)
                .globalResponseMessage(RequestMethod.DELETE, globalResponses)
                .globalResponseMessage(RequestMethod.PATCH, globalResponses)
                .globalResponseMessage(RequestMethod.PUT, globalResponses)

                .select()
                .apis(RequestHandlerSelectors.basePackage("org.ospic.platform"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metadata());
    }

}
