package sejong.foodsns.controller.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.dto.member.update.MemberUpdatePwdDto;
import sejong.foodsns.dto.member.update.MemberUpdateUserNameDto;
import sejong.foodsns.log.error.ErrorResult;
import sejong.foodsns.service.member.business.MemberBusinessService;
import sejong.foodsns.service.member.crud.MemberCrudService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static sejong.foodsns.service.member.crud.MemberSuccessOrFailedMessage.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class MemberController {

    private final MemberCrudService memberCrudService;
    private final MemberBusinessService memberBusinessService;

    /**
     * 회원 가입
     * @param memberRequestDto
     * @return 회원 정보, CREATE
     */
    @Operation(summary = "회원 가입", description = "이메일, 닉네임, 비밀번호로 회원 가입을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class)))
    })
    @PostMapping("/member")
    public ResponseEntity<MemberResponseDto> memberCreate(@RequestBody @Valid MemberRequestDto memberRequestDto) {

        ResponseEntity<Optional<MemberResponseDto>> memberCreate = memberCrudService.memberCreate(memberRequestDto);
        log.info("memberCreate = {}", memberCreate);

        return new ResponseEntity<>(getMember(memberCreate), memberCreate.getStatusCode());
    }

    /**
     * 회원 조회
     * @param email 회원 이메일
     * @return 회원 정보, OK
     */
    @Operation(summary = "회원 조회", description = "이메일로 회원을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 조회 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class)))
    })
    @GetMapping("/member/{email}")
    public ResponseEntity<MemberResponseDto> memberSearch(@PathVariable("email") String email) {

        ResponseEntity<Optional<MemberResponseDto>> member = memberCrudService.findMember(email);

        return new ResponseEntity<>(getMember(member), member.getStatusCode());
    }

    /**
     * 회원 비밀번호 수정
     * @param memberUpdatePwdDto
     * @return 비밀번호 수정 완료, OK
     */
    @Operation(summary = "회원 비밀번호 수정", description = "회원 비밀번호를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 비밀번호 수정 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PatchMapping("/member/password")
    public ResponseEntity<String> memberUpdatePassword(@RequestBody @Valid MemberUpdatePwdDto memberUpdatePwdDto) {

        ResponseEntity<Optional<MemberResponseDto>> passwordUpdate =
                memberCrudService.memberPasswordUpdate(memberUpdatePwdDto.getEmail(), memberUpdatePwdDto.getPassword());

        // 비밀번호 찾기 기능은 일반적으로 수정이 되어야한다. 그 때문에 수정 메세지를 리턴하는 것이다.
        return new ResponseEntity<>(PASSWORD_SEARCH_SUCCESS, passwordUpdate.getStatusCode());
    }

    /**
     * 회원 닉네임 수정
     * @param memberUpdateUserNameDto
     * @return 닉네임 수정 완료, OK
     */
    @Operation(summary = "회원 닉네임 수정", description = "회원 닉네임을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 닉네임 수정 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PatchMapping("/member/username")
    public ResponseEntity<String> memberUpdateUsername(@RequestBody @Valid MemberUpdateUserNameDto memberUpdateUserNameDto) {

        ResponseEntity<Optional<MemberResponseDto>> nameUpdate =
                memberCrudService.memberNameUpdate(memberUpdateUserNameDto.getEmail(), memberUpdateUserNameDto.getUsername());

        return new ResponseEntity<>(USERNAME_UPDATE_SUCCESS, nameUpdate.getStatusCode());
    }

    /**
     * 회원 목록 조회
     * @return 회원 목록, OK
     */
    @Operation(summary = "회원 목록 조회", description = "회원 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> members() {

        ResponseEntity<List<MemberResponseDto>> memberList = memberCrudService.memberList();

        return new ResponseEntity<>(getMemberResponseDtos(memberList), memberList.getStatusCode());
    }

    /**
     * 회원 탈퇴 (삭제)
     * @param memberRequestDto
     * @return 회원 삭제 완료, OK
     */
    @Operation(summary = "회원 탈퇴", description = "회원 이메일로 회원 탈퇴를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/member")
    public ResponseEntity<String> memberDelete(@RequestBody @Valid MemberRequestDto memberRequestDto) {

        ResponseEntity<Optional<MemberResponseDto>> memberDelete = memberCrudService.memberDelete(memberRequestDto);

        return new ResponseEntity<>(USER_DELETE_SUCCESS, memberDelete.getStatusCode());
    }

    /**
     * 회원 이메일 중복 검사
     * @param memberRequestDto
     * @return 중복을 찾는데에 성공하면 True 와 OK, 실패하면 False 와 NOT_FOUND
     * 혼동이 있을 수도 있으니, 후에 테스트를 하여 수정하겠음.
     */
    @Operation(summary = "회원 이메일 중복 검사", description = "회원 이메일로 이메일 중복을 검증한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 이메일 중복 검사 성공", content = @Content(schema = @Schema(implementation = String.class))),
    })
    @PostMapping("/member/duplicated/email")
    public ResponseEntity<String> memberDuplicatedEmailCheck(@RequestBody @Valid MemberRequestDto memberRequestDto) {

        Boolean emailExistValidation = memberCrudService.memberEmailExistValidation(memberRequestDto);

        if(emailExistValidation) {
            return new ResponseEntity<>(emailExistValidation.toString(), OK);
        } else {
            throw new IllegalArgumentException(emailExistValidation.toString());
        }
    }

    /**
     * 회원 닉네임(유저이름) 중복 검사
     * @param memberRequestDto
     * @return 중복을 찾는데에 성공하면 True 와 OK, 실패하면 False 와 NOT_FOUND
     * 혼동이 있을 수도 있으니, 후에 테스트를 하여 수정하겠음.
     */
    @Operation(summary = "회원 닉네임 중복 검사", description = "회원 닉네임으로 닉네임 중복 검증을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 닉네임 중복 검사 성공", content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping("/member/duplicated/username")
    public ResponseEntity<String> memberDuplicatedNameCheck(@RequestBody @Valid MemberRequestDto memberRequestDto) {

        Boolean nameExistValidation = memberCrudService.memberNameExistValidation(memberRequestDto);

        if(nameExistValidation) {
            return new ResponseEntity<>(nameExistValidation.toString(), OK);
        } else {
            throw new IllegalArgumentException(nameExistValidation.toString());
        }
    }

    /**
     * 회원 랭크 업데이트
     * @param email 회원 이메일
     * @return 회원 응답 Dto
     */
    @Operation(summary = "회원 랭크 업데이트", description = "이메일로 회원 랭크를 업데이트 시킨다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 랭크 업데이트 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class)))
    })
    @GetMapping("/member/rank/{email}")
    public ResponseEntity<MemberResponseDto> memberRankUp(@PathVariable("email") String email) {
        ResponseEntity<MemberResponseDto> memberRankService = memberBusinessService.memberRankService(email);

        return new ResponseEntity<>(memberRankService.getBody(), memberRankService.getStatusCode());
    }

    /**
     * 회원 블랙리스트로 타입 변경
     * @param email 회원 이메일
     * @return 회원 응답 Dto
     */
    @Operation(summary = "회원 블랙리스트 타입 변경", description = "회원은 일반 타입, 관리자 타입, 블랙리스트 타입이 존재한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 블랙리스트 타입 변경 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class)))
    })
    @GetMapping("/member/blackList/type/{email}")
    public ResponseEntity<MemberResponseDto> memberBlackListTypeConvert(@PathVariable("email") String email) {
        ResponseEntity<MemberResponseDto> blackListTypeConvert = memberBusinessService.memberBlackListTypeConvert(email);

        return new ResponseEntity<>(blackListTypeConvert.getBody(), blackListTypeConvert.getStatusCode());
    }

    /**
     * 회원 추천 수 증가
     * @param email 회원 이메일
     * @return 회원 응답 Dto
     */
    @Operation(summary = "회원 추천 수 증가", description = "회원 추천 수를 증가시킨다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 추천 수 증가 성공", content = @Content(schema = @Schema(implementation = MemberResponseDto.class)))
    })
    @GetMapping("/member/recommend/{email}")
    public ResponseEntity<MemberResponseDto> memberRecommendUp(@PathVariable("email") String email) {
        ResponseEntity<MemberResponseDto> memberRecommendUp = memberBusinessService.memberRecommendUp(email);

        return new ResponseEntity<>(memberRecommendUp.getBody(), memberRecommendUp.getStatusCode());
    }

    /**
     * 회원 목록 Optional Wrapping 해제 후 반환
     * @param memberList
     * @return 회원 목록
     */
    private static List<MemberResponseDto> getMemberResponseDtos(ResponseEntity<List<MemberResponseDto>> memberList) {
        return memberList.getBody();
    }

    /**
     * 회원 응답 Dto Optional Wrapping 해제 후 반환
     * @param member
     * @return 회원 응답 Dto
     */
    private static MemberResponseDto getMember(ResponseEntity<Optional<MemberResponseDto>> member) {
        return getBody(member).get();
    }

    /**
     * ResponseEntity Wrapping 해제 후 반환
     * @param memberCreate
     * @return Optionally Wrapped 회원 응답 Dto
     */
    private static Optional<MemberResponseDto> getBody(ResponseEntity<Optional<MemberResponseDto>> memberCreate) {
        return memberCreate.getBody();
    }
}
