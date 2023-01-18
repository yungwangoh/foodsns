package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberResponseDto;

public interface MemberBusinessService {

    ResponseEntity<MemberResponseDto> memberRankService(String email);

    ResponseEntity<MemberResponseDto> memberReportCount(String email);

    ResponseEntity<MemberResponseDto> memberBlackListTypeConvert(String email);
    // 게시판이 구현되면 구현할 예정.
    default ResponseEntity<MemberResponseDto> memberRecommendUp(String email) {
        return null;
    }
}
