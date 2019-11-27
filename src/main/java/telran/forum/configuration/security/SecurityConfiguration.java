package telran.forum.configuration.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

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
		http.authorizeRequests()
			.antMatchers("/forum/posts/**").permitAll()
			.antMatchers(HttpMethod.GET, "/forum/post/{id}/**").authenticated()
			.antMatchers(HttpMethod.PUT, "/forum/post/{id}/like").authenticated()
			.antMatchers("/account/user/*").authenticated()
			.antMatchers(HttpMethod.POST, "/account/login").authenticated()
			.antMatchers("/account/user/{login}/role/{role}").hasRole("ADMINISTRATOR")
			.antMatchers(HttpMethod.DELETE, "/forum/post/{id}").access("@customSecurity.checkAuthorityForDeletePost(#id, authentication) or hasRole('MODERATOR')");
	}
}
