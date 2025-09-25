package org.affidtech.musdict.musdictapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class APISecurityConfig {
	
	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.cors {  }
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
	
	 @Bean
	fun corsConfigurationSource(): CorsConfigurationSource {
		val cors = CorsConfiguration().apply {
			allowedOrigins = listOf("*")
			allowedMethods = listOf("GET","POST","PUT","PATCH","DELETE","OPTIONS")
			allowedHeaders = listOf("Authorization","Content-Type","Accept","X-Requested-With")
			exposedHeaders = listOf("Location")
			maxAge = 3600
		}
		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", cors)
		return source
	}
}