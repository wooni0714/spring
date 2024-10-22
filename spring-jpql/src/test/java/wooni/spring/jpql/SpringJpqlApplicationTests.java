package wooni.spring.jpql;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import wooni.spring.jpql.entity.Hello;
import wooni.spring.jpql.entity.QHello;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Commit
class SpringQuerydslApplicationTests {

    @Autowired
    EntityManager entityManager;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        entityManager.persist(hello);

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QHello qHello = QHello.hello;

        Hello result = queryFactory
                .selectFrom(qHello)
                .fetchOne();

        assertThat(result).isEqualTo(hello);
        assert result != null;
        assertThat(result.getId()).isEqualTo(hello.getId());
    }
}
