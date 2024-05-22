package sweet.dh.datajpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import sweet.dh.datajpa.dto.MemberSearchCondition;
import sweet.dh.datajpa.dto.MemberTeamDto;
import sweet.dh.datajpa.dto.QMemberTeamDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.QMember;
import sweet.dh.datajpa.entity.QTeam;

import java.util.List;

@RequiredArgsConstructor
public class MemberDslRepositoryImpl implements MemberDslRepository {
    private final JPAQueryFactory queryFactory;
    private final QMember m = QMember.member;
    private final QTeam t = QTeam.team;

    @Override
    public List<Member> findByUsernameCustom(String username) {
        return queryFactory
                .selectFrom(m)
                .where(m.username.eq(username))
                .fetch();
    }

    @Override
    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {

        BooleanBuilder whereCondition = new BooleanBuilder();

        if (StringUtils.hasText(condition.getUsername())) {
            whereCondition.and(m.username.like("%" + condition.getUsername() + "%"));
        }

        if (StringUtils.hasText(condition.getTeamName())) {
            whereCondition.and(t.name.contains(condition.getTeamName()));
        }

        if (condition.getAgeGoe() != null) {
            whereCondition.and(m.age.goe(condition.getAgeGoe()));
        }

        if (condition.getAgeLoe() != null) {
            whereCondition.and(m.age.loe(condition.getAgeLoe()));
        }

        return queryFactory
                .select(new QMemberTeamDto(m.id.as("memberId"), m.username, m.age, t.id.as("teamId"), t.name))
                .from(m)
                .leftJoin(m.team, t)
                .where(whereCondition)
                .fetch();
    }

    @Override
    public List<Member> searchAsMember(MemberSearchCondition condition) {
        return queryFactory
                .select(m)
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameLike(condition.getUsername()),
                        teamNameLike(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return queryFactory
                .select(new QMemberTeamDto(m.id.as("memberId"), m.username, m.age, t.id.as("teamId"), t.name))
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameLike(condition.getUsername()),
                        teamNameLike(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    @Override
    public Page<MemberTeamDto> searchPagingSimple(MemberSearchCondition condition, Pageable pageable) {

        QueryResults<MemberTeamDto> results = queryFactory
                .select(new QMemberTeamDto(m.id.as("memberId"), m.username, m.age, t.id.as("teamId"), t.name))
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameLike(condition.getUsername()),
                        teamNameLike(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MemberTeamDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MemberTeamDto> searchPagingComplex(MemberSearchCondition condition, Pageable pageable) {
        JPAQuery<MemberTeamDto> contentQuery = queryFactory
                .select(new QMemberTeamDto(m.id.as("memberId"), m.username, m.age, t.id.as("teamId"), t.name))
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameLike(condition.getUsername()),
                        teamNameLike(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
                        new OrderSpecifier(Order.ASC, m.username),
                        new OrderSpecifier(Order.ASC, t.name)
                );

        JPAQuery<Long> countQuery = queryFactory
                .select(m.id.count())
                .from(m)
                .leftJoin(m.team, t)
                .where(
                        usernameLike(condition.getUsername()),
                        teamNameLike(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
                );

        List<MemberTeamDto> content = contentQuery.fetch();
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }


    BooleanExpression usernameLike(String username) {
        return StringUtils.hasText(username) ? m.username.like("%" + username + "%") : null;
    }

    BooleanExpression teamNameLike(String teamName) {
        return StringUtils.hasText(teamName) ? t.name.like("%" + teamName + "%") : null;
    }

    BooleanExpression ageGoe(Integer age) {
        return age != null ? m.age.goe(age) : null;
    }

    BooleanExpression ageLoe(Integer age) {
        return age != null ? m.age.loe(age) : null;
    }

}