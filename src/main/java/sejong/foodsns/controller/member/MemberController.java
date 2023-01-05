package sejong.foodsns.controller.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.find.MemberFindDto;
import sejong.foodsns.dto.member.update.MemberUpdatePwdDto;
import sejong.foodsns.dto.member.update.MemberUpdateUserNameDto;
import sejong.foodsns.service.member.business.MemberBusinessService;
import sejong.foodsns.service.member.crud.MemberCrudService;
import sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberCrudService memberCrudService;
    private final MemberBusinessService memberBusinessService;

    /**
     * 회원 가입
     * @param memberRequestDto
     * @return 회원 정보, CREATE
     */
    @PostMapping("/member/create")
    public ResponseEntity<MemberResponseDto> memberCreate(@RequestBody @Valid MemberRequestDto memberRequestDto) {

        ResponseEntity<Optional<MemberResponseDto>> memberCreate = memberCrudService.memberCreate(memberRequestDto);

        return new ResponseEntity<>(getMember(memberCreate), memberCreate.getStatusCode());
    }

    /**
     * 회원 조회
     * @param memberFindDto
     * @return 회원 정보, OK
     */
    @PostMapping("/member/search")
    public ResponseEntity<MemberResponseDto> memberSearch(@RequestBody @Valid MemberFindDto memberFindDto) {

        ResponseEntity<Optional<MemberResponseDto>> member = memberCrudService.findMember(memberFindDto.getEmail());

        return new ResponseEntity<>(getMember(member), member.getStatusCode());
    }

    /**
     * 회원 비밀번호 수정
     * @param memberUpdatePwdDto
     * @return 비밀번호 수정 완료, OK
     */
    @PatchMapping("/member/update/password")
    public ResponseEntity<String> memberUpdatePassword(@RequestBody @Valid MemberUpdatePwdDto memberUpdatePwdDto) {

        ResponseEntity<Optional<MemberResponseDto>> passwordUpdate =
                memberCrudService.memberPasswordUpdate(memberUpdatePwdDto.getEmail(), memberUpdatePwdDto.getPassword());

        return new ResponseEntity<>(PASSWORD_SEARCH_SUCCESS, passwordUpdate.getStatusCode());
    }

    /**
     * 회원 닉네임 수정
     * @param memberUpdateUserNameDto
     * @return 닉네임 수정 완료, OK
     */
    @PatchMapping("/member/update/username")
    public ResponseEntity<String> memberUpdateUsername(@RequestBody @Valid MemberUpdateUserNameDto memberUpdateUserNameDto) {

        ResponseEntity<Optional<MemberResponseDto>> nameUpdate =
                memberCrudService.memberNameUpdate(memberUpdateUserNameDto.getEmail(), memberUpdateUserNameDto.getUsername());

        return new ResponseEntity<>(USERNAME_UPDATE_SUCCESS, nameUpdate.getStatusCode());
    }

    /**
     * 회원 목록 조회
     * @return 회원 목록, OK
     */
    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> members() {

        ResponseEntity<Optional<List<MemberResponseDto>>> memberList = memberCrudService.memberList();

        return new ResponseEntity<>(getMemberResponseDtos(memberList), memberList.getStatusCode());
    }

    /**
     * 회원 탈퇴 (삭제)
     * @param memberRequestDto
     * @return 회원 삭제 완료, OK
     */
    @DeleteMapping("/member/delete")
    public ResponseEntity<String> memberDelete(@RequestBody @Valid MemberRequestDto memberRequestDto) {

        ResponseEntity<Optional<MemberResponseDto>> memberDelete = memberCrudService.memberDelete(memberRequestDto);

        return new ResponseEntity<>(USER_DELETE_SUCCESS, memberDelete.getStatusCode());
    }

    /**
     * 회원 목록 Optional Wrapping 해제 후 반환
     * @param memberList
     * @return 회원 목록
     */
    private List<MemberResponseDto> getMemberResponseDtos(ResponseEntity<Optional<List<MemberResponseDto>>> memberList) {
        return memberList.getBody().get();
    }

    /**
     * 회원 응답 Dto Optional Wrapping 해제 후 반환
     * @param member
     * @return 회원 응답 Dto
     */
    private MemberResponseDto getMember(ResponseEntity<Optional<MemberResponseDto>> member) {
        return getBody(member).get();
    }

    /**
     * ResponseEntity Wrapping 해제 후 반환
     * @param memberCreate
     * @return Optionally Wrapped 회원 응답 Dto
     */
    private Optional<MemberResponseDto> getBody(ResponseEntity<Optional<MemberResponseDto>> memberCreate) {
        return memberCreate.getBody();
    }
}
