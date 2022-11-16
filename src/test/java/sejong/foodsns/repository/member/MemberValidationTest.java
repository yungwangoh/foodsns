package sejong.foodsns.repository.member;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sejong.foodsns.dto.member.MemberRequestDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class MemberValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("회원 이름 (특수 문자를 제외한 2 ~ 10자 허용, blank 허용 x)")
    void memberNameValidation() {

        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("@@")
                .email("swager253@naver.com")
                .password("rhkddh77@")
                .build();

        // when (유효하지 않을 경우 유효성 문자열을 가지고 있다.)
        Set<ConstraintViolation<MemberRequestDto>> validate =
                validator.validate(memberRequestDto);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("닉네임은 특수문자를 제외한 2 ~ 10자리여야 합니다.");
        });
    }

    @Test
    @DisplayName("회원 이메일 (이메일 형식에 맞아야 100자 이하 허용, blank 허용 x)")
    void memberEmailValidation() {

        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("qkfks1234")
                .email("test")
                .password("rhkddh77@")
                .build();

        // when
        Set<ConstraintViolation<MemberRequestDto>> validate =
                validator.validate(memberRequestDto);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("올바른 형식의 이메일 주소여야 합니다");
        });
    }

    @Test
    @DisplayName("회원 비밀번호 (8 ~ 16자 영문 대 소문자, 숫자, 특수문자 허용, blank 허용 x)")
    void memberPasswordValidation() {
        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("qkfks1234")
                .email("swager253@naver.com")
                .password("r")
                .build();

        // when
        Set<ConstraintViolation<MemberRequestDto>> validate =
                validator.validate(memberRequestDto);

        // then
        assertThat(validate).isNotEmpty();
        validate.forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("비밀번호는 8 ~ 16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.");
        });
    }

    @Test
    @DisplayName("모든 유효성 성공")
    void memberValidationSuccess() {

        // given
        MemberRequestDto memberRequestDto = MemberRequestDto.builder()
                .username("qkfks1234")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();

        // when
        Set<ConstraintViolation<MemberRequestDto>> validate =
                validator.validate(memberRequestDto);

        // then
        assertThat(validate).isEmpty();
    }
}
