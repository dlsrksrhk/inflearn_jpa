package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        inheritance_join_test();
    }

    //상속 조인전략
    public static void inheritance_join_test() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
            Movie movie = new Movie();
            movie.setDirector("김감독");
            movie.setActor("김배우");
            movie.setName("재밌는 영화3");
            movie.setPrice(5000);
            movie.setCreatedBy("test1");
            movie.setCreatedDate(LocalDateTime.now());
            em.persist(movie);

//            Movie movie = em.find(Movie.class, 1L);
//            System.out.println(movie.getActor());
//            System.out.println(movie.getName());
//            System.out.println(movie.getPrice());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }


    public static void persist() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
            Member member = new Member();
            member.setId(1L);
            member.setName("홍길동");
            em.persist(member);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void find() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
            Member member = em.find(Member.class, 1L);
            System.out.println(member.getName());
            System.out.println(member.getId());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void update() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
            Member member = em.find(Member.class, 1L);
            member.setName("김또깡");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    public static void jpql() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m ", Member.class);
            List<Member> members = query.getResultList();

            members.forEach(member -> {
                System.out.println(member.getName());
                System.out.println(member.getId());
            });

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

}
