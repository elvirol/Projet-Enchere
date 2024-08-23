package fr.eni.enchere.configuration.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class EnchereSecurityConfig {

//	@Bean
//	public UserDetailsService users() {
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		String password = encoder.encode("Passw0rd"); // le mot de passe en dur
//		System.out.println("pwd = " + password);
//
//		UserDetails user = User.builder().username("abaille@campus-eni.fr").password(password)
//				.roles("EMPLOYE", "FORMATEUR").build();
//
//		UserDetails user2 = User.builder().username("sdautais@campus-eni.fr").password(password).roles("EMPLOYE")
//				.build();
//
//		UserDetails admin = User.builder().username("sgobin@campus-eni.fr").password(password)
//				.roles("EMPLOYE", "FORMATEUR", "ADMIN").build();
//		return new InMemoryUserDetailsManager(user, user2, admin);
//	}

	@Bean
	UserDetailsManager users(DataSource dataSource) {

		JdbcUserDetailsManagerEni users = new JdbcUserDetailsManagerEni(dataSource);
		users.setUsersByUsernameQuery(
				"select pseudo,  mot_de_passe,  case est_actif when 1 then 'true' when 0 then 'false' end as enabled from utilisateurs where pseudo = ? OR email = ?");
		users.setAuthoritiesByUsernameQuery(
				"select pseudo, 'ROLE_ADMIN' as role from utilisateurs where pseudo = ? OR email = ?");

		return users;
	}

	@Bean
	SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/encheres").permitAll()
				.requestMatchers("/css/**").permitAll()
				.requestMatchers("/images/**").permitAll()
				.requestMatchers("/").permitAll()
				.requestMatchers("/monCompte/creer").permitAll()
				.anyRequest().authenticated());

		// formulaire de connexion par defaut
		http.formLogin(form -> {
			form.loginPage("/login"); // url permettant d'afficher la page de login
			form.permitAll();
			form.defaultSuccessUrl("/session"); // url appellée si connexion ok
		});

		http.logout(form -> {
			form.invalidateHttpSession(true);
			form.clearAuthentication(true);
			form.deleteCookies("JSESSIONID");
			form.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
			form.logoutSuccessUrl("/").permitAll();
		});
		
		http.rememberMe(rememberMe-> rememberMe.authenticationSuccessHandler(myAuthenticationSuccessHandler()));
		
		return http.build();
	}

	@Bean
	AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
	    return new MySimpleUrlAuthenticationSuccessHandler();
	}
	
}
