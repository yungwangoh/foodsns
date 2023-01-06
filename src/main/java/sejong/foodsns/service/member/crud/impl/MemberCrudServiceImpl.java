package sejong.foodsns.service.member.crud.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.crud.MemberCrudService;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.*;
import static sejong.foodsns.domain.member.MemberType.NORMAL;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberCrudServiceImpl implements MemberCrudService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 생성 -> 성공 201, 실패 404
     * @param memberRequestDto
     * @return 회원 DTO, HTTP CREATED
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberCreate(MemberRequestDto memberRequestDto) {

        duplicatedCheckLogic(memberRequestDto);

        Member member = memberClassCreated(memberRequestDto);

        Member save = memberRepository.save(member);
        return new ResponseEntity<>(of(new MemberResponseDto(save)), CREATED);
    }

    /**
     * 회원 비밀번호 수정 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @param password
     * @return 회원 DTO, HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberPasswordUpdate(String email, String password) {

        Optional<Member> member = getMemberReturnOptionalMember(email);

        Member updateMember = getMember(member).memberPasswordUpdate(passwordEncoder.encode(password));

        Member save = memberRepository.save(updateMember);

        return new ResponseEntity<>(of(new MemberResponseDto(save)), OK);
    }

    /**
     * 회원 이름 수정 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @param username
     * @return 회원 DTO, HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberNameUpdate(String email, String username) {

        Optional<Member> member = getMemberReturnOptionalMember(email);

        Member updateMember = getMember(member).memberNameUpdate(username);

        Member save = memberRepository.save(updateMember);

        return new ResponseEntity<>(of(new MemberResponseDto(save)), OK);
    }

    /**
     * 회원 탈퇴 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @return HTTP OK
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberDelete(MemberRequestDto memberRequestDto) {

        Optional<Member> member = getMemberReturnOptionalMember(memberRequestDto.getEmail());

        passwordMatchCheck(memberRequestDto, member);

        return new ResponseEntity<>(NO_CONTENT);
    }

    /**
     * 회원 찾기 -> 성공 200, 실패 404
     * @param email
     * @return 회원, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<MemberResponseDto>> findMember(String email) {

        Optional<Member> member = getMemberReturnOptionalMember(email);

        return new ResponseEntity<>(of(new MemberResponseDto(getMember(member))), OK);
    }

    /**
     * 맴버 목록 -> 성공 200
     * @return 회원 리스트, HTTP OK
     */
    @Override
    public ResponseEntity<Optional<List<MemberResponseDto>>> memberList() {

        List<Member> members = memberRepository.findAll();

        Optional<List<MemberResponseDto>> collect = of(members.stream()
                .map(MemberResponseDto::new)
                .collect(toList()));

        return new ResponseEntity<>(collect, OK);
    }

    /**
     * 회원 이름 중복 검사
     * @param memberRequestDto
     * @return 중복 true, 아니면 false
     */
    @Override
    public Boolean memberNameExistValidation(MemberRequestDto memberRequestDto) {
        return memberRepository.existsMemberByUsername(memberRequestDto.getUsername());
    }

    /**
     * 회원 이메일 중복 검사
     * @param memberRequestDto
     * @return 중복이면 true, 아니면 false
     */
    @Override
    public Boolean memberEmailExistValidation(MemberRequestDto memberRequestDto) {
        return memberRepository.existsMemberByEmail(memberRequestDto.getEmail());
    }

    /**
     * Optional Member -> return member
     * @param member
     * @return 회원
     */
    private Member getMember(Optional<Member> member) {
        return member.get();
    }

    /**
     * 회원 찾고 반환하는 로직 Optional
     * @param email
     * @return 회원 존재 X -> Exception
     */
    private Optional<Member> getMemberReturnOptionalMember(String email) {
        return of(memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));
    }

    /**
     * 회원 중복 검증
     * @param memberRequestDto
     */
    private void duplicatedCheckLogic(MemberRequestDto memberRequestDto) {
        Boolean duplicatedCheck = memberRepository.existsMemberByEmail(memberRequestDto.getEmail());
        if(duplicatedCheck) 
            throw new IllegalArgumentException("중복된 회원입니다.");
    }

    /**
     * 비밀번호 체크 로직
     * @param memberRequestDto
     * @param member
     */
    private void passwordMatchCheck(MemberRequestDto memberRequestDto, Optional<Member> member) {
        boolean matches = passwordEncoder.matches(memberRequestDto.getPassword(), member.get().getPassword());
        if(matches) {
            memberRepository.delete(getMember(member));
        } else {
            throw new IllegalArgumentException("비밀번호가 동일하지 않습니다.");
        }
    }

    /**
     * 회원 객체 생성
     * @param memberRequestDto
     * @return 회원
     */
    private Member memberClassCreated(MemberRequestDto memberRequestDto) {
        return Member.builder()
                .username(memberRequestDto.getUsername())
                .email(memberRequestDto.getEmail())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .memberType(NORMAL)
                .build();
    }
}
