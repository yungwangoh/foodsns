package sejong.foodsns.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.find.MemberFindDto;
import sejong.foodsns.service.member.business.MemberBusinessService;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberCrudService memberCrudService;
    private final MemberBusinessService memberBusinessService;

    @PostMapping("/member/create")
    public ResponseEntity<MemberResponseDto> memberCreate(@RequestBody @Valid MemberRequestDto memberRequestDto) {

        ResponseEntity<Optional<MemberResponseDto>> memberCreate = memberCrudService.memberCreate(memberRequestDto);

        return new ResponseEntity<>(getMember(memberCreate), memberCreate.getStatusCode());
    }

    @PostMapping("/member/search/password")
    public ResponseEntity<String> memberPasswordSearch(@RequestBody @Valid MemberFindDto memberFindDto) {

        ResponseEntity<Optional<MemberResponseDto>> member = memberCrudService.findMember(memberFindDto.getEmail());

        ResponseEntity<Optional<MemberResponseDto>> passwordUpdate =
                memberCrudService.memberPasswordUpdate(getMember(member).getEmail(), memberFindDto.getPassword());

        return new ResponseEntity<>(PASSWORD_SEARCH_SUCCESS, passwordUpdate.getStatusCode());
    }

    private MemberResponseDto getMember(ResponseEntity<Optional<MemberResponseDto>> member) {
        return getBody(member).get();
    }

    private Optional<MemberResponseDto> getBody(ResponseEntity<Optional<MemberResponseDto>> memberCreate) {
        return memberCreate.getBody();
    }
}
