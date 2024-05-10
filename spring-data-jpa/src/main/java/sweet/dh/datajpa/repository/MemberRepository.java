package sweet.dh.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sweet.dh.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsername(String username);
}