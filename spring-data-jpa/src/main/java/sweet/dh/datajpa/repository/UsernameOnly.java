package sweet.dh.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    @Value("#{'name : ' + target.username + ', team : ' + target.team.name}")
    String getUsername();
}
