package sejong.foodsns.service.member.business.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.*;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.repository.member.BlackListRepository;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.repository.member.ReportMemberRepository;
import sejong.foodsns.service.member.business.MemberBlackListService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberBlackListServiceImpl implements MemberBlackListService {

    private final BlackListRepository blackListRepository;
    private final ReportMemberRepository reportMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 블랙리스트 회원 등록
     * @param memberBlackListCreateRequestDto 블랙리스트 회원 DTO
     * @return 성공 : CREATE, 예외 : ACCEPTED (패널티를 초과하지 않은 회원이면 요청은 성공 그러나 처리되지 않음을 명시)
     * 실패 : NOT_FOUND
     */
    @Override
    @Transactional
    public ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate(MemberBlackListCreateRequestDto memberBlackListCreateRequestDto) {

        Optional<ReportMember> reportMember = ofNullable(reportMemberRepository.findById(memberBlackListCreateRequestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("신고 회원이 존재하지 않습니다.")));

        BlackList blackList = new BlackList(memberBlackListCreateRequestDto.getReason(), getReportMember(reportMember));

        if(TheNumberOfPenaltyIsThreeOrMore(reportMember)) {

            BlackList save = blackListProcessAndSave(reportMember, blackList);

            return new ResponseEntity<>(new MemberBlackListResponseDto(save), CREATED);
        }
        else return new ResponseEntity<>(new MemberBlackListResponseDto(blackList), ACCEPTED);
    }

    /**
     * 블랙리스트 회원 찾기
     *
     * @param id 블랙리스트 회원 DTO
     * @return 성공 : OK, 실패 : NOT_FOUND
     */
    @Override
    public ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne(Long id) {

        Optional<BlackList> blackListMember = ofNullable(blackListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("블랙리스트 회원이 존재하지 않습니다.")));

        MemberBlackListResponseDto memberBlackListResponseDto = MemberBlackListResponseDto.builder()
                .blackList(getBlackList(blackListMember))
                .build();

        return new ResponseEntity<>(memberBlackListResponseDto, OK);
    }

    /**
     * 블랙리스트 회원 목록 조회
     *
     * @return 성공 : OK, 실패 : NOT_FOUND
     */
    @Override
    public ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList() {

        List<BlackList> blackLists = blackListRepository.findAll();
        List<MemberBlackListResponseDto> blackListResponseDtos = blackLists.stream()
                .map(MemberBlackListResponseDto::new).collect(toList());

        return new ResponseEntity<>(blackListResponseDtos, OK);
    }

    /**
     * 회원 패널티 개수가 3개 이상
     * @param reportMember 신고 회원
     * @return 패널티 3개 이상 : true, 미만 : false
     */
    private static boolean TheNumberOfPenaltyIsThreeOrMore(Optional<ReportMember> reportMember) {
        return getReportMember(reportMember).getMember().getPenalty() >= MemberNumberOfCount.penalty;
    }

    /**
     * Optional 신고 회원 반환 메서드
     * @param reportMember 신고 회원
     * @return 신고회원 리턴
     */
    private static ReportMember getReportMember(Optional<ReportMember> reportMember) {
        return reportMember.get();
    }

    /**
     * 블랙 리스트 처리와 저장
     * @param reportMember 신고 회원
     * @param blackList 블랙 리스트 회원
     * @return 블랙 리스트 회원 저장
     */
    private BlackList blackListProcessAndSave(Optional<ReportMember> reportMember, BlackList blackList) {
        blackList.blackListMember(getReportMember(reportMember));
        return blackListRepository.save(blackList);
    }

    /**
     * 블랙리스트 회원이 NULL이면 예외, NULL 아니면 회원 Return
     *
     * @param blackListMemberFindOne Response
     * @return requireNonNull 파라미터가 null 이면, npe error 띄움, 아니면 파라미터 그대로 반환.
     */
    private MemberBlackListResponseDto getMember(ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberFindOne) {
        return Objects.requireNonNull(blackListMemberFindOne.getBody())
                .orElseThrow(() -> new IllegalArgumentException("찾는 신고 회원이 없습니다."));
    }

    private static BlackList getBlackList(Optional<BlackList> blackListMember) {
        return blackListMember.get();
    }
}
