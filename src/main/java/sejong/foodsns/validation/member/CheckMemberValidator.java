package sejong.foodsns.validation.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.repository.member.MemberRepository;
import sejong.foodsns.validation.AbstractValidator;

@RequiredArgsConstructor
@Component
public class CheckMemberValidator extends AbstractValidator<MemberRequestDto> {

    private final MemberRepository memberRepository;

    @Override
    protected void doValidator(MemberRequestDto dto, Errors errors) {
        if(memberRepository.existsMemberByUsername(dto.getUsername())) {
            errors.rejectValue("username", "닉네임 중복 오류", "이미 존재하는 닉네임입니다.");
        }
        if(memberRepository.existsMemberByEmail(dto.getEmail())) {
            errors.rejectValue("email", "이메일 중복 오류", "이미 존재하는 이메일입니다.");
        }
    }
}
