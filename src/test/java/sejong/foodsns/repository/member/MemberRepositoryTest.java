package sejong.foodsns.repository.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional(readOnly = true)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    String username, username2, username3;
    String email, email2, email3;
    String password, password2, password3;
    MemberType memberType;

    @BeforeEach
    void init() {
        this.username = "윤광오";
        this.email = "swager253@naver.com";
        this.password = "1234";
        this.memberType = MemberType.NORMAL;

        this.username2 = "하윤";
        this.email2 = "qkfks1234@naver.com";
        this.password2 = "1234";

        this.username3 = "윤광";
        this.email3 = "alstngud77@naver.com";
        this.password3 = "12345";
    }

    @Test
    @Order(0)
    @DisplayName("회원 저장")
    @Transactional
    void MemberSave() {
        // given
        Long id = 1L;

        // when
        Member member = new Member(username, email, password, memberType);
        Member save = memberRepository.save(member);

        // then
        assertThat(member).isEqualTo(save);
    }

    @Test
    @Order(1)
    @DisplayName("회원 찾기")
    void memberFind() {
        // given
        Long id = 1L;
        Member member = new Member(username, email, password, memberType);
        Member save = memberRepository.save(member);

        // when
        Member findMember = getMember(save);

        // then
        assertThat(member.getId()).isEqualTo(findMember.getId());
    }

    @Test
    @Order(2)
    @DisplayName("모든 회원 찾기")
    void memberFindAll() {
        // given
        Long id = 1L;
        List<Member> addMember = getMembers();

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(addMember.size()).isEqualTo(members.size());

    }

    @Test
    @Order(3)
    @DisplayName("회원 삭제")
    @Transactional
    void memberDelete() {
        // given
        Long id = 1L;
        Member member = new Member(username, email, password, MemberType.NORMAL);
        Member save = memberRepository.save(member);

        // when
        memberRepository.delete(save);

        // then

    }

    @Test
    @Order(4)
    @DisplayName("회원 랭크")
    void memberRank() {
        // given


        // when

        // then

    }

    @Test
    @Order(5)
    @DisplayName("중복 회원 예외")
    void memberValidationDuplicateException() {
        // given

        // when

        // then

    }

    private List<Member> getMembers() {
        List<Member> addMember = new ArrayList<>();

        Member member = new Member(username, email, password, MemberType.NORMAL);
        memberRepository.save(member);

        Member member2 = new Member(username2, email2, password2, MemberType.NORMAL);
        memberRepository.save(member2);

        Member member3 = new Member(username3, email3, password3, MemberType.NORMAL);
        memberRepository.save(member3);

        addMember.add(member);
        addMember.add(member2);
        addMember.add(member3);
        return addMember;
    }

    private Member getMember(Member save) {
        Optional<Member> memberId = memberRepository.findById(save.getId());
        Member findMember = memberId.get();
        return findMember;
    }
}