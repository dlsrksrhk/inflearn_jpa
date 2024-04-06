package hellojpa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        proxy_test();
    }

    //프록시 테스트
    public static void proxy_test() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //JPA의 엔티티 EntityTransaction 얻음
        tx.begin(); //트랜잭션 시작

        try {
            Team team = new Team();
            team.setName("홍철팀");
            em.persist(team);
            
            Team team2 = new Team();
            team2.setName("명수팀");
            em.persist(team2);

            Member member1 = new Member();
            member1.setName("홍철");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("명수");
            member2.setTeam(team2);
            em.persist(member2);

            em.flush();
            em.clear();

//            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
//                    .getResultList();

            List<Member> resultList = em.createQuery("select m from Member m left outer join fetch m.team t", Member.class)
                    .getResultList();

            resultList.forEach(member -> System.out.println(member.getTeam().getName()));

//            Team findTeam = em.find(Team.class, 1L);
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println(findMember.getClass());
//            System.out.println(findMember.getTeam().getClass());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
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
