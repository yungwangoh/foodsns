package sejong.foodsns.repository.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReportMemberRepositoryTest {

    @Autowired
    private ReportMemberRepository reportMemberRepository;

    @Test
    @DisplayName("신고 리포트 저장")
    void saveReport() {

    }

    @Test
    @DisplayName("유저 패널티 수 증가")
    void userPenaltyTest() {

    }
}