package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

public interface MemberBusinessService {

    ResponseEntity<MemberResponseDto> memberRankService(MemberRequestDto memberRequestDto);

    ResponseEntity<MemberResponseDto> memberReportCount(MemberRequestDto memberRequestDto);

    ResponseEntity<MemberResponseDto> memberBlackListPenaltyCount(MemberRequestDto memberRequestDto);

    // 게시판이 구현되면 구현할 예정.
    ResponseEntity<MemberResponseDto> memberRecommendCount(int recommendCount);
}
