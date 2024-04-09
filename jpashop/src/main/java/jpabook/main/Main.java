package jpabook.main;

import jpabook.domain.Address;
import jpabook.domain.Member;
import jpabook.domain.Order;
import jpabook.domain.OrderStatus;
import jpabook.jpa.JpaTemplate;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        test();
//        value_object_share_problem();
//        value_object_equals_test();

//        value_object_collection_insert();
        value_object_collection_update();
    }

    public static void value_object_collection_update() {
        new JpaTemplate().execute(em -> {
            Member member = em.find(Member.class, 1L);
            member.getFavoriteFoot().remove("치킨");
//            member.getAddressHistory().remove(new Address("city2", "street2", "zipcode2"));
        });
    }

    public static void value_object_collection_insert() {
        new JpaTemplate().execute(em -> {
            Member member1 = new Member();
            member1.setName("member1");
            member1.getFavoriteFoot().add("치킨");
            member1.getFavoriteFoot().add("족발");
            member1.getFavoriteFoot().add("피자");
            member1.getAddressHistory().add(new Address("city1", "street1", "zipcode1"));
            member1.getAddressHistory().add(new Address("city2", "street2", "zipcode2"));

            em.persist(member1);
        });
    }

    public static void value_object_equals_test(){
        Address address1 = new Address("city1", "street1", "zipcode1");
        Address address2 = new Address("city1", "street1", "zipcode1");

        System.out.println(address1 == address2);
        System.out.println(address1.equals(address2));
    }


    public static void value_object_share_problem() {
        new JpaTemplate().execute(em -> {
            Address address = new Address("city1", "street1", "zipcode1");

            Member member1 = new Member();
            member1.setName("member1");
//            member1.setHomeAddress(address);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
//            member2.setHomeAddress(address);
            em.persist(member2);

//            em.flush();
//            em.clear();
//            Member findMember = em.find(Member.class, member2.getId());
//            member2.getHomeAddress().setCity("city2");
        });
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