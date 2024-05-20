package sweet.dh.datajpa.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import sweet.dh.datajpa.dto.MemberSearchCondition;
import sweet.dh.datajpa.dto.MemberTeamDto;
import sweet.dh.datajpa.dto.QMemberTeamDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.QMember;
import sweet.dh.datajpa.entity.QTeam;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberDslRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public List<Member> findByUsername(String username) {
        QMember m = QMember.member;
        return queryFactory
                .selectFrom(m)
                .where(m.username.eq(username))
                .fetch();
    }

    public List<Member> findAll() {
        QMember m = QMember.member;
        return queryFactory
                .selectFrom(m)
                .fetch();
    }

    public Optional<Member> findById(Long id) {
        QMember m = QMember.member;
        Member member = queryFactory
                .selectFrom(m)
                .where(m.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(member);
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition) {
        QMember m = QMember.member;
        QTeam t = QTeam.team;

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

    public List<Member> searchAsMember(MemberSearchCondition condition) {
        QMember m = QMember.member;
        QTeam t = QTeam.team;
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

    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        QMember m = QMember.member;
        QTeam t = QTeam.team;
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

    BooleanExpression usernameLike(String username) {
        QMember m = QMember.member;
        return StringUtils.hasText(username) ? m.username.like("%" + username + "%") : null;
    }

    BooleanExpression teamNameLike(String teamName) {
        QTeam t = QTeam.team;
        return StringUtils.hasText(teamName) ? t.name.like("%" + teamName + "%") : null;
    }

    BooleanExpression ageGoe(Integer age) {
        QMember m = QMember.member;
        return age != null ? m.age.goe(age) : null;
    }

    BooleanExpression ageLoe(Integer age) {
        QMember m = QMember.member;
        return age != null ? m.age.loe(age) : null;
    }
}