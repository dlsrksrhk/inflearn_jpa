package sweet.dh.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sweet.dh.datajpa.dto.MemberSearchCondition;
import sweet.dh.datajpa.dto.MemberTeamDto;
import sweet.dh.datajpa.entity.Member;

import java.util.List;

public interface MemberDslRepository {
    List<Member> findByUsernameCustom(String username);

    List<MemberTeamDto> searchByBuilder(MemberSearchCondition condition);

    List<Member> searchAsMember(MemberSearchCondition condition);

    List<MemberTeamDto> search(MemberSearchCondition condition);

    Page<MemberTeamDto> searchPagingSimple(MemberSearchCondition condition, Pageable pageable);

    Page<MemberTeamDto> searchPagingComplex(MemberSearchCondition condition, Pageable pageable);
}