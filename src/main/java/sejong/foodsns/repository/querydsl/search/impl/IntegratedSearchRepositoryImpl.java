package sejong.foodsns.repository.querydsl.search.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.*;
import sejong.foodsns.domain.member.QMember;
import sejong.foodsns.log.util.querydsl.search.QueryDslSearchUtil;
import sejong.foodsns.repository.querydsl.search.IntegratedSearchRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;
import static sejong.foodsns.domain.board.QBoard.*;
import static sejong.foodsns.domain.board.QComment.*;
import static sejong.foodsns.domain.board.QReply.*;
import static sejong.foodsns.domain.member.QMember.*;

@Repository
@RequiredArgsConstructor
public class IntegratedSearchRepositoryImpl implements IntegratedSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> boardIntegratedSearch(String content) {
        return jpaQueryFactory.selectFrom(board)
                .join(board.member, member).fetchJoin()
                .where(QueryDslSearchUtil.boardIntegratedSearch(content))
                .fetch();
    }

    @Override
    public List<Comment> commentIntegratedSearch(String content) {
        return jpaQueryFactory.selectFrom(comment)
                .join(comment.board, board).fetchJoin()
                .join(comment.member, member).fetchJoin()
                .where(QueryDslSearchUtil.commentIntegratedSearch(content))
                .fetch();
    }

    @Override
    public List<Reply> replyIntegratedSearch(String content) {
        return jpaQueryFactory.selectFrom(reply)
                .join(reply.comment, comment).fetchJoin()
                .join(reply.member, member).fetchJoin()
                .where(QueryDslSearchUtil.replyIntegratedSearch(content))
                .fetch();
    }
}
