package sejong.foodsns.controller.member.blacklist;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListDetailDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.log.error.ErrorResult;
import sejong.foodsns.service.member.business.MemberBlackListService;

import javax.validation.Valid;
import java.util.List;

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
    @Operation(summary = "블랙리스트 회원 추가", description = "신고 수가 10개 이상일 경우 블랙리스트로 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "블랙리스트 회원 생성", content = @Content(schema = @Schema(implementation = MemberBlackListResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "블랙리스트 회원 생성 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
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
    @Operation(summary = "블랙리스트 회원 목록 조회", description = "블랙리스트 회원 목록을 조회한다. (목록 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블랙리스트 목록 조회 성공 (실패는 없음)", content = @Content(schema = @Schema(implementation = MemberBlackListResponseDto.class)))
    })
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
    @Operation(summary = "블랙리스트 회원 찾기", description = "id를 통하여 블랙리스트 회원을 조회한다. (단건 조회)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블랙리스트 회원 조회 성공", content = @Content(schema = @Schema(implementation = MemberBlackListResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "블랙리스트 회원 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
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
    @Operation(summary = "블랙리스트 회원의 상세 정보 조회", description = "회원 id를 통하여 블랙리스트 회원의 상세한 정보를 조회한다. (※ 블랙리스트 회원의 id가 아닌 회원 id 이다.)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블랙리스트 회원 상세 조회 성공", content = @Content(schema = @Schema(implementation = MemberBlackListDetailDto.class))),
            @ApiResponse(responseCode = "404", description = "블랙리스트 회원 상세 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
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
