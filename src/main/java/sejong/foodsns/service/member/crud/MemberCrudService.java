package sejong.foodsns.service.member.crud;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberCrudService {

    ResponseEntity<Optional<MemberResponseDto>> memberCreate(MemberRequestDto memberRequestDto);

    ResponseEntity<Optional<MemberResponseDto>> memberPasswordUpdate(MemberRequestDto memberRequestDto, String password);

    ResponseEntity<Optional<MemberResponseDto>> memberNameUpdate(MemberRequestDto memberRequestDto, String username);

    ResponseEntity<Optional<MemberResponseDto>> memberDelete(MemberRequestDto memberRequestDto);

    ResponseEntity<Optional<MemberResponseDto>> findMember(MemberRequestDto memberRequestDto);

    ResponseEntity<Optional<List<MemberResponseDto>>> memberList();

    Boolean memberNameExistValidation(MemberRequestDto memberRequestDto);

    Boolean memberEmailExistValidation(MemberRequestDto memberRequestDto);
}
