package sejong.foodsns.controller.member.blacklist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
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
        ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberCreate =
                memberBlackListService.blackListMemberCreate(memberBlackListCreateRequestDto);

        return new ResponseEntity<>(getBody(blackListMemberCreate).get(), blackListMemberCreate.getStatusCode());
    }

    /**
     * 블랙리스트 회원 목록
     * @return 블랙리스트 회원 목록
     */
    @GetMapping("/member/blackLists")
    ResponseEntity<List<MemberBlackListResponseDto>> memberBlackLists() {

        ResponseEntity<Optional<List<MemberBlackListResponseDto>>> blackListMemberList =
                memberBlackListService.blackListMemberList();

        return new ResponseEntity<>(getBlackListMemberListBody(blackListMemberList).get(), blackListMemberList.getStatusCode());
    }

    /**
     * 블랙리스트 회원 찾기
     * @param id 블랙리스트 회원 id
     * @return 성공 : 블랙리스트 회원 응답 Dto, OK | 실패 : 블랙리스트 회원 응답 Dto, NOT_FOUND
     */
    @GetMapping("/member/blackList/{id}")
    ResponseEntity<MemberBlackListResponseDto> memberBlackListSearch(@PathVariable("id") Long id) {
        ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberFindOne =
                memberBlackListService.blackListMemberFindOne(id);

        return new ResponseEntity<>(getBody(blackListMemberFindOne).get(), blackListMemberFindOne.getStatusCode());
    }

    /**
     * 블랙리스트 목록 ResponseEntity 해제 후 반환
     * @param blackListMemberList
     * @return Optional wrapping 된 블랙리스트 목록
     */
    private static Optional<List<MemberBlackListResponseDto>> getBlackListMemberListBody(ResponseEntity<Optional<List<MemberBlackListResponseDto>>> blackListMemberList) {
        return blackListMemberList.getBody();
    }

    /**
     * 블랙리스트 ResponseEntity 해제 후 반환
     * @param blackListMemberCreate
     * @return Optional wrapping 된 블랙리스트
     */
    private static Optional<MemberBlackListResponseDto> getBody(ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberCreate) {
        return blackListMemberCreate.getBody();
    }
}
