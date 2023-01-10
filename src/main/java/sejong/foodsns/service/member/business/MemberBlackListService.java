package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.blacklist.MemberBlackListCreateRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberBlackListService {

    ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberCreate(MemberBlackListCreateRequestDto memberBlackListCreateRequestDto);

    default ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberTypeChange(MemberBlackListRequestDto memberBlackListRequestDto) {
        return null;
    }

    ResponseEntity<Optional<MemberBlackListResponseDto>> blackListMemberFindOne(MemberBlackListRequestDto memberBlackListRequestDto);

    ResponseEntity<Optional<List<MemberBlackListResponseDto>>> blackListMemberList();
}
