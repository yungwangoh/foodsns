package sejong.foodsns.service.member.crud.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import sejong.foodsns.dto.member.MemberRequestDto;
import sejong.foodsns.dto.member.MemberResponseDto;
import sejong.foodsns.repository.member.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
class MemberCrudServiceImplTest {

    @InjectMocks
    private MemberCrudServiceImpl memberCrudService;
    @Mock
    private MemberRepository memberRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private MemberRequestDto memberRequestDto;


    @BeforeEach
    void memberRequestInit() {
        memberCrudService = new MemberCrudServiceImpl(memberRepository, passwordEncoder);

        memberRequestDto = MemberRequestDto.builder()
                .username("윤광오")
                .email("swager253@naver.com")
                .password("rhkddh77@A")
                .build();
    }

    @Test
    @DisplayName("회원 가입")
    void memberCreate() {
        ResponseEntity<MemberResponseDto> memberCreate = memberCrudService.memberCreate(memberRequestDto);

        if (memberCreate.getStatusCode().equals(CREATED)) {
            assertThat(memberCreate.getBody()).isEqualTo(memberRequestDto);
        }
    }

    @Test
    void memberPasswordUpdate() {
    }

    @Test
    void memberNameUpdate() {
    }

    @Test
    void memberDelete() {
    }

    @Test
    void findMember() {
    }

    @Test
    void memberList() {
    }
}