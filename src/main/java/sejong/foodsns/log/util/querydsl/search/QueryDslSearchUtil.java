package sejong.foodsns.log.util.querydsl.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class QueryDslSearchUtil {

    static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> booleanExpressionSupplier) {
        try {
            return new BooleanBuilder(booleanExpressionSupplier.get());
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }
}
