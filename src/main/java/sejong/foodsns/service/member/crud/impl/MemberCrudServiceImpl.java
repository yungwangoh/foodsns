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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
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
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberCreate(MemberRequestDto memberRequestDto) {

        Member member = Member.builder()
                .username(memberRequestDto.getUsername())
                .email(memberRequestDto.getEmail())
                .password(passwordEncoder.encode(memberRequestDto.getPassword()))
                .memberType(NORMAL)
                .build();

        Member save = memberRepository.save(member);
        return new ResponseEntity<>(of(new MemberResponseDto(save)), CREATED);
    }

    /**
     * 회원 비밀번호 수정 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @param password
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberPasswordUpdate(MemberRequestDto memberRequestDto, String password) {

        Optional<Member> member = getMemberReturnOptionalMember(memberRequestDto);

        Member updateMember = getMember(member).memberPasswordUpdate(passwordEncoder.encode(password));

        return new ResponseEntity<>(of(new MemberResponseDto(updateMember)), OK);
    }

    /**
     * 회원 이름 수정 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @param username
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberNameUpdate(MemberRequestDto memberRequestDto, String username) {

        Optional<Member> member = getMemberReturnOptionalMember(memberRequestDto);

        Member updateMember = getMember(member).memberNameUpdate(username);

        return new ResponseEntity<>(of(new MemberResponseDto(updateMember)), OK);
    }

    /**
     * 회원 탈퇴 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<Optional<MemberResponseDto>> memberDelete(MemberRequestDto memberRequestDto) {

        Optional<Member> member = getMemberReturnOptionalMember(memberRequestDto);

        memberRepository.delete(getMember(member));

        return new ResponseEntity<>(OK);
    }

    /**
     * 회원 찾기 -> 성공 200, 실패 404
     * @param memberRequestDto
     * @return
     */
    @Override
    public ResponseEntity<Optional<MemberResponseDto>> findMember(MemberRequestDto memberRequestDto) {

        Optional<Member> member = getMemberReturnOptionalMember(memberRequestDto);

        return new ResponseEntity<>(of(new MemberResponseDto(getMember(member))), OK);
    }

    /**
     * 맴버 목록 -> 성공 200
     * @return
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
     * Optional Member -> return member
     * @param member
     * @return
     */
    private Member getMember(Optional<Member> member) {
        return member.get();
    }

    private Optional<Member> getMemberReturnOptionalMember(MemberRequestDto memberRequestDto) {
        Optional<Member> member = of(memberRepository.findMemberByEmail(memberRequestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다.")));

        return member;
    }
}
