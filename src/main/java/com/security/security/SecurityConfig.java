package com.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.cors.CorsUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //  启用方法级别的权限认证，想用注解必须加这个
//@PreAuthorize("hasRole(‘admin‘)")
//@PreAuthorize("hasPessmion(‘admin‘)")
//WebSecurityConfigurerAdapter适配器
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    //WebAuthenticationDetails提供了remoteAddress与sessionId信息
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    protected void configure(HttpSecurity http) throws Exception {
        //  允许所有用户访问"/"和"/index.html"
        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//设置跨域
                .antMatchers("/getVerifyCode").permitAll()//允许该url匿名访问 其实就是放行
                /*.antMatchers("/", "/index.do").permitAll()*/
                .anyRequest().authenticated()   // 其他地址的访问均需验证权限
                .and()
                //表单登录
                //.basic 这是浮框的登录方式
                .formLogin()
                //这个一定要不加会出现302跨域问题
                .successForwardUrl("/user/hi")
                .loginPage("/login.do").permitAll()  //  登录页 允许所有用户访问这个页面
                .loginProcessingUrl("/user/hi")
                .failureUrl("/loginError.do")//允许所有用户访问登录页面以及登录失败页面 这玩意就是放行
                // 自定义登陆用户名和密码参数，默认为username和password
//                .usernameParameter("username")
//                .passwordParameter("password")
                //这是个关于验证码的
                .authenticationDetailsSource(authenticationDetailsSource)
                .and()
                .logout()
                .logoutSuccessUrl("/logout.do");//其实它也自带
        //设置跨域
        http.csrf().disable();
    }

    //这是给密码编码
    //构建AuthenticationManager委托给AuthenticationProvider进行验证
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
