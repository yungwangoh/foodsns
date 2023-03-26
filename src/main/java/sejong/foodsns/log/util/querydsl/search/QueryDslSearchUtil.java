package sejong.foodsns.log.util.querydsl.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import sejong.foodsns.domain.board.SearchOption;

import java.util.function.Supplier;

import static sejong.foodsns.domain.board.QBoard.board;
import static sejong.foodsns.domain.board.QComment.comment;
import static sejong.foodsns.domain.board.QReply.reply;

public class QueryDslSearchUtil {

    private static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> booleanExpressionSupplier) {
        try {
            return new BooleanBuilder(booleanExpressionSupplier.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }

    private static BooleanBuilder boardWriteUser(String username) {
        return nullSafeBuilder(() -> board.member.username.contains(username));
    }

    private static BooleanBuilder titleSearch(String title) {
        return nullSafeBuilder(() -> board.title.contains(title));
    }

    private static BooleanBuilder contentSearch(String content) {
        return nullSafeBuilder(() -> board.content.contains(content));
    }

    private static BooleanBuilder commentWriteUser(String username) {
        return nullSafeBuilder(() -> comment.member.username.contains(username));
    }

    private static BooleanBuilder commentContentSearch(String content) {
        return nullSafeBuilder(() -> comment.content.contains(content));
    }

    private static BooleanBuilder replyWriteUser(String username) {
        return nullSafeBuilder(() -> reply.member.username.contains(username));
    }

    private static BooleanBuilder replyContentSearch(String content) {
        return nullSafeBuilder(() -> reply.content.contains(content));
    }

    public static BooleanBuilder boardIntegratedSearch(String content) {
        return titleSearch(content).or(contentSearch(content)).or(boardWriteUser(content));
    }

    public static BooleanBuilder commentIntegratedSearch(String content) {
        return commentWriteUser(content).or(commentContentSearch(content));
    }

    public static BooleanBuilder replyIntegratedSearch(String content) {
        return replyWriteUser(content).or(replyContentSearch(content));
    }

    public static BooleanBuilder searchOptionCheck(SearchOption searchOption, String content) {
        if(searchOption == SearchOption.TITLE) {
            return titleSearch(content);
        } else if (searchOption == SearchOption.CONTENT) {
            return contentSearch(content);
        } else {
            return titleSearch(content).or(contentSearch(content));
        }
    }

    public static BooleanBuilder integratedSearch(String contents) {
        return boardIntegratedSearch(contents)
                .or(commentIntegratedSearch(contents))
                .or(replyIntegratedSearch(contents));
    }
}
