package sejong.foodsns.repository.querydsl.board.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.repository.querydsl.board.BoardQueryDslRepository;

import javax.persistence.EntityManager;
import java.util.List;

import static sejong.foodsns.domain.board.QBoard.board;
import static sejong.foodsns.log.util.querydsl.search.QueryDslSearchUtil.searchOptionCheck;

@Repository
@RequiredArgsConstructor
public class BoardQueryDslRepositoryImpl implements BoardQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 제목과 본문 그리고 제목 또는 본문을 조회하여 관련된 게시물 가져오기.
    @Override
    public List<Board> search(SearchOption searchOption, String content) {
        return jpaQueryFactory
                .selectFrom(board)
                .where(searchOptionCheck(searchOption, content))
                .orderBy(board.createTime.desc())
                .fetch();
    }
}