package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberBlackListService {

    ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate(MemberBlackListCreateRequestDto memberBlackListCreateRequestDto);

    ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne(Long id);

    ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList();

    default ResponseEntity<MemberBlackListResponseDto> blackListMemberBoardDelete() {
        return null;
    }
}
