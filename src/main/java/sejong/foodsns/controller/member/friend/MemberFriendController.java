package sejong.foodsns.controller.member.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.service.member.business.MemberFriendService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class MemberFriendController {

    private final MemberFriendService memberFriendService;

    @PostMapping("/member/friend/{username}")
    ResponseEntity<MemberResponseDto> memberFriendAdd(@RequestBody @Valid MemberRequestDto memberRequestDto,
                                                      @PathVariable("username") String username) {

        ResponseEntity<MemberResponseDto> friendMemberAdd = memberFriendService.friendMemberAdd(memberRequestDto, username);

        return new ResponseEntity<>(getBody(friendMemberAdd), friendMemberAdd.getStatusCode());
    }

    @GetMapping("/member/friend")
    ResponseEntity<MemberResponseDto> memberFriendDetailSearch(@RequestParam("email") String email,
                                                               @RequestParam("index") int index) {
        ResponseEntity<MemberResponseDto> friendMemberDetailSearch = memberFriendService.friendMemberDetailSearch(email, index);

        return new ResponseEntity<>(getBody(friendMemberDetailSearch), friendMemberDetailSearch.getStatusCode());
    }

    @GetMapping("/member/friends")
    ResponseEntity<List<MemberResponseDto>> memberFriendsList(@RequestParam("email") String email) {

        ResponseEntity<List<MemberResponseDto>> friendMemberList = memberFriendService.friendMemberList(email);

        return new ResponseEntity<>(getFriendMemberListBody(friendMemberList), friendMemberList.getStatusCode());
    }

    @DeleteMapping("/member/friends")
    ResponseEntity<MemberResponseDto> memberFriendDelete(@RequestParam("email") String email,
                                                         @RequestParam("index") int index) {

        ResponseEntity<MemberResponseDto> friendMemberDelete = memberFriendService.friendMemberDelete(email, index);

        return new ResponseEntity<>(getBody(friendMemberDelete), friendMemberDelete.getStatusCode());
    }

    private static List<MemberResponseDto> getFriendMemberListBody(ResponseEntity<List<MemberResponseDto>> friendMemberList) {
        return friendMemberList.getBody();
    }

    private static MemberResponseDto getBody(ResponseEntity<MemberResponseDto> friendMemberAdd) {
        return friendMemberAdd.getBody();
    }
}
