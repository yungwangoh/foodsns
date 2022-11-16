package sejong.foodsns.service.member.login.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.service.member.login.MemberLoginService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberLoginServiceImpl implements MemberLoginService {

    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<MemberResponseDto> login(MemberRequestDto memberRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<MemberResponseDto> logout(MemberRequestDto memberRequestDto) {
        return null;
    }
}
