package com.lq.pwdmanage.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security 配置
 *
 * @author LQ
 * @date 2019年12月23日 下午3:35:08
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity http) throws Exception {
		// 关闭 CSRF
        http.csrf().disable().httpBasic();

        http.antMatcher("/**").authorizeRequests()
            .antMatchers("/**").authenticated()
            .and()
            .headers()
            .frameOptions()
            .sameOrigin();

        super.configure(http);
    }

    /**
     * 配置放行的资源
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        //静态资源地址
        web.ignoring().antMatchers("/static/**");
    }

}
