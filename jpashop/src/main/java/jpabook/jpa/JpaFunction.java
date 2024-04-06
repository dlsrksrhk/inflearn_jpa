package jpabook.jpa;

import jakarta.persistence.EntityManager;

public interface JpaFunction {
    void execute(EntityManager em);
}
