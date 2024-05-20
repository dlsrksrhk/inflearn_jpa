package sweet.dh.datajpa.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberDto {
    private Long id;
    private String username;
    private int age;
    private String teamName;

    public MemberDto() {
    }

    @QueryProjection
    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    @QueryProjection
    public MemberDto(Long id, String username, int age, String teamName) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.teamName = teamName;
    }

}