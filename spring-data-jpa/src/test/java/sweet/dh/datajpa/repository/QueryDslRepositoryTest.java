package sweet.dh.datajpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sweet.dh.datajpa.dto.MemberDto;
import sweet.dh.datajpa.dto.MemberSearchCondition;
import sweet.dh.datajpa.dto.MemberTeamDto;
import sweet.dh.datajpa.dto.QMemberDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.QMember;
import sweet.dh.datajpa.entity.QTeam;
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
    MemberDslRepository memberDslRepository;

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
        System.out.println(memberDslRepository.findByUsername("member1"));
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

        List<MemberTeamDto> memberTeamDtos = memberDslRepository.searchByBuilder(condition);
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

        List<MemberTeamDto> memberTeamDtos = memberDslRepository.search(condition);
        memberTeamDtos.forEach(System.out::println);
    }

}