/*
 * Copyright since 2019 Nhut Do <mr.nhut@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.donhut.simplemonitorserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

//import org.springframework.core.env.Profiles;

/**
 * Swagger configuration.<br>
 * - <a href="http://localhost/v2/api-docs">API schema</a><br>
 * - <a href="http://localhost/swagger-ui.html">API UI documentation</a><br>
 * <br>
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Autowired
    private Environment environment;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public Docket api() {
        final ApiSelectorBuilder apiSelectorBuilder = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any()); //limit which package content will be shown in api
        final boolean hasDevProfile = Arrays.asList(environment.getActiveProfiles()).contains("dev");
        if (!hasDevProfile) {
            apiSelectorBuilder.paths(input -> input != null && input.startsWith("/api")); //limit which rest url api will be exposed
        }

        return apiSelectorBuilder.build()
                .forCodeGeneration(true)
                .apiInfo(new ApiInfo(
                        appName + " API",
                        "This is swagger schema is for client codegen only. Check the official documentation here: /swagger-ui.html",
                        "v1",
                        "https://github.com/nhut/simple-monitor-server/blob/master/LICENSE",
                        null,
                        "License of API",
                        "https://github.com/nhut/simple-monitor-server/blob/master/LICENSE",
                        Collections.emptyList()));
    }
}