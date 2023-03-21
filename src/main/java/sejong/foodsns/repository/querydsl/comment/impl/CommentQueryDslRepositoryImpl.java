package sejong.foodsns.repository.querydsl.comment.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Comment;
import sejong.foodsns.domain.board.QComment;
import sejong.foodsns.repository.querydsl.comment.CommentQueryDslRepository;

import java.util.List;

import static sejong.foodsns.domain.board.QComment.*;

@Repository
@RequiredArgsConstructor
public class CommentQueryDslRepositoryImpl implements CommentQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> searchComments(String content) {

        return jpaQueryFactory.selectFrom(comment)
                .where(comment.content.contains(content))
                .fetch();
    }

    @Override
    public List<Comment> searchCommentsUserName(String username) {

        return jpaQueryFactory.selectFrom(comment)
                .where(comment.member.username.eq(username))
                .fetch();
    }
}
