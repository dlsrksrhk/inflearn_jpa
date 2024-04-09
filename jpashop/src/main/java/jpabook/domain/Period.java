package jpabook.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Embeddable
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
