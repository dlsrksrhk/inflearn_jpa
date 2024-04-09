package jpabook.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="member_id")
    private Long id;
    private String name;

    @Embedded
    private Period workingPeriod;
//
//    @Embedded
//    private Address homeAddress;

//    @OrderColumn(name = "food_order")
    @ElementCollection
    @CollectionTable(name = "favorite_food", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "food_name")
    private Set<String> favoriteFoot = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "address_history", joinColumns = @JoinColumn(name = "member_id"))
    private List<Address> addressHistory = new ArrayList<>();

//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "city", column = @Column(name = "working_city")),
//            @AttributeOverride(name = "street", column = @Column(name = "working_street")),
//            @AttributeOverride(name = "zipcode", column = @Column(name = "working_zipcode"))
//    })
//    private Address workingAddress;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private List<AddressEntity> addressHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
