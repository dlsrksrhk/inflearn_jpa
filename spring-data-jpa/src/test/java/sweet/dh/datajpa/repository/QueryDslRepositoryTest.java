package sweet.dh.datajpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import sweet.dh.datajpa.dto.MemberSearchCondition;
import sweet.dh.datajpa.dto.MemberTeamDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.Team;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.as;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void saveMemberAndTeam() {
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
        memberRepository.save(new Member("member6", 20, teamB));
        memberRepository.save(new Member("member7", 20, teamB));
        memberRepository.save(new Member("member8", 30, teamB));
        memberRepository.save(new Member("member9", 30, teamA));
        memberRepository.save(new Member("member10", 30, teamA));
        memberRepository.save(new Member("member11", 10, teamA));
        em.flush();
        em.clear();
    }

    @Test
    public void sqlFunction2() {
        System.out.println(memberRepository.findByUsername("member1"));
    }

    @Test
    public void searchByBuilder() {
        MemberSearchCondition condition = MemberSearchCondition
                .builder()
                .username("member")
                .teamName("team")
                .ageGoe(10)
                .ageLoe(40)
                .build();

        List<MemberTeamDto> memberTeamDtos = memberRepository.searchByBuilder(condition);
        memberTeamDtos.forEach(System.out::println);
    }

    @Test
    public void search() {
        MemberSearchCondition condition = MemberSearchCondition
                .builder()
                .username("member")
                .teamName("team")
                .ageGoe(10)
                .ageLoe(40)
                .build();

        List<MemberTeamDto> memberTeamDtos = memberRepository.search(condition);
        memberTeamDtos.forEach(System.out::println);
    }

    @Test
    public void searchPagingSimple() {
        MemberSearchCondition condition = MemberSearchCondition
                .builder()
                .username("member")
                .teamName("team")
                .ageGoe(10)
                .ageLoe(40)
                .build();

        PageRequest pageable = PageRequest.of(0, 3);

        Page<MemberTeamDto> pageMemberDto = memberRepository.searchPagingSimple(condition, pageable);
        System.out.println(pageMemberDto.getSize());
        System.out.println(pageMemberDto.getTotalElements());
        System.out.println(pageMemberDto.getContent());
    }

    @Test
    public void searchPagingComplex() {
        MemberSearchCondition condition = MemberSearchCondition
                .builder()
                .username("member")
                .teamName("team")
                .ageGoe(10)
                .ageLoe(40)
                .build();

        PageRequest pageable = PageRequest.of(0, 3);

        Page<MemberTeamDto> pageMemberDto = memberRepository.searchPagingComplex(condition, pageable);
        System.out.println(pageMemberDto.getSize());
        System.out.println(pageMemberDto.getTotalElements());
        System.out.println(pageMemberDto.getContent());
    }


}