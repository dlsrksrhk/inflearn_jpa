package jpabook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jpabook.domain.Member;
import jpabook.domain.Order;
import jpabook.domain.OrderStatus;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
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

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}