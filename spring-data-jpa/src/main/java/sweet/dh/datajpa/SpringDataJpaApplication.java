package sweet.dh.datajpa;

import com.p6spy.engine.spy.P6SpyOptions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.Team;
import sweet.dh.datajpa.repository.MemberRepository;
import sweet.dh.datajpa.repository.TeamRepository;
import sweet.dh.datajpa.setting.CustomP6spySqlFormat;

import java.util.Optional;
import java.util.stream.IntStream;

//@EnableJpaRepositories(basePackages = "sweet.dh.datajpa",
//        repositoryImplementationPostfix = "Custom")
@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor
public class SpringDataJpaApplication {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

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

    @Profile("!test")
    @Bean
    public ApplicationRunner applicationRunner() {
        //유닛 테스트시에 여기가 실행돼버려서 중복 데이터 입력됨..
        return args -> {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            teamRepository.save(teamA);
            teamRepository.save(teamB);

            IntStream.rangeClosed(0, 100)
                    .forEach(i -> {
                        Team team = i % 2 == 0 ? teamA : teamB;
                        memberRepository.save(new Member("member" + i, i, team));
                    });
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

}
