package sweet.dh.datajpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member extends BaseEntity implements Persistable<Long> {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeUserName(String name) {
        this.username = name;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMember().add(this);
    }
}
