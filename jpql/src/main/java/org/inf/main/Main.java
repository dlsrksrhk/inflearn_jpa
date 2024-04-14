package org.inf.main;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.inf.domain.*;
import org.inf.jpa.JpaTemplate;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        select_team_by_join();
//        select_embedded_value();
//        select_dto();
//        select_paging();
//        join();
//        jpql_type();
//        jpql_path_explore();
        fetch_join();
    }

    private static void fetch_join() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

//            TypedQuery<Member> query = em.createQuery("select m from Member m inner join fetch m.team t", Member.class);
            TypedQuery<Team> query = em.createQuery("select t from Team t inner join fetch t.members m WHERE m.userName = '홀리나이트'", Team.class);

            List<Team> resultList = query.getResultList();
            resultList.forEach(team -> {
                team.getMembers().forEach(member -> {
                    System.out.println(String.format("member : %s, team : %s, team hashCode : %s ", member.getUserName(), team.getName(), team.hashCode()));
                });
            });

//            resultList.forEach(member -> {
//                System.out.println(String.format("member : %s, team : %s", member.getUserName(), member.getTeam().getName()));
//            });
        });
    }

    private static void jpql_path_explore() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

//            Query query = em.createQuery("select m.team.name from Member m ");
//            Query query = em.createQuery("select t.name from Member m inner join m.team t");
//            Query query = em.createQuery("select t.members from Team t ");
            Query query = em.createQuery("select m.userName, t.name from Team t join t.members m");
            List<Object[]> resultList = query.getResultList();
            resultList.forEach(o -> {
                System.out.println("멤버 : " + o[0] + ", 팀 : " + o[1]);
            });

//            Query query = em.createQuery("select m.team from Member m ");

//            List<Object> resultList = query.getResultList();
//            resultList.forEach(System.out::println);
        });
    }

    public static void jpql_type() {
        new JpaTemplate().execute(em -> {
            createTeamAndMember(em);

//            TypedQuery<Member> query = em.createQuery("select m from Member m where m.isDeleted = true", Member.class);
//            TypedQuery<Member> query = em.createQuery("select m from Member m where m.memberType = org.inf.domain.MemberType.USER ", Member.class);
            TypedQuery<Member> query = em.createQuery("select m.team.name from Member m ", Member.class);


            List<Member> resultList = query.getResultList();

            resultList.forEach(en -> System.out.println(en.getUserName()));
        });
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

        Team team2 = new Team();
        team2.setName("애니츠");
        em.persist(team2);

        Member member1 = new Member();
        member1.setUserName("홀리나이트");
        member1.setAge(30);
        member1.setTeam(team);
        member1.setDeleted(true);
        member1.setMemberType(MemberType.ADMIN);
        em.persist(member1);

        Member member2 = new Member();
        member2.setUserName("버서커");
        member2.setAge(20);
        member2.setTeam(team);
        member2.setDeleted(false);
        member2.setMemberType(MemberType.USER);
        em.persist(member2);

        Member member3 = new Member();
        member3.setUserName("인파이터");
        member3.setAge(25);
        member3.setTeam(team2);
        member3.setDeleted(false);
        member3.setMemberType(MemberType.USER);
        em.persist(member3);

        em.flush();
        em.clear();
    }


}