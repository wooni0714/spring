## CORS(Cross-Origin Resource Sharing)

다른 출처(Origin) 간의 자원을 공유하는 정책으로 다른 도메인에 있는 웹페이지가 한 도메인의 리소스(API 등)를 요청할 수 있는 방법을 제어하기 위해 웹 브라우저에서 구현하는 보안 기능.

즉, CORS를 설정 한다는 것은 출처가 다른 서버간의 리소스를 공유하는 것이다.

### **SOP(Same-origin policy)**

웹 보안 정책 중 하나로 웹 브라우저에서 서로 다른 출처(origin) 간의 상호작용을 제한하여 악의적인 웹사이트가 사용자의 정보를 탈취하거나 민감한 데이터에 접근하는 것을 방지.

<aside>
💡 SOP가 서로 다른 출처일 때 리소스 요청과 응답을 차단하는 정책이라면, CORS는 반대로 서로 다른 출처라도 리소스 요청, 응답을 허용할 수 있도록 하는 정책이다.

  
</aside>

### 출처(Origin)란?

`Protocol + Host + Port` 부분을 합친 것을 말함. 

Protocol : `https://` 

HostName : `www.myshop.com`

Port : `:8080`

![aa](https://github.com/user-attachments/assets/4284e0f3-fb56-4cec-b7ab-8c204df1ea45)

### CORS 설정 방법

- **WebMvcConfigurer 사용하여 서버 전역에 설정**

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대해 CORS 설정
                .allowedOrigins("http://localhost:8080") //http://localhost:9090에서 오는 요청만 허용
                .allowedMethods("*") //모든 HTTP 메서드(GET, POST, PUT, DELETE 등)를 허용
                .allowedHeaders("*") //모든 헤더를 허용
                .exposedHeaders("Authorization") //서버가 응답을 보낼 때 클라이언트가 접근할 수 있는 응답 헤더들을 지정
                .allowCredentials(true) //자격 증명(쿠키, 인증 관련 헤더 등)을 사용할 수 있도록 설정
                .maxAge(3600); //캐시 시간을 초 단위로 지정
    }
```

- **`@CrossOrigin` 어노테이션 설정**
    
    — 특정 Controller 및 Method에 설정
    

```java
@RestController
public class CorsController {

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("hi")
    public String hi() {
        return "hi";
    }
}
```

- **CORS 필터 설정**
    
    — 더 세밀한 설정이 필요할 때 유용
    

```java
@Configuration
public class CorsFilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
```
