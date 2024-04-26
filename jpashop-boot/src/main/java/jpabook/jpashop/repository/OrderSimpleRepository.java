package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.controller.dto.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderByDtos() {
        String jqpl = """
                select new jpabook.jpashop.controller.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) 
                    from Order o 
                    join o.member m 
                    join o.delivery d
                """;
        return em.createQuery(jqpl, OrderSimpleQueryDto.class).getResultList();
    }
}