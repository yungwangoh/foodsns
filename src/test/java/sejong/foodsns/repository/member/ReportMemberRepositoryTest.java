package sejong.foodsns.repository.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.domain.member.ReportMember;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReportMemberRepositoryTest {

    private static final int reportTestOne = 10;
    private static final int reportTestTwo = 20;
    private static final int reportTestThree = 30;

    private static final int penaltyOne = 1;
    private static final int penaltyTwo = 2;
    private static final int penaltyThree = 3;

    private Member save;

    @Autowired
    private ReportMemberRepository reportMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void init() {
        save = initProcess();
    }

    @Test
    @DisplayName("신고 리포트 저장")
    void saveReport() {
        // given
        ReportMember reportMember = initReportMember();

        // when
        ReportMember reportMemberSave = reportMemberRepository.save(reportMember);

        // then
        assertThat(reportMember).isEqualTo(reportMemberSave);
        assertThat(reportMember.getMember()).isEqualTo(reportMemberSave.getMember());
        assertThat(reportMember.getMember().getReportCount()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("신고 리포트 조회")
    void findReport() {
        // given
        ReportMember reportMember = initReportMember();
        ReportMember reportMemberSave = reportMemberRepository.save(reportMember);

        // when
        Optional<ReportMember> findReportMember = reportMemberRepository.findById(reportMember.getId());

        // then
        assertThat(findReportMember.get().getId()).isEqualTo(reportMemberSave.getId());
        assertThat(findReportMember.get().getMember()).isEqualTo(reportMemberSave.getMember());
    }

    @Test
    @DisplayName("신고 리포트 삭제")
    void deleteReport() {
        // given
        ReportMember reportMember = initReportMember();
        ReportMember reportMemberSave = reportMemberRepository.save(reportMember);

        // when
        reportMemberRepository.delete(reportMemberSave);
        Optional<ReportMember> deleteReportMember = reportMemberRepository.findById(reportMember.getId());

        // then
        assertFalse(deleteReportMember.isPresent());
    }

    @Test
    @DisplayName("유저 패널티 수")
    void userPenaltyTest() {
        // given
        ReportMember reportMember = initReportMember();

        // when
        int penaltyCountOne = getInitPenaltyCount(reportMember);
        int penaltyCountTwo = getPenaltyCount(reportMember);
        int penaltyCountThree = getPenaltyCount(reportMember);

        // then
        assertThat(penaltyCountOne).isEqualTo(penaltyOne);
        assertThat(penaltyCountTwo).isEqualTo(penaltyTwo);
        assertThat(penaltyCountThree).isEqualTo(penaltyThree);
    }

    /**
     * 테스트 용 초기화 프로세스 (신고 리포트 생성자 초기화)
     * @return
     */
    private ReportMember initReportMember() {
        ReportMember reportMember = ReportMember.builder().build();
        reportMember.memberReport(save);

        return reportMember;
    }

    /**
     * 테스트 용 초기화 프로세스 (유저 신고 수 초기화)
     * @return
     */
    private Member initProcess() {
        Member member = new Member("윤광오", "swager253@naver.com", "1234", MemberType.NORMAL);
        for(int i = 0; i <= reportTestOne; i++) {
            member.reportCount();
        }

        return memberRepository.save(member);
    }

    /**
     * 테스트 용 초기화 프로세스 (블랙리스트 패널티 수 초기화)
     * @param reportMember
     * @return
     */
    private int getPenaltyCount(ReportMember reportMember) {
        for(int i = 0; i <= reportTestOne; i++) {
            reportMember.getMember().reportCount();
        }

        return getInitPenaltyCount(reportMember);
    }

    /**
     * 테스트 용 초기화 프로세스 (블랙리스트 패널티 초기화)
     * @param reportMember
     * @return
     */
    private int getInitPenaltyCount(ReportMember reportMember) {
        return reportMember.blackListPenaltyCount(reportMember.getMember());
    }
}