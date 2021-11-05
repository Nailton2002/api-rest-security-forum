package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

@EnableWebSecurity
@Configuration
@Profile(value = {"prod", "test"})
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	// CONFIGURAÇÃO DE SEGURAÇA NA PARTE DE AUTENTICAÇÃO
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	// CONFIGURAÇÃO DE SEGURAÇA NA PARTE DE AUTORIZAÇÃO
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(HttpMethod.GET,"/topicos").permitAll()//SÓ É PERMITOS OS METODOS GET AQUI
		                        .antMatchers(HttpMethod.GET,"/topicos/*").permitAll()//AQUI É PERMITOS QUALQUER COISA DEPOIS DO TOPICOS
		                        .antMatchers(HttpMethod.POST,"/auth").permitAll()//AQUI PERMITi FAZER CADASTRO
		                        .antMatchers(HttpMethod.GET,"/actuator/**").permitAll()//PERMIALL ´SO PRA TESTES SE FOR PARA PRODUÇÃO MUDA
		                        .antMatchers(HttpMethod.DELETE,"/topicos/*").hasRole("MODERADOR")
		                        .anyRequest().authenticated()//SÓ QUEM ESTA AUTENTICADO PODE ENTRA NESTES ENDPOINTS
		                        .and().csrf().disable()
		                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//NÃO CRIARA SESSÃO
		                        .and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class);	}
	
	// CONFIGURAÇÃO DE SEGURAÇA NA PARTE ESTATICA COMO(HTML, CSS, JS E IMAGEN E ETC)
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
		}
	
}
