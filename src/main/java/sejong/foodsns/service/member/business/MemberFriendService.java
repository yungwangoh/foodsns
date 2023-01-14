package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.List;

public interface MemberFriendService {

    default ResponseEntity<MemberResponseDto> friendMemberAdd(String email, String friendEmail) {
        return null;
    }

    default ResponseEntity<MemberResponseDto> friendMemberDelete(String email, int index) {
        return null;
    }

    default ResponseEntity<List<MemberResponseDto>> friendMemberList(String email) {
        return null;
    }

    default ResponseEntity<MemberResponseDto> friendMemberDetailSearch(String email, int index) {
        return null;
    }
}
