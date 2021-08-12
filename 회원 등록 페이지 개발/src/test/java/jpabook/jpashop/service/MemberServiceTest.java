package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)//junit실행할 때 스프링이랑 같이 실행한다는 의미, 더 찾아보기
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception{
        //given 이런게 주어졌을 때,
        Member member=new Member();
        member.setName("Yoon");

        //when 이 상황에서
        Long saveId = memberService.join(member);

        //then 이렇게 해야한다
        assertEquals(member,memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복회원예외() throws Exception{
        Member member1=new Member();
        member1.setName("yoon");
        Member member2=new Member();
        member2.setName("yoon");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외가 발생해야 한다.");



    }

}