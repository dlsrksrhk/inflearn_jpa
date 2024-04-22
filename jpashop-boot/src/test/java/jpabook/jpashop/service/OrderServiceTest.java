package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문테스트")
    @Test
    public void 주문_테스트() {
        // given
        Member member = createMember();

        final int bookPrice = 10000;
        final int bookStock = 10;

        Book book = createBook("백설공주", bookPrice, bookStock);

        // when
        int orderCount = 2; //주문 개수
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, findOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1, findOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 x 수량이다.", bookPrice * orderCount, findOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", bookStock - orderCount, book.getStockQuantity());
    }

    private Book createBook(String name, int bookPrice, int bookStock) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(bookPrice);
        book.setStockQuantity(bookStock); //재고 개수
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("홀리나이트");
        member.setAddress(new Address("서울시", "벛꽃로", "123456"));
        em.persist(member);
        return member;
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량_초과() {
        // given
        final int bookPrice = 20000;
        final int bookStock = 20;

        Member member = createMember();
        Item item = createBook("JPA", bookPrice, bookStock);

        //when
        int orderCount = 21; //주문 개수
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문_취소() {
        // given
        final int bookPrice = 20000;
        final int bookStock = 20;
        Member member = createMember();
        Item item = createBook("JPA", bookPrice, bookStock);

        int orderCount = 10; //주문 개수
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL이다",OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.",bookStock, item.getStockQuantity());
    }

}