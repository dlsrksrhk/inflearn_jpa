package sweet.dh.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sweet.dh.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
