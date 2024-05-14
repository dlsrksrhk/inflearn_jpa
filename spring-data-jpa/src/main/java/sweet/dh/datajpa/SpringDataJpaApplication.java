package sweet.dh.datajpa;

import com.p6spy.engine.spy.P6SpyOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.Team;
import sweet.dh.datajpa.repository.MemberRepository;
import sweet.dh.datajpa.repository.TeamRepository;
import sweet.dh.datajpa.setting.CustomP6spySqlFormat;

import java.util.Optional;

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
    public ApplicationRunner applicationRunner() {
        return args -> {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            teamRepository.save(teamA);
            teamRepository.save(teamB);

            Member member1 = new Member("member1", 10, teamA);
            Member member2 = new Member("member2", 20, teamA);
            Member member3 = new Member("member3", 30, teamB);
            Member member4 = new Member("member4", 40, teamB);
            memberRepository.save(member1);
            memberRepository.save(member2);
            memberRepository.save(member3);
            memberRepository.save(member4);

            memberRepository.save(new Member("member5", 25, teamB));
            memberRepository.save(new Member("member6", 26, teamB));
            memberRepository.save(new Member("member7", 27, teamB));
            memberRepository.save(new Member("member8", 28, teamB));
            memberRepository.save(new Member("member9", 29, teamA));
            memberRepository.save(new Member("member10", 30, teamA));
            memberRepository.save(new Member("member11", 31, teamA));
        };
    }


    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

}
