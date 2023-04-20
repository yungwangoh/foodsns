package sejong.foodsns.repository.querydsl.board.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sejong.foodsns.domain.board.Board;
import sejong.foodsns.domain.board.QRecommend;
import sejong.foodsns.domain.board.Recommend;
import sejong.foodsns.domain.board.SearchOption;
import sejong.foodsns.domain.member.Member;
import sejong.foodsns.domain.member.QMember;
import sejong.foodsns.repository.querydsl.board.BoardQueryRepository;

import java.util.List;

import static sejong.foodsns.domain.board.QBoard.board;
import static sejong.foodsns.domain.board.QRecommend.*;
import static sejong.foodsns.domain.member.QMember.*;
import static sejong.foodsns.log.util.querydsl.search.QueryDslSearchUtil.searchOptionCheck;

@Repository
@RequiredArgsConstructor
public class BoardQueryDslRepositoryImpl implements BoardQueryRepository {

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

    // 최소 추천 수가 1 이상인 게시물 중 가장 높은 추천수부터 내림차순 정렬
    @Override
    public List<Board> searchByHighestRecommendCount() {
        return jpaQueryFactory
                .selectFrom(board)
                .where(board.recommCount.goe(1))
                .offset(0)
                .limit(3)
                .orderBy(board.recommCount.desc())
                .fetch();
    }

    // 최소 댓글 수가 1 이상인 게시물 중 가장 많은 댓글수부터 내림차순 정렬
    @Override
    public List<Board> searchByHighestCommentCount() {
        return jpaQueryFactory
                .selectFrom(board)
                .where(board.comments.size().goe(1))
                .orderBy(board.recommCount.desc())
                .fetch();
    }

    @Override
    @Transactional
    public void recommendUp(Board b) {
        jpaQueryFactory.update(board)
                .set(board.recommCount, board.recommCount.add(1))
                .where(board.eq(b))
                .execute();
    }

    @Override
    @Transactional
    public void recommendDown(Board b) {
        jpaQueryFactory.update(board)
                .set(board.recommCount, board.recommCount.subtract(1))
                .where(board.eq(b))
                .execute();
    }

    @Override
    public boolean checkRecommendMemberAndBoard(Member m, Board b) {
        List<Recommend> list = jpaQueryFactory.selectFrom(recommend)
                .join(recommend.board, board).fetchJoin()
                .join(recommend.member, member).fetchJoin()
                .where(recommend.board.eq(b).and(recommend.member.eq(m)))
                .fetch();

        return list.size() > 0;
    }
}
