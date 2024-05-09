package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}