package sweet.dh.datajpa;

import com.p6spy.engine.spy.P6SpyOptions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import sweet.dh.datajpa.setting.CustomP6spySqlFormat;

import java.util.Optional;

//@EnableJpaRepositories(basePackages = "sweet.dh.datajpa",
//        repositoryImplementationPostfix = "Custom")
@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class SpringDataJpaApplication {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(CustomP6spySqlFormat.class.getName());
    }

    @Bean
    public AuditorAware<String> auditProvider() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                return Optional.of(Thread.currentThread().getName());
            }
        };
    }

    @Bean
    JPAQueryFactory jpaQueryFactory(@Autowired EntityManager em) {
        return new JPAQueryFactory(em);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

}
