package org.inf.main;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.inf.domain.Address;
import org.inf.domain.Member;
import org.inf.domain.MemberDTO;
import org.inf.domain.Team;
import org.inf.jpa.JpaTemplate;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        select_team_by_join();
//        select_embedded_value();
//        select_dto();
//        select_paging();
        join();
    }


    public static void join() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

            TypedQuery<Member> query = em.createQuery("select m from Member m inner join m.team t", Member.class);
            List<Member> resultList = query.getResultList();

            resultList.forEach(en -> System.out.println(en.getUserName()));
        });
    }

    public static void select_paging() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

            TypedQuery<Member> query = em.createQuery("SELECT m FROM Member m ORDER BY m.age DESC ", Member.class);
            List<Member> resultList = query.setFirstResult(0)
                            .setMaxResults(10)
                            .getResultList();
            resultList.forEach(en -> System.out.println(en.getUserName()));
        });
    }

    public static void select_dto() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

            TypedQuery<MemberDTO> query = em.createQuery("SELECT new org.inf.domain.MemberDTO(m.id, m.userName, m.age) FROM Member m ", MemberDTO.class);

            List<MemberDTO> resultList = query.getResultList();
            resultList.forEach(System.out::println);
        });
    }


    public static void select_embedded_value() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

            TypedQuery<Address> query = em.createQuery("SELECT o.address FROM Order o ", Address.class);

            List<Address> resultList = query.getResultList();
            resultList.forEach(addr -> System.out.println(addr.getCity()));
        });
    }

    public static void select_team_by_join() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

//            TypedQuery<Team> query = em.createQuery("SELECT m.team FROM Member m ", Team.class);
            TypedQuery<Team> query = em.createQuery("SELECT t FROM Member m join m.team t", Team.class);

            List<Team> resultList = query.getResultList();
            resultList.forEach(team -> System.out.println(team.getName()));
        });
    }

    public static void select_member() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

            TypedQuery<Member> query = em.createQuery("SELECT m.team FROM Member m ", Member.class);

            List<Member> resultList = query.getResultList();
            resultList.forEach(m -> System.out.println(m.getUserName()));
        });
    }


    public static void createTeamAndMember(EntityManager em) {
        Team team = new Team();
        team.setName("슈샤이어");
        em.persist(team);

        Member member1 = new Member();
        member1.setUserName("홀리나이트");
        member1.setAge(30);
        member1.setTeam(team);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUserName("버서커");
        member2.setAge(20);
        member2.setTeam(team);
        em.persist(member2);

        em.flush();
        em.clear();
    }


}