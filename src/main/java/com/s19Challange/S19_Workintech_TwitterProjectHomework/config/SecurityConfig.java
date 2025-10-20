package com.s19Challange.S19_Workintech_TwitterProjectHomework.config;

import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.AuthEntryPointJwt;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.securityutil.AuthTokenFilter;
import com.s19Challange.S19_Workintech_TwitterProjectHomework.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService)
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }


    /*
     @Bean anotasyonu, bu metodu Spring’in yönetmesi için işaretler.
    SecurityFilterChain, Spring Security’nin HTTP isteklerini nasıl işleyeceğini belirleyen güvenlik filtresidir.
    Bu yapılandırma, uygulamanın güvenlik kurallarını belirler (kimlik doğrulama, yetkilendirme, CORS, CSRF vb.).
    Spring Security’nin HttpSecurity nesnesini kullanarak güvenlik ayarlarını yapılandırıyoruz.
    Metod SecurityFilterChain nesnesi döndürerek bu ayarları uygular.
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        //CSRF korumasını devre dışı bırakıyor.
        //CSRF saldırılarına karşı ek güvenlik sağlar ama genellikle REST API’lerde gerekmez.
        //Çünkü REST API’lerde genellikle JWT veya Session Cookie kullanılır ve CSRF koruması gerekli olmaz.
        //Eğer tarayıcıdan gelen form verileri ile çalışan bir uygulama olsaydı, CSRF koruması açık olmalıydı.
        return httpSecurity.csrf(csrf -> csrf.disable())
                // CORS (Cross-Origin Resource Sharing) Ayarlarını Etkinleştirme
                //CORS, frontend ile backend farklı portlarda (localhost:3200 & localhost:3000 gibi)
                // çalıştığında gerekli olan bir mekanizmadır.
                //Eğer CorsConfig sınıfında CORS ayarı yaptıysan, burada .configure(httpSecurity) ile o ayarı uyguluyoruz.
                .cors(cors -> cors.configure(httpSecurity))
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("/auth/register/**").permitAll();
                    auth.requestMatchers("/auth/login/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                //Spring Security’nin oturum yönetimi kurallarını belirler.
                //SessionCreationPolicy.STATELESS, Hiçbir şekilde session oluşturmaz veya kullanmaz.
                //Her istek bağımsız olarak değerlendirilir (tam JWT uyumlu).
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //HTTP Basic Authentication’ı etkinleştirir.
                //Kullanıcı adı ve şifreyi Authorization Header üzerinden göndermeyi sağlar.
                //Eğer sadece session tabanlı authentication (JSESSIONID) kullanıyorsan, bunu kaldırabilirsin.
                .httpBasic(Customizer.withDefaults())
                //Bu yapılandırmayı tamamlar ve Spring Security’nin filtre zincirine ekler.
                .addFilterBefore(authenticationJwtTokenFilter(),  UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
