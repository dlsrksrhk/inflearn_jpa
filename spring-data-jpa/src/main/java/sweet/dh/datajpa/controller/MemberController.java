package sweet.dh.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sweet.dh.datajpa.dto.MemberDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.repository.MemberRepository;

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

}
