package sweet.dh.datajpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sweet.dh.datajpa.entity.Member;
import sweet.dh.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(false)
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCrud() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> members = memberJpaRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        Long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        Long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> members = memberJpaRepository.findByUsernameAndAgeGreaterThan("member1", 9);
        assertThat(members.get(0).getUsername()).isEqualTo("member1");
        assertThat(members.get(0).getAge()).isEqualTo(10);
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void pagingTest() {
        saveMemberAndTeam();
        int age = 10;
        int offset = 0;
        int limit = 5;

        List<Member> members = memberJpaRepository.findByAgeWithPaging(age, offset, limit);
        long totalCount = memberJpaRepository.totalCountByAgeWithPaging(age);
        System.out.println("members : " + members);
        System.out.println("totalCount : " + totalCount);
    }

    public void saveMemberAndTeam() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamJpaRepository.save(teamA);
        teamJpaRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);
        memberJpaRepository.save(member3);
        memberJpaRepository.save(member4);

        memberJpaRepository.save(new Member("member5", 25));
        memberJpaRepository.save(new Member("member6", 26));
        memberJpaRepository.save(new Member("member7", 27));
        memberJpaRepository.save(new Member("member8", 28));
        memberJpaRepository.save(new Member("member9", 29));
        memberJpaRepository.save(new Member("member10", 30));
        memberJpaRepository.save(new Member("member11", 31));
    }

    @Test
    public void bulkAgePlusOne() {
        saveMemberAndTeam();
        int count = memberJpaRepository.bulkAgePlusOne(10);
        System.out.println(count);
    }

    @Test
    public void jpaEventBaseEntity() {
        saveMemberAndTeam();
        em.flush();
        em.clear();

        List<Member> members = memberJpaRepository.findAll();
        members.forEach(member -> member.changeUserName(member.getUsername() + "_1"));
        System.out.println(members);

        em.flush(); //@Rollback(false) 사용 안하면 변경감지 작동 안하므로 수동 flush 해줘야 변경내용 반영됨.
    }

}