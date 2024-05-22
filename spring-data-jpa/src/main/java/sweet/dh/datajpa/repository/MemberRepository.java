package sweet.dh.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import sweet.dh.datajpa.dto.MemberDto;
import sweet.dh.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberDslRepository, JpaSpecificationExecutor<Member> {
    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3By();

    @Query("select m from Member m where m.username = :username and m.age = :age ")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m ")
    List<String> findUserNameList();

    @Query("select new sweet.dh.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDtoList();

    @Query("select m from Member m where m.username in :names ")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username);

    Member findMemberByUsername(String Username);

    Optional<Member> findOptionalMemberByUsername(String username);

    @Query(
            value = "select m from Member m left join fetch m.team t where m.age >= :age",
            countQuery = "select count(m.username) from Member m where m.age >= :age"
    )
    Page<Member> findByAgeGreaterThanEqual(@Param("age") int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age = :age")
    int bulkAgePlusOne(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team t")
    List<Member> findMembersFetchJoin();

//    @EntityGraph(attributePaths = {"team"})
//    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m ")
    List<Member> findAllWithFetchJoin();

    @QueryHints(
            @QueryHint(name = "org.hibernate.readOnly", value = "true")
    )
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Member findLockByUsername(String username);

    List<UsernameOnly> findProjectionUsernameByUsername(@Param("username") String username);

    @Query(value = "select * from member where username = :username", nativeQuery = true)
    List<Member> findByNativeQuery(@Param("username") String username);
}