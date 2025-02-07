package com.wsystems.residentstore.configurations;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.shaded.jcip.Immutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AuthorizationServerConfig {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationServerConfig.class);
	@Value("${security.client-id}")
	private String clientId;

	@Value("${security.client-secret}")
	private String clientSecret;

	@Value("${security.jwt.duration}")
	private Integer jwtDurationSeconds;

	@Value("${cors.origins}")
	private String corsOrigins;

	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${jwt.public.key}")
	private RSAPublicKey publicKey;
	@Value("${jwt.private.key}")
	private RSAPrivateKey privateKey;



	//Customizar a segurança do projeto
	@Bean
	public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

		http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/login").permitAll()
						.anyRequest().authenticated()) //Essa linha diz que toda requisição deve ser autenticada
		        .csrf(csrf -> csrf.disable()) //Desabilitando proteção de vulnerabilidade
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) //Configurando o JWT no oauth2
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Indicando que não vamos guardar nada em sessão, será tudo STATELESS

				.securityMatcher(PathRequest.toH2Console()).csrf(csrf -> csrf.disable())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

		return http.build();
	}

	//Decoder da chave JWT, aqui que será feito a descriptografia da chave na requisição
	@Bean
	public JwtDecoder jwtDecoder(){
		return NimbusJwtDecoder.withPublicKey(publicKey).build();
	}


	//Encoder que irá encriptografar nossa chave JWT
	@Bean
	public JwtEncoder jwtEncoder(){
		JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
		var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}


	//Metodo de criptografia
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
