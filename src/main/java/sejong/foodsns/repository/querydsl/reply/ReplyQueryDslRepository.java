package sejong.foodsns.repository.querydsl.reply;

import sejong.foodsns.domain.board.Reply;

import java.util.List;

public interface ReplyQueryDslRepository {

    List<Reply> searchReplies(String content);
    List<Reply> searchRepliesUserName(String username);
}
