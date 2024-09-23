# HTTP Client

### RestTemplate

Spring Framework3 에서 제공되는 Http Client 이다. 

멀티스레드 방식을 사용하고 있으며, Blocking 방식을 사용한다.

RestTemplate Ex)

```java
RestTemplate restTemplate = new RestTemplate();
```

RestTemplate은 동기식으로 동작하기 때문에, 응답을 받을 때까지 대기하면서 스레드를 차단한다.

(하나의 스레드당 하나의 요청 방식으로 클라이언트가 응답을 받을 때까지 스레드는 Block 상태이므로 응답시간에 영향을 미친다.)

### WebClient

Spring Framework5 이후부터 제공되는 fluent한 API를 지원하는 리액티브 기반의 Http Client 이다.

**RestTemplate의 대안으로 제공되며, WebClient를 사용하도록 권장한다.**

Non-Blocking I/O 모델을 기반으로 비동기 및 리액티브 스트림 처리를 지원하여, 여러 요청을 동시에 수행하고 응답을 비동기식으로 처리할 수 있다.

Mono와 Flux의 결과값을 다루기 위해서는 `subscribe`을 통해 콜백 함수로 받아 사용하거나, `map`을 사용해야 한다. 

<aside>
💡 Mono : 0 ~ 1 개의 결과를 처리하기위한 Reactor 객체이다.

💡 Flux : 0 ~ N 개의 결과를 처리하기 위한 Reactor 객체이다

</aside>

.`block`이라는 메소드를 사용하면 원하는 객체 그대로 받을 수 있지만, WebClient 안티 패턴이라고 한다.

Webclient Ex)

```java
implementation 'org.springframework.boot:spring-boot-starter-webflux'
```

```java
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("Accept", "application/json")
                .build();
    }
```

```java
    public Mono<String> getData(String num) {
        return webClient.get()
                .uri("/posts/{num}", num)
                .retrieve()
                .bodyToMono(String.class)
    }
    
     public Flux<String> getAllData() {
        return webClient.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(String.class)
                .doOnError(error -> log.error("Error : {}", error.getMessage()));
    }
```

### RestClient

Spring Framework6.1부터 새로 나온 동기식 RestClient이다.

`Mono`나 `Flux`를 지원하지 않는다.

RestTemplate과 동일 기반 기술을 바탕으로 fluent한 API를 제공한다.

RestClient는 WebFlux의 의존성도 필요없고, WebClient와 유사한 방식으로 사용가능하다.

**RestClient를 통한 GET/POST 요청**

```java
@Bean
public RestClient restClient() {
    return RestClient.builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .defaultHeader("Accept", "application/json")
            .build();
    }
```

GET

```java
String result = restClient.get()
  .uri("https://jsonplaceholder.typicode.com")
  .retrieve()
  .body(String.class);
  // .toEntity(String.class); ResponseEntity로도 반환할 수있다.
```

POST

```java
ResponseEntity response = restClient.post()
  .uri("https://jsonplaceholder.typicode.com")
  .contentType(APPLICATION_JSON)
  .body(pet)
  .retrieve()
  .toBodilessEntity();
```

Error Handlering

```java
String result = restClient.get()
  .uri("https://jsonplaceholder.typicode.com")
  .retrieve()
  .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
      throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders())
  })
  .body(String.class);
```

### Http Interface Client

Spring 6와 Spring Boot 3에서 새로 도입된 HTTP 클라이언트 기능으로, 인터페이스에 직접 HTTP 요청을 정의하고, Spring이 이를 구현해주는 방식.

`RestTemplate`이나 `WebClient`와 달리, 코드가 더 간결해짐.

**@HttpExchange**

- 인터페이스 내의 각 메서드가 HTTP 요청에 해당하는 인터페이스에서 사용.
- HTTP 요청에 url, accept, headers 등을 설정할 수 있음.
- @GetExchange, @PostExchange, @PatchExchange 등 URI 경로 및 헤더를 직접 지정하여 사용할 수 있음.
- url 동적경로에는 /{id} 와 같이 사용할 수 있음.

```java
    @Bean
    public HttpInterfaceClient httpClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) ->
                        log.error("status : {}", response.getStatusCode())
                        ))
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(HttpInterfaceClient.class);
    }
```

```java
@HttpExchange("posts")
public interface ExampleClient {

    @GetExchange("/{id}")
    String getData(@PathVariable String id);
}
```

```java
@Service
@RequiredArgsConstructor
public class HttpInterfaceService{

    private final HttpInterfaceClient httpInterfaceClient;

    public String getData(String id) {
        return httpInterfaceClient.getData(id);
    }
}
```
