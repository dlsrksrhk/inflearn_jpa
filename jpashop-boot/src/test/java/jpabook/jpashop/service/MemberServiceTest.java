package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("회원가입")
    @Test
//    @Rollback(false)
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("홀리나이트");

        // when
        Long savedId = memberService.join(member);
        em.flush();

        // then
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @DisplayName("test")
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("홀리나이트");
        Member member2 = new Member();
        member2.setName("홀리나이트");

        // when
        memberService.join(member1);
        memberService.join(member2);

        // then
        // fail 메서드가 실행되었다는 것은 member2 join시 예외가 발생하지 않았다는 것
        fail("예외가 발생하지 않으면 실패");
    }

}