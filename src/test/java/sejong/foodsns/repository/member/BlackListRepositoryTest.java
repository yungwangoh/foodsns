package sejong.foodsns.repository.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.member.BlackList;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sejong.foodsns.domain.member.MemberType.*;

@DataJpaTest
class BlackListRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BlackListRepository blackListRepository;

    @Test
    @DisplayName("블랙리스트 등록")
    void blackListMemberCreate() {
        // given
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", BLACKLIST);
        Member save = memberRepository.save(member);
        BlackList blackList = new BlackList("악의적인 댓글", save);

        // when
        BlackList blackListSave = blackListRepository.save(blackList);

        // then
        assertThat(blackListSave).isEqualTo(blackList);
    }

    @Test
    @DisplayName("블랙리스트 등록할 때 회원이 블랙리스트가 아닐 경우")
    void blackListCratedMemberIsNotBlackList() {
        // given
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", NORMAL);
        Member save = memberRepository.save(member);
        BlackList saveBlack = null;
        // when
        if(member.getMemberType().equals(BLACKLIST)) {
            BlackList blackList = new BlackList("악의적인 댓글", save);
            saveBlack = blackListRepository.save(blackList);
        }

        // then
        assertThat(saveBlack).isNull();
    }

    @Test
    @DisplayName("블랙리스트 등록할 때 회원이 블랙리스트일 경우 등록")
    void blackListCratedMemberIsBlackList() {
        // given
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", BLACKLIST);
        Member save = memberRepository.save(member);
        BlackList saveBlack = null;
        BlackList blackList = null;
        // when
        if(member.getMemberType().equals(BLACKLIST)) {
            blackList = new BlackList("악의적인 댓글", save);
            saveBlack = blackListRepository.save(blackList);
        }

        // then
        assertThat(saveBlack).isNotNull();
        assertThat(saveBlack).isEqualTo(blackList);
    }

    @Test
    @DisplayName("블랙리스트 회원 찾기")
    void blackListMemberAllDelete() {
        // given
        Member member = new Member("윤광오", "swager253@zzz.com", "qwer1234@A", BLACKLIST);
        Member save = memberRepository.save(member);
        BlackList blackList = new BlackList("악의적인 댓글", save);
        BlackList blackListSave = blackListRepository.save(blackList);

        // when
        Optional<BlackList> findBlackList = blackListRepository.findById(blackListSave.getId());

        // then
        assertThat(findBlackList.get()).isEqualTo(blackListSave);
    }
}