package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;
//주요 테스트 기능
//상품 주문이 성공해야 한다
//상품을 주문할 때 재고 수량을 초과하면 안된다
//주문 취소가 성공해야 한다
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member=createMember();//member 생성
        Item item=createBook("jpa",10000,10);//item(book) 생성
        int orderCount=2;//얼마나 살지
        //when
        Long orderId=orderService.order(member.getId(),item.getId(),orderCount);//주문 했을 때
        //then
        Order getOrder=orderRepository.findOne(orderId);//저장된 레포에서 값을 가져온다. 이 값과 위에서 설정한 값이 같은지 test!

        //주문 상태 확인
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());//message-> 오류나면 뜨도록, expected, actual
        //주문 종류 확인
        assertEquals("주문한 상품 종류 수가 정확해야 한다!",1,getOrder.getOrderItems().size());
        //주문 가격 확인
        assertEquals("주문 가격은 가격 * 수량이여야 한다!",10000*2,getOrder.getTotalPrice());
        //주문 수량만큼 재고가 줄었는지 확인
        assertEquals("주문 수량만큼 재고가 줄어야 한다!",8,item.getStockQuantity());
    }
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member=createMember();
        Item item=createBook("jpa",10000,10);
        int orderCount=11;
        //when
        orderService.order(member.getId(),item.getId(),orderCount);//주문 했을 때
        //then
        fail("재고 수량 부족 예외가 발생해야 한다!");//오류가 났는데 처리가 exception이 안되고 여기까지 오면 fail
    }
    @Test
    public void 주문취소(){
        //given
        Member member=createMember();
        Item item=createBook("jpa",10000,10);
        int orderCount=2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);//위의 값으로 주문 했을 때
        //when
        orderService.cancelOrder(orderId);//주문을 취소 하면
        //then
        Order getOrder=orderRepository.findOne(orderId);
        //주문 취소 후 상태 변화 확인
        assertEquals("주문 취소시 상태는 CANCEL이다!",OrderStatus.CANCEL,getOrder.getStatus());
        //주문 취소 상품의 재고 증가 확인
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다!",10,item.getStockQuantity());
    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member=new Member();
        member.setName("주리링");
        member.setAddress(new Address("서울","세종대학교","1234"));
        em.persist(member);
        return member;

    }
}