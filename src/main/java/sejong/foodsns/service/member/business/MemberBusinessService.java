package sejong.foodsns.service.member.business;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

@Service
public interface MemberBusinessService {

    ResponseEntity<MemberResponseDto> memberRankService(MemberRequestDto memberRequestDto);
}
