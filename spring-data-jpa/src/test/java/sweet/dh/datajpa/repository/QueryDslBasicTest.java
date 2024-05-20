package sweet.dh.datajpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
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
import sweet.dh.datajpa.dto.QMemberDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.QMember;
import sweet.dh.datajpa.entity.QTeam;
import sweet.dh.datajpa.entity.Team;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(false)
class QueryDslBasicTest {
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
    public void testMember() {
        QMember qMember = new QMember("m");

        List<Member> members = queryFactory.selectFrom(qMember)
                .fetch();

        System.out.println(members);
    }

    @Test
    public void basic() {
//        QMember m = new QMember("m");
        QMember m = QMember.member;

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetch();

        System.out.println(members);
    }

    @Test
    public void basicWhere() {
        QMember m = QMember.member;
        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"), m.age.eq(10))
                .fetch();

        System.out.println(members);
    }

    @Test
    public void results() {
        QMember m = QMember.member;
        //List
//        List<Member> fetch = queryFactory
//                .selectFrom(m)
//                .fetch();
        //단 건
//        Member findMember1 = queryFactory
//                .selectFrom(m)
//                .fetchOne();

        //처음 한 건 조회
//        Member findMember2 = queryFactory
//                .selectFrom(m)
//                .fetchFirst();

        //페이징에서 사용
        QueryResults<Member> results = queryFactory
                .selectFrom(m)
                .fetchResults();

        //count 쿼리로 변경
//        long count = queryFactory
//                .selectFrom(m)
//                .fetchCount();
    }

    @Test
    public void sort() {
        QMember m = QMember.member;
        List<Member> members = queryFactory
                .selectFrom(m)
                .where(m.age.goe(10))
                .orderBy(m.age.desc(), m.username.asc().nullsLast())
                .fetch();

        System.out.println(members);
    }

    @Test
    public void paging() {
        QMember m = QMember.member;
        QueryResults<Member> results = queryFactory
                .selectFrom(m)
                .orderBy(m.age.desc(), m.username.asc().nullsLast())
                .offset(1)
                .limit(3)
                .fetchResults();

        System.out.println(results.getResults());
        System.out.println(results.getTotal());
        System.out.println(results.getLimit());
        System.out.println(results.getOffset());
    }

    @Test
    public void aggregation() {
        QMember m = QMember.member;
        List<Tuple> tuples = queryFactory
                .select(
                        m.count(),
                        m.age.sum(),
                        m.age.avg(),
                        m.age.max(),
                        m.age.min()
                )
                .from(m)
                .fetch();

        System.out.println(tuples); //[[22, 561, 25.5, 40, 10]]
    }

    @Test
    public void group() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .join(m.team, t)
                .fetch();

        System.out.println(members);
    }

    @Test
    public void join() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Tuple> members = queryFactory
                .select(m.username, t.name)
                .from(m, t)
                .where(m.username.eq("member1"), t.name.eq("teamB"))
                .fetch();

        System.out.println(members);
    }

    @Test
    public void join_on() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .join(m.team, t)
                .on(t.name.eq("teamB"))
                .fetch();

        System.out.println(members);
    }

    @Test
    public void join_no_relation() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .leftJoin(t)
                .on(m.username.eq(t.name))
                .fetch();

        System.out.println(members);
    }

    @Test
    public void join_fetch() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .join(m.team, t).fetchJoin()
                .fetch();

        members.forEach(member -> {
            System.out.printf("username : %s, teamName : %s%n", member.getUsername(), member.getTeam().getName());
        });
    }

    @Test
    public void sub_query() {
        QMember m = QMember.member;
        QMember mSub = new QMember("msub");

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .where(m.age.eq(
                        JPAExpressions
                                .select(mSub.age.max())
                                .from(mSub)))
                .fetch();

    }

    @Test
    public void select_scala_subquery() {
        QMember m = QMember.member;
        QMember mSub = new QMember("msub");

        List<Tuple> tuples = queryFactory
                .select(m.username, JPAExpressions
                        .select(mSub.count())
                        .from(mSub)
                        .where(m.eq(mSub))
                )
                .from(m)
                .fetch();

        System.out.println(tuples);
    }

    @Test
    public void case_1() {
        QMember m = QMember.member;

        List<Tuple> tuples = queryFactory
                .select(
                        m.username,
                        m.age,
                        m.age.when(10).then("10살")
                                .when(20).then("20살")
                                .otherwise("기타")
                )
                .from(m)
                .fetch();

        System.out.println(tuples);
    }

    @Test
    public void case_2() {
        QMember m = QMember.member;

        List<Tuple> tuples = queryFactory
                .select(
                        m.username,
                        m.age,
                        new CaseBuilder()
                                .when(m.age.between(0, 20)).then("20살 이하")
                                .when(m.age.between(21, 30)).then("30살 이하")
                                .otherwise("30대 이후")
                )
                .from(m)
                .fetch();

        System.out.println(tuples);
    }

    @Test
    public void constant() {
        QMember m = QMember.member;
        List<Tuple> tuples = queryFactory
                .select(
                        m.username,
                        m.age,
                        m.age.stringValue().concat("_").concat(m.username)
                )
                .from(m)
                .fetch();

        tuples.forEach(data -> {
            System.out.println("===========");
            System.out.printf("username : %s \n", data.get(m.username));
            System.out.printf("age : %s \n", data.get(m.age));
            System.out.printf("age : %s \n", data.get(2, String.class));
        });
    }

    @Test
    public void dto_select_by_constructor() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<MemberDto> dtos = queryFactory
                .select(
                        Projections.constructor(
                                MemberDto.class,
                                m.id,
                                m.username,
                                m.age,
                                m.team.name
                        )
                )
                .from(m)
                .leftJoin(m.team, t)
                .fetch();

        System.out.println(dtos);
    }

    @Test
    public void dto_select_by_setter() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<MemberDto> dtos = queryFactory
                .select(
                        Projections.bean(
                                MemberDto.class,
                                m.id,
                                m.username,
                                m.age,
                                m.team.name.as("teamName")
                        )
                )
                .from(m)
                .leftJoin(m.team, t)
                .fetch();

        System.out.println(dtos);
    }

    @Test
    public void dto_select_by_fields() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<MemberDto> dtos = queryFactory
                .select(
                        Projections.fields(
                                MemberDto.class,
                                m.id,
                                m.username,
                                m.age,
                                m.team.name.as("teamName")
                        )
                )
                .from(m)
                .leftJoin(m.team, t)
                .fetch();

        System.out.println(dtos);
    }

    @Test
    public void as_expression() {
        QMember m = QMember.member;

        List<Tuple> tuples = queryFactory
                .select(
                        m.username,
                        m.age,
                        as(m.id, "userId")
                )
                .from(m)
                .fetch();

        tuples.forEach(data -> {
            System.out.println("===========");
            System.out.printf("userId : %s \n", data.get(as(m.id, "userId")));
            System.out.printf("username : %s \n", data.get(m.username));
            System.out.printf("age : %s \n", data.get(m.age));
        });
    }

    @Test
    public void dto_select_by_fields_as_alias() {
        QMember m = QMember.member;
        QTeam subTeam = QTeam.team;

        List<MemberDto> dtos = queryFactory
                .select(
                        Projections.fields(
                                MemberDto.class,
                                m.id,
                                m.username,
                                m.age,
                                as(
                                        JPAExpressions
                                                .select(subTeam.name.max())
                                                .from(subTeam)
                                                .where(m.team.eq(subTeam))
                                        , "teamName")
                        )
                )
                .from(m)
                .fetch();

        System.out.println(dtos);
    }

    @Test
    public void query_projection_dto() {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

        List<MemberDto> dtos = queryFactory
                .select(new QMemberDto(m.id, m.username, m.age, m.team.name))
                .from(m)
                .leftJoin(m.team, t)
                .fetch();

        System.out.println(dtos);
    }

    @Test
    public void boolean_builder_dynamic_query1() {
        String username = "member1";
        Integer age = 10;
        QMember m = QMember.member;

        BooleanBuilder condition = new BooleanBuilder();

        if (username != null && !username.isEmpty()) {
            condition.and(m.username.eq(username));
        }

        if (age != null && age > 0) {
            condition.and(m.age.eq(age));
        }

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .where(condition)
                .fetch();

        System.out.println(members);
    }

    @Test
    public void where_params_dynamic_query1() {
        QMember m = QMember.member;
        String username = "member1";
        Integer age = 10;

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .where(usernameEquals(username), ageEquals(age))
                .fetch();

        System.out.println(members);
    }

    private BooleanExpression usernameEquals(String username) {
        return username != null && !username.isEmpty() ? QMember.member.username.eq(username) : null;
    }
    private BooleanExpression ageEquals(Integer age) {
        return age != null && age > 0 ? QMember.member.age.eq(age) : null;
    }


    @Test
    public void bulk_update() {
        QMember m = QMember.member;
        long rows = queryFactory
                .update(m)
                .set(m.username, m.username.concat("_").concat(m.id.stringValue()))
                .where(m.age.gt(30))
                .execute();

        //만약 update 로직 앞쪽에 find() 또는 save() 하는 로직이 있었다면
        //영속성 컨텍스트에 이미 엔티티가 있기 때문에 한번 비워줘야함.
        em.flush();
        em.clear();

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .fetch();

        System.out.println(members);
    }

    @Test
    public void bulk_delete() {
        QMember m = QMember.member;
        long rows = queryFactory
                .delete(m)
                .where(m.age.gt(20))
                .execute();

        em.flush();
        em.clear();

        List<Member> members = queryFactory
                .select(m)
                .from(m)
                .fetch();

        System.out.println(members);
    }

    @Test
    public void sqlFunction() {
        QMember m = QMember.member;

        List<Tuple> members = queryFactory
                .select(
                        m.username,
                        m.age,
                        Expressions.stringTemplate("function('replace', {0}, {1}, {2})", m.username, "member", "mem")
                )
                .from(m)
                .fetch();

        System.out.println(members);
    }

    @Test
    public void sqlFunction2() {
        QMember m = QMember.member;

        List<Tuple> members = queryFactory
                .select(
                        m.username,
                        m.age
                )
                .from(m)
                .where(m.username.lower().eq("member1"))
//                .where(m.username.eq(Expressions.stringTemplate("function('lower', {0})", "MEMBER1")))
//                .where(Expressions.stringTemplate("function('lower', {0})", m.username).eq("member1")) //반대로도 가능
                .fetch();

        System.out.println(members);
    }
}