package sejong.foodsns.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.member.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByUsername(String username);
    Optional<Member> findMemberByEmail(String email);
    @Query("select m from Member m join fetch m.boards b where b.id = :boardId")
    Optional<Member> findMemberByBoards(@Param("boardId") Long id);
    Boolean existsMemberByUsername(String username);
    Boolean existsMemberByEmail(String email);
}
