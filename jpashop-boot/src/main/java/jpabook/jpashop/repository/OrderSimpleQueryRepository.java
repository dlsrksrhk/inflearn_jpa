package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.dto.OrderFlatDto;
import jpabook.jpashop.dto.OrderItemQueryDto;
import jpabook.jpashop.dto.OrderQueryDto;
import jpabook.jpashop.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderByDtos() {
        String jqpl = """
                select new jpabook.jpashop.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) 
                    from Order o 
                    join o.member m 
                    join o.delivery d
                """;
        return em.createQuery(jqpl, OrderSimpleQueryDto.class).getResultList();
    }

    public List<OrderQueryDto> findOrderQueryDtos() {
        //루트 조회(toOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders();

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    /**
     * 쿼리 2번만에 컬렉션을 DTO로 조회
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        //쿼리1
        List<OrderQueryDto> result = findOrders();

        //id값만 추출
        List<Long> orderIds = result.stream()
                .map(OrderQueryDto::getOrderId)
                .collect(Collectors.toList());

        //쿼리2
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        """
                                select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) 
                                    from OrderItem oi 
                                    join oi.item i 
                                    where oi.order.id in :orderIds
                                """, OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        //orderId별 OrderItemDto 그룹핑
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        //OrderDto에 직접 OrderDto 맵핑
        result.forEach(o -> {
            o.setOrderItems(orderItemMap.get(o.getOrderId()));
        });

        return result;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpabook.jpashop.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                        "select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                        "select new jpabook.jpashop.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi " +
                                "join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}