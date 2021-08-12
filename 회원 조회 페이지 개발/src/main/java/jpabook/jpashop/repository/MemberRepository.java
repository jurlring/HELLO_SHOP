package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository//내부에 @Component가 있음
public class MemberRepository {

    @PersistenceContext//스프링이 EntityManager를 만들어서 주입
    private EntityManager em;

    public void save(Member member){//jpa가 member를 저장
        em.persist(member);//트랜잭션이 커밋되는 시점에 db에 반영
    }

    public Member findOne(Long id){//member를 찾아서 반환
        return em.find(Member.class,id);
    }

    public List<Member> findAll(){//from의 대상이 테이블이 아니라 entity
        return em.createQuery("select m from Member  m",Member.class)//조회타입 member.class
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select  m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
