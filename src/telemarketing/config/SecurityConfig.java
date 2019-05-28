package telemarketing.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import com.sun.org.apache.xpath.internal.operations.And;

import telemarketing.myimplclass.Md5PasswordEncoder;

//此类将会启用web安全检查
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource datasource;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		
		auth.jdbcAuthentication()
		.dataSource(datasource)
		.usersByUsernameQuery("select phone as username ,user_pwd as password,true from pt_user "
				+ "where phone=? and state=0")
		.authoritiesByUsernameQuery("select phone as username,role_code from pt_user "
				+ "where phone=? and state=0")
		.passwordEncoder(new Md5PasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.formLogin().loginPage("/login")
		.and()
		.rememberMe()
		.tokenValiditySeconds(30*24*3600).key("user_key")
		.and()
		.authorizeRequests()
		.antMatchers("/").authenticated()
		.antMatchers("/home**").authenticated()
		.antMatchers("/customer/**").authenticated()
		.antMatchers("/customer/add**").access("hasAnyRole('ADMIN','CSO')")
		.antMatchers("/customer/edit**").access("hasAnyRole('ADMIN','CSO','TeamLeader')")
		.antMatchers("/customer/pager**").access("hasAnyRole('ADMIN','CSO','TeamLeader')")
		.antMatchers("/customer/dispatchTo**").access("hasAnyRole('ADMIN','CSO','TeamLeader')")
		.antMatchers("/phoneorder/**").authenticated()
		.antMatchers("/reports/**").authenticated()
		.antMatchers("/user/**").authenticated()
		.antMatchers("/user/add**").access("hasRole('ADMIN')")
		.antMatchers("/user/edit**").access("hasRole('ADMIN')")
		.antMatchers("/remind/**").authenticated()
		.antMatchers("/login_record/**").authenticated()
		.antMatchers("/login_record/list**").access("hasAnyRole('ADMIN','CSO')")
		.antMatchers("/allow_ip/**").access("hasAnyRole('ADMIN','CSO')")
		.anyRequest().permitAll()
		.and()
		//.csrf().requireCsrfProtectionMatcher(new CsrfSecurityRequestMatcher());
		.csrf().disable();
	} 
}
