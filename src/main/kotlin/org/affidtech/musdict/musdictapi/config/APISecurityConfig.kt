package org.affidtech.musdict.musdictapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
class APISecurityConfig {
	
	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { csrf ->
				csrf.disable()
			}
			.authorizeHttpRequests { auth ->
				auth
					.requestMatchers("/api/**").permitAll()
					.requestMatchers("/error").permitAll()
					.requestMatchers("/files/**").permitAll()
					.anyRequest().denyAll()
			}
			.httpBasic { basic ->
				basic.authenticationEntryPoint { _, response, _ ->
					response.sendError(401, "Unauthorized")
				}
			}
		return http.build()
	}
}