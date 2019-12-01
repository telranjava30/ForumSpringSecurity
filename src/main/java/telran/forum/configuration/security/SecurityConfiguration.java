package telran.forum.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import telran.forum.service.security.ExpDateFilter;

//@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.POST, "/account/user");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.httpBasic();
		http.csrf().disable();
		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.addFilterAfter(new ExpDateFilter(), BasicAuthenticationFilter.class);
		http.authorizeRequests()
			.antMatchers("/forum/posts/**").permitAll()
			.antMatchers(HttpMethod.GET, "/forum/post/{id}/**").hasAnyRole("ADMINISTRATOR", "MODERATOR", "USER")
			.antMatchers(HttpMethod.PUT, "/forum/post/{id}/like").hasAnyRole("ADMINISTRATOR", "MODERATOR", "USER")
			.antMatchers("/account/user").hasAnyRole("ADMINISTRATOR", "MODERATOR", "USER")
			.antMatchers(HttpMethod.PUT, "/account/user/password").authenticated()
			.antMatchers(HttpMethod.POST, "/account/login").hasAnyRole("ADMINISTRATOR", "MODERATOR", "USER")
			.antMatchers("/account/user/{login}/role/{role}").hasRole("ADMINISTRATOR")
			.antMatchers(HttpMethod.DELETE, "/forum/post/{id}").access("@customSecurity.checkAuthorityForPost(#id, authentication) or hasRole('MODERATOR')")
			.antMatchers(HttpMethod.PUT, "/forum/post/{id}").access("@customSecurity.checkAuthorityForPost(#id, authentication)")
			.antMatchers(HttpMethod.POST, "/forum/post/{author}").access("#author == authentication.name and hasAnyRole('ADMINISTRATOR', 'MODERATOR', 'USER')")
			.antMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}").access("#author == authentication.name and hasAnyRole('ADMINISTRATOR', 'MODERATOR', 'USER')")
			.antMatchers("/actuator/**").hasRole("ADMINISTRATOR");
	}
}
