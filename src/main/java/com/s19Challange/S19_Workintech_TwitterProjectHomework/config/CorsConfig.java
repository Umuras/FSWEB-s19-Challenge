package com.s19Challange.S19_Workintech_TwitterProjectHomework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Bu sınıfın Srping yapılandırma sınıfı olduğunu belirttik Configuration anatosyonu ile.
//Spring Boot, uygulama başladığında bu sınıfı otomatik olarak algılar ve içindeki yapılandırmaları uygular.
@Configuration
//Spring MVC'nin gelişmiş özelliklerini etkinleştirir.
//Ancak Srping Boot projelerinde genellikle gerekli değildir, çünkü Spring Boot zaten Web MVC yapılandırmasını
//kendisi yönetir. Eğer manuel MVC yapılandırması yapmayacaksak, bu anatosyonu kullanmamıza gerek yok.
@EnableWebMvc
//WebMvcConfigurer ile Spring MVC yapılandırmalarını değiştirmek için kullanırız.
//Burada, CORS ayarlarını özelliştirmek için addCorsMapping methodunu kullanıyoruz.
public class CorsConfig implements WebMvcConfigurer {
    //Spring Boot'un CORS(Cross-Origin Resource Sharing) ayarlarını değiştirmek için kullanılır.
    //CorseRegistry nesnesi, hangi kaynaklara izin verileceğini belirlememizi sağlar.
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        //Bu şekilde tüm urlere CORS ayarı uygula diyoruz. API'deki tüm urller için
        registry.addMapping("/**")
                //Hangi frontend domainine izin verildiğini belirtme, Sadece http://localhost:3200 adresinden gelen
                //istekleri kabul eder. Bu React veya Angular frontendinin çalıştığı port ile eşleşmelidir.
                //Eğer tüm domainlere izin vermek istersen .allowedOriginPatterns("*") yazacaksın.
                .allowedOrigins("http://localhost:3200")
                //İzin verilen http metodları
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                //Çerezlerin (Cookies) Gönderilmesine İzin Verme
                //Frontend’in, backend’e çerez göndermesine ve backend’den çerez almasına izin verir.
                //Özellikle JSESSIONID gibi oturum yönetimi çerezlerinin gönderilebilmesi için gereklidir.
                //Eğer bu ayar false olursa, tarayıcı backend’den gelen JSESSIONID çerezini reddeder ve oturum yönetimi çalışmaz.
                //Önemli not: .allowCredentials(true) kullanıyorsan, allowedOrigins("*") kullanamzyız. Bunun yerine belirli
                //domainleri eklemelisin.
                .allowCredentials(true)
                //CORS yanıtlarını önbelleğe Alma süresi
                //Tarayıcının, CORS izinlerini ne kadar süre önbelleğe alacağını belirler.
                //3600 saniye (1 saat) boyunca tarayıcı tekrar tekrar OPTIONS isteği atmak yerine önbellekten kullanır.
                .maxAge(3600);
    }

    /*
    CORS Yapılandırmasını Neden Yapıyoruz?

    CORS (Cross-Origin Resource Sharing, Çapraz Kaynak Paylaşımı), tarayıcıların bir domain (köken) üzerinden başka
    bir domain’e API isteği yapmasını güvenlik nedeniyle engelleyen bir mekanizmadır.
🚨  Problem:
    Varsayılan olarak tarayıcılar, farklı bir kökenden (origin) gelen API isteklerini engeller.

    Örneğin, frontend (http://localhost:3200) üzerinden backend (http://localhost:3000) API’sine istek attığında
    CORS hatası alırsın.

    Backend’e CORS yapılandırması ekleyerek, bu engeli kaldırırız.

     */
}
