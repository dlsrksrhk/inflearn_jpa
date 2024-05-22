package sweet.dh.datajpa.setting;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.Team;
import sweet.dh.datajpa.repository.MemberRepository;
import sweet.dh.datajpa.repository.TeamRepository;

import java.util.stream.IntStream;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class DataInit {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @PostConstruct
    public void initMemberAndTeam() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        IntStream.rangeClosed(0, 100)
                .forEach(i -> {
                    Team team = i % 2 == 0 ? teamA : teamB;
                    memberRepository.save(new Member("member" + i, i, team));
                });
    }
}
