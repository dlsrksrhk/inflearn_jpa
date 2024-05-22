package sweet.dh.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sweet.dh.datajpa.dto.MemberDto;
import sweet.dh.datajpa.dto.MemberSearchCondition;
import sweet.dh.datajpa.dto.MemberTeamDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.repository.MemberRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> findMembersPaging(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));
    }

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberRepository.search(condition);
    }

    @GetMapping("/v2/members")
    public Page<MemberTeamDto> searchPagingSimple(MemberSearchCondition condition, Pageable pageable) {
        return memberRepository.searchPagingSimple(condition, pageable);
    }

    @GetMapping("/v3/members")
    public Page<MemberTeamDto> searchPagingComplex(MemberSearchCondition condition, Pageable pageable) {
        return memberRepository.searchPagingComplex(condition, pageable);
    }

}
