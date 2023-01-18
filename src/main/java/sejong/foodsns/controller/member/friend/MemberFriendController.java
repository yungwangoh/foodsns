package sejong.foodsns.controller.member.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;
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
