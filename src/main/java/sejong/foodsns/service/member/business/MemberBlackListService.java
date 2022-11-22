package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.foodsns.dto.member.blacklist.MemberBlackListRequestDto;
import sejong.foodsns.dto.member.blacklist.MemberBlackListResponseDto;

import java.util.List;

public interface MemberBlackListService {

    ResponseEntity<MemberBlackListResponseDto> blackListMemberCreate(String reason, MemberBlackListRequestDto memberBlackListRequestDto);

    ResponseEntity<MemberBlackListResponseDto> blackListMemberDelete(MemberBlackListRequestDto memberBlackListRequestDto);

    ResponseEntity<MemberBlackListResponseDto> blackListMemberFindOne(MemberBlackListRequestDto memberBlackListRequestDto);

    ResponseEntity<List<MemberBlackListResponseDto>> blackListMemberList();
}
