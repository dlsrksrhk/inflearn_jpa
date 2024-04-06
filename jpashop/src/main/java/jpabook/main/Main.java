package jpabook.main;

import jpabook.domain.Member;
import jpabook.domain.Order;
import jpabook.domain.OrderStatus;
import jpabook.jpa.JpaTemplate;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        new JpaTemplate().execute(em -> {
            Member member1 = new Member();
            member1.setName("member1");
            em.persist(member1);

            Order order1 = new Order();
            order1.setMember(member1);
            order1.setStatus(OrderStatus.ORDER);
            em.persist(order1);

            Order order2 = new Order();
            order2.setMember(member1);
            order2.setStatus(OrderStatus.ORDER);
            em.persist(order2);

            member1.getOrders().add(order1);
            member1.getOrders().add(order2);
            List<Order> orders1 = member1.getOrders();

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            List<Order> orders2 = findMember.getOrders();
            System.out.println(orders1 == orders2); //false

            orders2.forEach(order -> {
                System.out.println(order.getId());
            });
        });
    }
}