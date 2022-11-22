package sejong.foodsns.service.member.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.List;

public interface MemberCrudService {

    ResponseEntity<MemberResponseDto> memberCreate(MemberRequestDto memberRequestDto);

    ResponseEntity<MemberResponseDto> memberPasswordUpdate(MemberRequestDto memberRequestDto, String password);

    ResponseEntity<MemberResponseDto> memberNameUpdate(MemberRequestDto memberRequestDto, String username);

    ResponseEntity<MemberResponseDto> memberDelete(MemberRequestDto memberRequestDto);

    ResponseEntity<MemberResponseDto> findMember(MemberRequestDto memberRequestDto);

    ResponseEntity<List<MemberResponseDto>> memberList();
}
