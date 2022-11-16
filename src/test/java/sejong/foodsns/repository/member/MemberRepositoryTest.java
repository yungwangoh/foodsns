package sejong.foodsns.repository.member;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberRank;
import sejong.foodsns.domain.member.MemberType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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
    @DisplayName("회원 저장")
    void MemberSave() {
        // given
        Long id = 1L;

        // when
        Member member = new Member(username, email, password, memberType);
        Member save = memberRepository.save(member);

        // then
        assertThat(member).isEqualTo(save);
        assertThat(member.getUsername()).isEqualTo(save.getUsername());
        assertThat(member.getMemberType()).isEqualTo(save.getMemberType());
        assertThat(member.getEmail()).isEqualTo(save.getEmail());
        assertThat(member.getPassword()).isEqualTo(save.getPassword());
    }

    @Test
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
    @DisplayName("모든 회원 찾기")
    void memberFindAll() {
        // given
        Long id = 1L;
        List<Member> addMember = getMembers();
        memberRepository.saveAll(addMember);

        // when
        List<Member> members = memberRepository.findAll();

        // then
        assertThat(addMember.size()).isEqualTo(members.size());
    }

    @Test
    @DisplayName("회원 삭제")
    void memberDelete() {
        // given
        Long id = 1L;
        Member member = new Member(username, email, password, MemberType.NORMAL);
        Member save = memberRepository.save(member);

        // when
        memberRepository.delete(save);
        Optional<Member> deleteMember = memberRepository.findById(save.getId());

        // then
        assertFalse(deleteMember.isPresent());
    }

    @Test
    @DisplayName("회원 이름 수정")
    void memberUpdateName() {
        // given
        Long id = 1L;
        String updateUserName = "하윤";
        Member member = new Member(username, email, password, MemberType.NORMAL);
        Member save = memberRepository.save(member);

        // when
        save.memberNameUpdate(updateUserName);
        Member updateMember = memberRepository.save(save);

        // then
        assertThat(updateMember.getUsername()).isEqualTo(updateUserName);
        assertThat(updateMember.getUsername()).isNotEqualTo(username);
    }

    @Test
    @DisplayName("회원 이메일 수정")
    void memberUpdateEmail() {
        // given
        Long id = 1L;
        String updateUserEmail = "qkfks1234@naver.com";
        Member member = new Member(username, email, password, MemberType.NORMAL);
        Member save = memberRepository.save(member);

        // when
        save.memberEmailUpdate(updateUserEmail);
        Member updateMember = memberRepository.save(save);

        // then
        assertThat(updateMember.getEmail()).isEqualTo(updateUserEmail);
        assertThat(updateMember.getEmail()).isNotEqualTo(email);
    }

    @Test
    @DisplayName("회원 비밀번호 수정")
    void memberUpdatePassword() {
        // given
        Long id = 1L;
        String updateUserPassword = "101010";
        Member member = new Member(username, email, password, MemberType.NORMAL);
        Member save = memberRepository.save(member);

        // when
        save.memberPasswordUpdate(updateUserPassword);
        Member updateMember = memberRepository.save(save);

        // then
        assertThat(updateMember.getPassword()).isEqualTo(updateUserPassword);
        assertThat(updateMember.getPassword()).isNotEqualTo(password);
    }

    @Test
    @DisplayName("중복 회원 예외")
    void memberValidationDuplicateException() throws IllegalStateException {
        // given
        Long id = 1L;
        Member member = new Member(username, email, password, MemberType.NORMAL);
        Member save = memberRepository.save(member);

        // when
        Member findMember = getMember(save);

        // then
        assertThatThrownBy(() -> memberEqualCheck(findMember)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("회원 추천 랭크")
    void memberRecommendedRank() {
        // given
        int bronzeNumOfRecommend = 10;
        int silverNumOfRecommend = 30;
        int goldNumOfRecommend = 50;
        int platinumNumOfRecommend = 80;
        int diamondNumOfRecommend = 100;
        int vipNumOfRecommend = 150;

        Member member = new Member(username, email, password, MemberType.NORMAL);

        // when
        member.memberRecommendUp(bronzeNumOfRecommend);
        Member save = memberRepository.save(member);

        // then
        assertThat(save.getMemberRank()).isEqualTo(MemberRank.BRONZE);
        assertThat(save.getMemberRank()).isNotEqualTo(MemberRank.SILVER);
    }


    /**
     * 중복 회원 예외 테스트 메서드
     * @param findMember
     */
    private void memberEqualCheck(Member findMember) {
        if (findMember.getUsername().equals(username)) throw new IllegalStateException("중복된 회원 입니다.");
    }

    /**
     * 회원들을 담는 초기화 테스트 메서드
     * @return
     */
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

    /**
     * 찾은 회원을 리턴하는 테스트 메서드
     * @param save
     * @return
     */
    private Member getMember(Member save) {
        Optional<Member> memberId = memberRepository.findById(save.getId());
        Member findMember = memberId.get();
        return findMember;
    }
}