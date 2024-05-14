package sweet.dh.datajpa.repository;

import sweet.dh.datajpa.entity.Member;

import java.util.List;

public interface MemberCustomRepository {
    List<Member> findByUsernameCustom(String username);
}