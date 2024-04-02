package hellojpa;

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
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="locker_id")
    private Long id;
    private String lockerNumber;

    @OneToOne(mappedBy = "locker")
    private Member member;
}
