package sejong.foodsns.service.member.crud;

import org.springframework.http.ResponseEntity;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;

import java.util.List;
import java.util.Optional;

public interface MemberCrudService {

    ResponseEntity<Optional<MemberResponseDto>> memberCreate(MemberRequestDto memberRequestDto);

    ResponseEntity<Optional<MemberResponseDto>> memberPasswordUpdate(String email, String password);

    ResponseEntity<Optional<MemberResponseDto>> memberNameUpdate(String email, String username);

    ResponseEntity<Optional<MemberResponseDto>> memberDelete(MemberRequestDto memberRequestDto);

    ResponseEntity<Optional<MemberResponseDto>> findMember(String email);

    ResponseEntity<List<MemberResponseDto>> memberList();

    Boolean memberNameExistValidation(MemberRequestDto memberRequestDto);

    Boolean memberEmailExistValidation(MemberRequestDto memberRequestDto);
}
