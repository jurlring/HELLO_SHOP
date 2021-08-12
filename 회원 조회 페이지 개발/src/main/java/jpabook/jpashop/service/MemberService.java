package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class MemberService {


    private final MemberRepository memberRepository;
    //@Autowired안써도 됨 하나일때는
   public MemberService(MemberRepository memberRepository){//=>@AllArgsConstructor로 해도 됨, @RequierdArgsConstructor은 final붙은 애를 생성자를 만들어줌
        this.memberRepository=memberRepository;
    }

    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }
    //중복 예외처리
    private void validateDuplicateMember(Member member) {
        List<Member> findmembers = memberRepository.findByName(member.getName());//최후의 보루로 name를 unique로 잡는게 좋음
        if (!findmembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다,");
        }
    }

    //회원 조회
    @Transactional(readOnly = true)
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
    //회원 전체 조회
    @Transactional(readOnly = true)
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
}
