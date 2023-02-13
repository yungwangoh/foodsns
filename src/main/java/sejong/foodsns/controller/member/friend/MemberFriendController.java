package sejong.foodsns.controller.member.friend;

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
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;
import sejong.foodsns.log.error.ErrorResult;
import sejong.foodsns.service.member.business.MemberFriendService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class MemberFriendController {

    private final MemberFriendService memberFriendService;

    /**
     * 친구 추가
     * @param email 본인 이메일
     * @param friendUsername 본인 친구리스트에 추가할 친구 닉네임
     * @return 성공 : 친구 정보 응답 Dto, CREATE | 실패 : 친구 정보 응답 Dto, NOT_FOUND
     */
    @Operation(summary = "친구 추가", description = "본인 이메일과 본인 친구리스트에 추가할 친구 닉네임으로 추가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "친구 생성", content = @Content(schema = @Schema(implementation = MemberFriendResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "친구 생성 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @PostMapping("/member/friend")
    ResponseEntity<MemberFriendResponseDto> memberFriendAdd(@RequestParam("email") String email,
                                                      @RequestParam("friendUsername") String friendUsername) {

        ResponseEntity<MemberFriendResponseDto> friendMemberAdd = memberFriendService.friendMemberAdd(email, friendUsername);

        return new ResponseEntity<>(getBody(friendMemberAdd), friendMemberAdd.getStatusCode());
    }

    /**
     * 친구 조회
     * @param email 본인 이메일
     * @param index 친구 리스트의 번호 (index)
     * @return 성공 : 친구 정보, OK | 실패 : 친구 정보, NOT_FOUND
     */
    @Operation(summary = "친구 조회", description = "본인 이메일과 친구 리스트의 index로 친구를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "친구 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @GetMapping("/member/friend")
    ResponseEntity<MemberResponseDto> memberFriendDetailSearch(@RequestParam("email") String email,
                                                               @RequestParam("index") int index) {
        ResponseEntity<MemberResponseDto> friendMemberDetailSearch = memberFriendService.friendMemberDetailSearch(email, index);

        return new ResponseEntity<>(getMemberResponseDto(friendMemberDetailSearch), friendMemberDetailSearch.getStatusCode());
    }

    /**
     * 친구 목록
     * @param email 본인 이메일
     * @return 성공 : 친구 목록, OK
     */
    @Operation(summary = "친구 목록", description = "본인 이메일로 친구 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 목록 조회 성공", content = @Content(schema = @Schema(implementation = MemberFriendResponseDto.class)))
    })
    @GetMapping("/member/friends")
    ResponseEntity<List<MemberFriendResponseDto>> memberFriendsList(@RequestParam("email") String email) {

        ResponseEntity<List<MemberFriendResponseDto>> friendMemberList = memberFriendService.friendMemberList(email);

        return new ResponseEntity<>(getFriendMemberListBody(friendMemberList), friendMemberList.getStatusCode());
    }

    /**
     * 친구 삭제
     * @param email 본인 이메일
     * @param index 친구 리스트의 번호 (index)
     * @return 성공 : 삭제된 친구의 정보, OK | 실패 :
     */
    @Operation(summary = "친구 삭제", description = "본인 이메일과 친구 리스트의 번호(index)로 친구 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 삭제 성공", content = @Content(schema = @Schema(implementation = MemberFriendResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "친구 삭제 실패", content = @Content(schema = @Schema(implementation = ErrorResult.class)))
    })
    @DeleteMapping("/member/friends")
    ResponseEntity<MemberFriendResponseDto> memberFriendDelete(@RequestParam("email") String email,
                                                         @RequestParam("index") int index) {

        ResponseEntity<MemberFriendResponseDto> friendMemberDelete = memberFriendService.friendMemberDelete(email, index);

        return new ResponseEntity<>(getBody(friendMemberDelete), friendMemberDelete.getStatusCode());
    }

    private static List<MemberFriendResponseDto> getFriendMemberListBody(ResponseEntity<List<MemberFriendResponseDto>> friendMemberList) {
        return friendMemberList.getBody();
    }

    private static MemberFriendResponseDto getBody(ResponseEntity<MemberFriendResponseDto> friendMemberAdd) {
        return friendMemberAdd.getBody();
    }

    private static MemberResponseDto getMemberResponseDto(ResponseEntity<MemberResponseDto> friendMemberDetailSearch) {
        return friendMemberDetailSearch.getBody();
    }
}
