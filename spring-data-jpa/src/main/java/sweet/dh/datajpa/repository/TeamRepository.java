package sweet.dh.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sweet.dh.datajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
