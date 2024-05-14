package sweet.dh.datajpa.repository;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.Team;

public class MemberSpec {
    public static Specification<Member> teamNameEqual(String teamName) {
        return (m, query, where) -> {

            if(!StringUtils.hasText(teamName)){
                return null;
            }

            Join<Member, Team> t = m.join("team", JoinType.LEFT);
            return where.equal( t.get("name"), teamName);
        };
    }

    public static Specification<Member> userNameEqual(String userName) {
        if(!StringUtils.hasText(userName)){
            return null;
        }
        return (m, query, where) -> where.equal( m.get("username"), userName);
    }

    public static Specification<Member> teamNameEqualAndUserNameEqual(String teamName, String userName) {
        return teamNameEqual(teamName).and(userNameEqual(userName));
    }
}
