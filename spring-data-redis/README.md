# Redis

## **Redis 란?**

- 메모리에 저장하는 Key-value 기반의 No-sql  DBMS.
- Key - Value 구조이기 때문에 쿼리를 작성할 필요가 없다.
- PUB/SUB 형태의 기능을 제공하여 메세지를 전달할 수 도 있다.
- Redis는 주로 보조 데이터 저장소로 사용한다.
- 영구적으로 디스크에 저장할 수 있는 백업 기능을 제공하여 주 저장소로도 사용할 수 있다.
- Redis는 싱글스레드이다.
- Redis는 캐싱 뿐만 아니라 임시작업 큐, 실시간 채팅, 메시지 브로커 등으로 사용할 수 있다.

---

## **Redis와 캐시의 관계**

캐시는 데이터나 계산 결과를 미리 저장하여 빠른 액세스와 높은 성능을 제공하며,

캐시를 구현하는 방법으로 Redis, Local Memory, Memcached 등이 있다.

따라서 Redis는 캐시를 구현하는 방법중 하나이다.

메모리 특성상 저장된 데이터는 휘발성으로 사라질 수도 있지만, 레디스는 데이터에 영속성을 제공하여 

서버가 다운이 되어도 디스크에 저장해놓았던 메모리를 다시 가져올 수 있다.

RDB, AOF 방식으로 백업 할 수있다.

---

## **RDB와 AOF**

Redis는 서버가 시작될 때 데이터를 읽는데 어떤 데이터파일을 읽을지는 redis.conf → appendonlyfile 설정을 따름.

<img width="530" alt="스크린샷 2024-09-19 오후 3 56 54" src="https://github.com/user-attachments/assets/1877444e-686f-4b66-9354-2c52576f9a40">

```bash
appendonly yes #AOF 파일을 읽음
appendonly no #(Default no) RDB 파일을 읽음
```

### **RDB**

RDB(redis database) 메모리에 있는 데이터 전체에서 스냅샷을 작성하고, 하드 디스크에 바이너리 파일로  저장하는 방식

- 특정 시간마다 여러개의 스냅샷을 생성 -> 데이터 복원을 원하면 스냅샷을 그대로 로딩하기만 하면 됨.
- RDB로 생성된 스냅샷은 dump.rdb 이름으로 생성
- 스냅샷 이후에 변경된 데이터에 대해서는 복구 불가 (데이터유실 loss)
- CPU, 메모리 등 자원을 많이 사용함.

### RDB 백업 및 복구

**수동 백업**

- `SAVE` : 순간적으로 redis 동작을 중지시키고, 현재 Redis 서버의 스냅샷을 RDB파일로 저장함. (blocking 방식)
- `BGSAVE` : redis의 동작을 중지시키지 않고 백그라운드에서 RDB파일을 생성함. (non-blocking 방식)

**RDB 백업 기본 설정**

- redis.conf 파일에 별도 설정을 하지 않은 경우 아래와 같이 기본 설정이 되어있음.
    
<img width="225" alt="스크린샷 2024-09-19 오후 3 14 30" src="https://github.com/user-attachments/assets/65ddf1a7-c299-480d-a27d-38d6882b188c">
    
- 3600초 안에 1개 이상의 데이터가 변경되면 저장
- 300초 안에 100개 이상의 데이터가 변경되면 저장
- 60초 안에 10000개 이상의 데이터가 변경되면 저장

**RDB 복구**

- dump.rdb 파일을 Redis data dir에 넣고 재시작 해주면 됨.
- 위치는 redis-cli에서 config get dir 로 찾음.

### **AOF**

AOF(Append Only file) : 데이터가 변경되는 이벤트가 발생하면 이를 모두 로그에 저장하는 방식

- 데이터 수정, 생성, 삭제 이벤트를 초 단위로 로그 파일에 작성
- 모든 데이터의 기록을 보관하고 있어, 최신데이터로 백업가능하며  RDB 방식 대비 안정적으로 백업 가능
- 읽기모드이므로  RDB 방식 보다 백업 파일이 손상될 위험이 적음
- RDB 방식 보다 로딩 속도가 느리고 파일 크기가 큼

### AOF 백업 및 복구

### AOF 백업

AOF 파일명 지정

```bash
appendfilename "appendonly.aof"
```

AOF에 기록되는 시점 지정

```bash
appendfsync always
appendfsync everysec
appendfsync no
```

- `always` : 모든 쓰기 작업이 발생할 때마다 AOF 파일에 즉시 기록
    
    — 장단점 :  데이터 유실은 거의 없고, 성능 저하가 발생할 수 있음.
    
- `everysec` : 매 초마다 AOF 파일에 기록.
    
    — 장단점 : 성능과 데이터 안전성의 균형을 맞출 수 있음. 기본값
    
- `no` : Redis가 AOF 파일을 자동으로 기록하지 않고, 운영 체제의 버퍼에 맡김 (기본적으로 리눅스는 30초)
    
    — 장단점 : 성능이 가장 좋으나 영 체제가 버퍼에 있는 데이터를 디스크에 기록하지 않으면 데이터 유실 가능성이 큼.
    

AOF Rewrite 설정

- AOF Rewrite는 특정 조건 일 때 설정에 따라 현재 메모리에 있는 데이터를 기반으로 새로 압축된 AOF 파일을 생성하여 불필요한 명령이나 중복 명령을 제거하고 파일 크기를 줄임.

```bash
#AOF 파일 크기가 마지막 Rewrite 또는 Redis 시작 이후 처음으로 생성된 AOF 파일 크기보다 100%(2배) 이상 증가했을 때, 자동으로 AOF Rewrite가 트리거
#처음 AOF 파일이 100MB였다면, 파일 크기가 200MB에 도달할 때 AOF Rewrite가 자동으로 실행
auto-aof-rewrite-percentage 100

# AOF Rewrite가 자동으로 발생하기 위해 최소한의 파일 크기를 지정
# 파일 크기가 최소 64MB 이상일 때만 Rewrite 조건이 적용
auto-aof-rewrite-min-size 64mb
```

### **AOF 복구**

- Ex) flushall 명령어로 모든 데이터를 삭제 했을시 복구

<img width="186" alt="스크린샷 2024-09-20 오전 11 03 16" src="https://github.com/user-attachments/assets/5271dced-d38a-45b3-b3b4-ea5e83f82c1c">

- appendonly.aof 파일을 열어 flushall 명령어를 삭제 후 저장
    
    — Redis CLI에서 현재 설정된 경로를 확인 명령어
    
    ```bash
    CONFIG GET dir
    ```
    

<img width="131" alt="스크린샷 2024-09-20 오전 11 04 05" src="https://github.com/user-attachments/assets/0f1e759d-55f3-481c-b288-95f3fba0c3c6">

- redis-server 재실행 후 다시 조회해보면 데이터 백업 완료.
<img width="164" alt="스크린샷 2024-09-20 오전 11 05 38" src="https://github.com/user-attachments/assets/d38615b5-1626-440c-88d5-bf091bed44ab">

## **RDB와 AOF 중 어느 방법이 좋을까 ?**

보통은 AOF 기법을 사용하나 RDB보다 높은 품질의 데이터 백업이 가능하고 

성능 또한 RDB보다 많이 뒤처지지 않는다.
AOF를 Default로 하되 RDB를 옵션으로 하여 AOF + RDB를 같이사용하는것이 좋을듯 하다.

```bash
# AOF 설정
appendonly yes
appendfilename "appendonly.aof"
appendfsync everysec

# RDB 설정 (주석처리하여 비활성화하거나, 원하는 주기로 조정)
save 900 1
save 300 10
save 60 10000

# AOF 및 RDB 혼합 모드
aof-use-rdb-preamble yes

# RDB 오류 시 쓰기 중단 방지
stop-writes-on-bgsave-error no
```

---

## StringRedisTemplate 과 CacheManager

- `StringRedisTemplate`
    - 단순한 키-값 데이터 저장 및 조회. 실시간으로 데이터를 저장 및 조회에 적합
    - opsForValue(): 문자열 작업에 사용됩니다. 예를 들어 간단한 키-값 쌍을 저장하고 검색
    - opsForList(): Redis에서 목록 작업을 수행
    - opsForSet(), opsForHash() 등도 Redis에서 세트와 해시를 처리하는 데 사용
- `CacheManager`
    - 메소드 결과를 캐싱하여 성능 최적화. 반복적으로 조회되는 데이터의 성능 최적화에 적합
    - getCache(String name): 이름으로 특정 캐시를 검색
    - getCacheNames(): 이 CacheManager가 관리하는 모든 캐시 이름을 반환
