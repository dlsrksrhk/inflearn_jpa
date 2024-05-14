package sweet.dh.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import sweet.dh.datajpa.dto.MemberDto;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        Long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("member1", 9);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");
        assertThat(members.get(0).getAge()).isEqualTo(10);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void findTop3By() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findTop3By();
    }

    @Test
    public void findUser() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findUser("member2", 20);
        System.out.println(members);
        assertThat(members.get(0).getUsername()).isEqualTo("member2");
        assertThat(members.get(0).getAge()).isEqualTo(20);
    }

    @Test
    public void findUserNameList() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> userNames = memberRepository.findUserNameList();
        System.out.println(userNames);
        assertThat(userNames.get(0)).isEqualTo("member1");
        assertThat(userNames.get(1)).isEqualTo("member2");
    }

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
        memberRepository.save(new Member("member6", 26, teamB));
        memberRepository.save(new Member("member7", 27, teamB));
        memberRepository.save(new Member("member8", 28, teamB));
        memberRepository.save(new Member("member9", 29, teamA));
        memberRepository.save(new Member("member10", 30, teamA));
        memberRepository.save(new Member("member11", 31, teamA));
//        em.flush();
//        em.clear();
    }

    @Test
    public void findMemberDtoList() {
        saveMemberAndTeam();
        List<MemberDto> members = memberRepository.findMemberDtoList();
        System.out.println(members);
    }


    @Test
    public void findByNames() {
        saveMemberAndTeam();

        List<Member> members = memberRepository.findByNames(List.of("member1", "member2", "member3", "member4"));
        System.out.println(members);
    }

    @Test
    public void findReturnTypeTest() {
        saveMemberAndTeam();
//        Member member = new Member("member1", 10);
//        memberRepository.save(member);

        List<Member> members = memberRepository.findListByUsername("member1");
        System.out.println(members);

        Member member1 = memberRepository.findMemberByUsername("member2");
        System.out.println(member1);

        Optional<Member> member2 = memberRepository.findOptionalMemberByUsername("member3");
        System.out.println(member2.orElseThrow(NoResultException::new));
    }

    @Test
    public void findByAgePaging() {
        saveMemberAndTeam();

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> pageMember = memberRepository.findByAgeGreaterThanEqual(10, pageRequest);

        System.out.println("totalPage : " + pageMember.getTotalPages());
        System.out.println("totalCount : " + pageMember.getTotalElements());
        System.out.println("content : " + pageMember.getContent());

        Page<MemberDto> pageMemberDto = pageMember.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

        System.out.println("totalPage : " + pageMemberDto.getTotalPages());
        System.out.println("totalCount : " + pageMemberDto.getTotalElements());
        System.out.println("content : " + pageMemberDto.getContent());
    }

    @Test
    public void bulkAgePlusOne() {
        saveMemberAndTeam();
        int count = memberRepository.bulkAgePlusOne(10);
        System.out.println(count);

//        em.flush();
//        em.clear();
        Member member1 = memberRepository.findMemberByUsername("member1");
        System.out.println(member1);
    }

    @Test
    public void findMemberLazy() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

//        List<Member> members = memberRepository.findMembersFetchJoin();
//        List<Member> members = memberRepository.findAll();
        List<Member> members = memberRepository.findAllWithFetchJoin();

        members.forEach(member -> {
            System.out.println(member.getTeam().getName());
        });
    }

    @Test
    public void queryHint() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        Member member1 = memberRepository.findReadOnlyByUsername("member1"); //select 이후 영속성 컨텍스트에 저장 x
        member1.changeUserName("member1_1"); //변경감지 x
        
        em.flush(); //update 수행 안됨.
        System.out.println(member1.getUsername()); //member1_1 나오지만 실제 db에는 member1 그대로임
    }

    @Test
    public void lockTest() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        Member member1 = memberRepository.findLockByUsername("member1");
        System.out.println(member1.getUsername());
    }

    @Test
    public void findByUsernameCustom() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findByUsernameCustom("member1");
        System.out.println(members);
    }

    @Test
    public void jpaEventBaseEntity() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();
        members.forEach(member -> member.changeUserName(member.getUsername() + "_1"));
        System.out.println(members);

        em.flush(); //@Rollback(false) 사용 안하면 변경감지 작동 안하므로 수동 flush 해줘야 변경내용 반영됨.
    }

    @Test
    public void specBasic() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll(MemberSpec.teamNameEqualAndUserNameEqual("teamA", "member2"));
        System.out.println(members);
    }

    @Test
    public void findProjectionUsernameByUsername() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        List<UsernameOnly> usernames = memberRepository.findProjectionUsernameByUsername("member2");
        usernames.forEach(usernameOnly -> System.out.println(usernameOnly.getUsername()));
    }

    @Test
    public void findByNativeQuery() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        List<Member> membrs = memberRepository.findByNativeQuery("member2");
        System.out.println(membrs);
        membrs.get(0).changeUserName("qweqwe");
        em.flush();
    }

}