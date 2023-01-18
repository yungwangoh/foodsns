package sejong.foodsns.controller.member.blacklist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListDetailDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.service.member.business.MemberBlackListService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class MemberBlackListController {

    private final MemberBlackListService memberBlackListService;

    /**
     * 블랙리스트 회원 생성
     * @param memberBlackListCreateRequestDto 블랙리스트 생성 Dto
     * @return 성공 : 블랙리스트 회원 응답 Dto, OK | 실패 : 블랙리스트 회원 응답 Dto, NOT_FOUND | 예외사항 : 신고 수가 10개 미만인 경우 ACCEPT
     */
    @PostMapping("/member/blackList")
    ResponseEntity<MemberBlackListResponseDto> memberBlackListCreate(@RequestBody @Valid MemberBlackListCreateRequestDto memberBlackListCreateRequestDto) {
        ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate =
                memberBlackListService.blackListMemberCreate(memberBlackListCreateRequestDto.getReason(),
                        memberBlackListCreateRequestDto.getMemberRequestDto().getEmail());

        return new ResponseEntity<>(getBody(blackListMemberCreate), blackListMemberCreate.getStatusCode());
    }

    /**
     * 블랙리스트 회원 목록
     * @return 블랙리스트 회원 목록
     */
    @GetMapping("/member/blackLists")
    ResponseEntity<List<MemberBlackListResponseDto>> memberBlackLists() {

        ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList =
                memberBlackListService.blackListMemberList();

        return new ResponseEntity<>(getBlackListMemberListBody(blackListMemberList), blackListMemberList.getStatusCode());
    }

    /**
     * 블랙리스트 회원 찾기
     * @param id 블랙리스트 회원 id
     * @return 성공 : 블랙리스트 회원 응답 Dto, OK | 실패 : 블랙리스트 회원 응답 Dto, NOT_FOUND
     */
    @GetMapping("/member/blackList/{id}")
    ResponseEntity<MemberBlackListResponseDto> memberBlackListSearch(@PathVariable("id") Long id) {
        ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne =
                memberBlackListService.blackListMemberFindOne(id);

        return new ResponseEntity<>(getBody(blackListMemberFindOne), blackListMemberFindOne.getStatusCode());
    }

    /**
     * 블랙리스트 상세 정보 조회
     * @param id 회원 id
     * @return 블랙리스트 회원 상세정보 Dto
     */
    @GetMapping("/member/blackList/search/{id}")
    ResponseEntity<MemberBlackListDetailDto> memberBlackListDetailSearch(@PathVariable("id") Long id) {
        ResponseEntity<MemberBlackListDetailDto> blackListMemberDetailSearch =
                memberBlackListService.blackListMemberDetailSearch(id);

        return new ResponseEntity<>(getBlackListMemberDetailSearchBody(blackListMemberDetailSearch), blackListMemberDetailSearch.getStatusCode());
    }

    /**
     * 블랙리스트 목록 ResponseEntity 해제 후 반환
     * @param blackListMemberList 블랙리스트 회원 리스트
     * @return wrapping 된 블랙리스트 목록
     */
    private static List<MemberBlackListResponseDto> getBlackListMemberListBody(ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList) {
        return blackListMemberList.getBody();
    }

    /**
     * 블랙리스트 ResponseEntity 해제 후 반환
     * @param blackListMember 블랙리스트 응답 Dto
     * @return wrapping 된 블랙리스트
     */
    private static MemberBlackListResponseDto getBody(ResponseEntity<MemberBlackListResponseDto> blackListMember) {
        return blackListMember.getBody();
    }

    /**
     * 블랙리스트 회원 상세 정보 ResponseEntity 해제 후 반환
     * @param blackListMemberDetailSearch
     * @return wrapping 된 블랙리스트
     */
    private static MemberBlackListDetailDto getBlackListMemberDetailSearchBody(ResponseEntity<MemberBlackListDetailDto> blackListMemberDetailSearch) {
        return blackListMemberDetailSearch.getBody();
    }
}
