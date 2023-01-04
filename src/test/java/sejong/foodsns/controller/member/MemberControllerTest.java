package sejong.foodsns.controller.member;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sejong.foodsns.dto.member.MemberRequestDto;

@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MemberController memberController;
    private MemberRequestDto memberRequestDto;

    @BeforeEach
    void init() {

    }
}