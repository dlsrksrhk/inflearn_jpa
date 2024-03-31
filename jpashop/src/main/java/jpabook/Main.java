package jpabook;

import jakarta.persistence.*;
import jpabook.domain.Member;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}