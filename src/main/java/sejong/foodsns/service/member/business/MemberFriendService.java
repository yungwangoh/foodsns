package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.friend.MemberFriendResponseDto;

import java.util.List;

public interface MemberFriendService {

    default ResponseEntity<MemberFriendResponseDto> friendMemberAdd(String email, String friendEmail) {
        return null;
    }

    default ResponseEntity<MemberFriendResponseDto> friendMemberDelete(String email, int index) {
        return null;
    }

    default ResponseEntity<List<MemberFriendResponseDto>> friendMemberList(String email) {
        return null;
    }

    default ResponseEntity<MemberResponseDto> friendMemberDetailSearch(String email, int index) {
        return null;
    }
}
