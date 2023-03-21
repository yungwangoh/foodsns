package sejong.foodsns.repository.querydsl.reply.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sejong.foodsns.domain.board.Reply;
import sejong.foodsns.repository.querydsl.reply.ReplyQueryDslRepository;

import java.util.List;

import static sejong.foodsns.domain.board.QReply.*;

@Repository
@RequiredArgsConstructor
public class ReplyQueryDslRepositoryImpl implements ReplyQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Reply> searchReplies(String content) {

        return jpaQueryFactory.selectFrom(reply)
                .where(reply.content.contains(content))
                .fetch();
    }

    @Override
    public List<Reply> searchRepliesUserName(String username) {

        return jpaQueryFactory.selectFrom(reply)
                .where(reply.member.username.eq(username))
                .fetch();
    }
}
