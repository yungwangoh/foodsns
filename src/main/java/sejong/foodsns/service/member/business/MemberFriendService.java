package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage;

import java.util.List;

public interface MemberFriendService {

    default ResponseEntity<List<MemberResponseDto>> friendMemberAdd(MemberRequestDto memberRequestDto, String username) {
        return null;
    }

    default ResponseEntity<List<MemberResponseDto>> friendMemberDelete(MemberRequestDto memberRequestDto, int index) {
        return null;
    }

    default ResponseEntity<List<MemberResponseDto>> friendMemberList(MemberRequestDto memberRequestDto) {
        return null;
    }

    default ResponseEntity<MemberResponseDto> friendMemberDetailSearch(MemberRequestDto memberRequestDto, int index) {
        return null;
    }
}
