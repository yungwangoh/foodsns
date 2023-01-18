package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.blacklist.MemberBlackListDetailDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;

import java.util.List;

public interface MemberBlackListService {

    ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate(String reason, String email);

    ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne(Long id);

    ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList();

    ResponseEntity<MemberBlackListDetailDto> blackListMemberDetailSearch(Long id);

    default ResponseEntity<MemberBlackListResponseDto> blackListMemberBoardDelete() {
        return null;
    }
}
