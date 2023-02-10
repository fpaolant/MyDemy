package it.univaq.disim.mwt.mydemy.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().disable()
		            .csrf().disable()
					.formLogin().loginPage("/loginstart.html").loginProcessingUrl("/login")
					.failureUrl("/loginstart.html?error=invalidlogin").defaultSuccessUrl("/", false)
					.and().logout().invalidateHttpSession(true)
					.deleteCookies("JSESSIONID").logoutSuccessUrl("/")
					//.addLogoutHandler(logoutHandler)
				    //.deleteCookies(cookieNamesToClear)
					.and().exceptionHandling().accessDeniedPage("/common/accessdenied")
					.and().authorizeRequests()
					// Specificare le url che sono soggette ad autenticazione ed autorizzazione
					.mvcMatchers("/", "/static/**", "/favicon.ico", "/corsidisponibili").permitAll()
					.mvcMatchers("/common/**").authenticated()
				    .mvcMatchers("/creatore/**").hasAuthority("CREATOR")
					.mvcMatchers("/admin/**").hasAuthority("ADMIN");
	}
	
	

}
