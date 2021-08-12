package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter @Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)//String으로 안하면 값이 추가되거나 삭제될때마다 반환하는 int값이 달라져서 다루기 힘듬
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]
}
