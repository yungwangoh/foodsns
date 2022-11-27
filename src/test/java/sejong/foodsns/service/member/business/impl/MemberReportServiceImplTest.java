package sejong.foodsns.service.member.business.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.MemberType;
import sejong.foodsns.dto.member.report.MemberReportRequestDto;
import sejong.foodsns.dto.member.report.MemberReportResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberReportService;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.*;

@SpringBootTest
class MemberReportServiceImplTest {

    @Autowired
    private MemberReportServiceImpl memberReportService;

    @Autowired
    private ReportMemberRepository reportMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Success {

        @Test
        @Order(0)
        @DisplayName("신고 회원 저장 -> 신고 수가 10개 미만 : 202 ACCEPTED 반환")
        void reportMemberAcceptedSave() {
            // given
            MemberReportRequestDto memberReportRequestDto = MemberReportRequestDto.builder()
                    .member(memberReportTenOrLessInit())
                    .build();

            // when
            ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate =
                    memberReportService.reportMemberCreate(memberReportRequestDto);

            // then
            assertThat(reportMemberCreate.getStatusCode()).isEqualTo(ACCEPTED);
        }

        @Test
        @Order(1)
        @DisplayName("신고 회원 저장 -> 신고 수가 10개 이상 : 201 CREATE 반환")
        void reportMemberCreateSave() {
            // given
            MemberReportRequestDto memberReportRequestDto = MemberReportRequestDto.builder()
                    .member(memberReportTenOrMoreInit())
                    .build();

            // when
            ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate =
                    memberReportService.reportMemberCreate(memberReportRequestDto);

            // then
            assertThat(reportMemberCreate.getStatusCode()).isEqualTo(CREATED);
        }

        @Test
        @Order(2)
        @DisplayName("신고 회원 찾기")
        void reportMemberFindOne() {
            // given
            MemberReportRequestDto memberReportRequestDto = MemberReportRequestDto.builder()
                    .member(memberReportTenOrMoreInit())
                    .build();

            ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate =
                    memberReportService.reportMemberCreate(memberReportRequestDto);

            // when
            ResponseEntity<Optional<MemberReportResponseDto>> reportMemberFindOne =
                    memberReportService.reportMemberFindOne(getMemberReportResponseDto(reportMemberCreate).getId());

            // then
            assertThat(reportMemberFindOne.getStatusCode()).isEqualTo(OK);
            assertTrue(Objects.requireNonNull(reportMemberFindOne.getBody()).isPresent());
        }

        @AfterEach
        void deleteInit() {
            reportMemberRepository.deleteAll();
        }
    }

    @Nested
    @DisplayName("실패")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Fail {

        @Test
        @Order(0)
        @DisplayName("신고 회원 ACCEPTED 난 후 신고 회원 찾기")
        void reportMemberIsAcceptedAfterFindOne() {
            // given
            MemberReportRequestDto memberReportRequestDto = MemberReportRequestDto.builder()
                    .member(memberReportTenOrLessInit())
                    .build();

            // when
            ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate =
                    memberReportService.reportMemberCreate(memberReportRequestDto);

            // then
            // 찾는 신고 회원이 존재하지 않아야 함.
            assertThatThrownBy(() ->
                    memberReportService.reportMemberFindOne(getMemberReportResponseDto(reportMemberCreate).getId())
            ).isInstanceOf(InvalidDataAccessApiUsageException.class);
        }

        @Test
        @Order(1)
        @DisplayName("신고 회원 저장해야하는데 회원 정보가 없을 경우 예외")
        void reportMemberSaveButNoMember() {
            // given
            MemberReportRequestDto memberReportRequestDto = MemberReportRequestDto.builder()
                    .member(memberNoSave())
                    .build();

            // when

            // then
            assertThatThrownBy(() -> memberReportService.reportMemberCreate(memberReportRequestDto))
                    .isInstanceOf(InvalidDataAccessApiUsageException.class);
        }

        @AfterEach
        void deleteInit() {
            reportMemberRepository.deleteAll();
        }
    }

    private Member memberNoSave() {
        return getMember();
    }

    private Member memberReportTenOrMoreInit() {
        Member member = getMember();

        // 신고 수가 10개 이상
        for(int i = 0; i < 10; i++) member.reportCount();

        return memberRepository.save(member);
    }

    private Member memberReportTenOrLessInit() {
        Member member = getMember();

        // 신고 수가 10개 미만
        for(int i = 0; i < 9; i++) member.reportCount();

        return memberRepository.save(member);
    }

    private Member getMember() {
        Member member = new Member("윤광오", "swager253@naver.com", "rhkddh77@A", NORMAL);
        return member;
    }

    private MemberReportResponseDto getMemberReportResponseDto(ResponseEntity<Optional<MemberReportResponseDto>> reportMemberCreate) {
        return reportMemberCreate.getBody().get();
    }
}