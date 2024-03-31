package jpabook.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="ORDER_ITEM_ID")
    private Long id;

    @Column(name ="ORDER_ID")
    private Long orderId;

    @Column(name ="ITEM_ID")
    private Long itemId;

    private int orderPrice;
    private int count;
}
