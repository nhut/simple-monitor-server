/*
 * Copyright since 2019 Nhut Do <mr.nhut.dev@gmail.com>
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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configurations.
 *
 * @author Nhut Do (mr.nhut.dev@gmail.com)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.security.admin.username}")
    private String userAdminUsername;

    @Value("${app.security.admin.password}")
    private String userAdminPassword;

    @Value("${app.security.api.username}")
    private String userApiUsername;

    @Value("${app.security.api.password}")
    private String userApiPassword;

    @Override
    protected void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.inMemoryAuthentication()
                .withUser(userAdminUsername).password(passwordEncoder.encode(userAdminPassword)).roles(UserRole.ADMIN.getName())
                .and()
                .withUser(userApiUsername).password(passwordEncoder.encode(userApiPassword)).roles(UserRole.API.getName())
        ;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/api/**").hasRole(UserRole.API.getName())
                .antMatchers("/actuator/", "/actuator/info").hasRole(UserRole.ADMIN.getName())
                .antMatchers("/swagger-resources/*", "/swagger-ui.html", "/api/v1/swagger.json").hasRole(UserRole.ADMIN.getName())
                .anyRequest().authenticated()
                .and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
