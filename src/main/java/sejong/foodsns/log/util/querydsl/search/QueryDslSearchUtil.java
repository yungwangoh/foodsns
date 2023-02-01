package sejong.foodsns.log.util.querydsl.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import sejong.foodsns.domain.board.QBoard;
import sejong.foodsns.domain.board.SearchOption;

import java.util.function.Supplier;

import static sejong.foodsns.domain.board.QBoard.*;

public class QueryDslSearchUtil {

    static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> booleanExpressionSupplier) {
        try {
            return new BooleanBuilder(booleanExpressionSupplier.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }

    static BooleanBuilder titleSearch(String title) {
        return nullSafeBuilder(() -> board.title.contains(title));
    }

    static BooleanBuilder contentSearch(String content) {
        return nullSafeBuilder(() -> board.content.contains(content));
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
}
