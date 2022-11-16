package sejong.foodsns.repository.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BlackListRepositoryTest {

    @Test
    @DisplayName("블랙리스트 등록")
    void blackListMemberCreate() {

    }

    @Test
    @DisplayName("블랙리스트 회원 모든 게시물, 댓글, 대댓글 삭제")
    void blackListMemberAllDelete() {

    }
}