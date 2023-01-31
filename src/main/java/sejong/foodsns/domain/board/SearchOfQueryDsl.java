package sejong.foodsns.domain.board;

import com.querydsl.core.types.dsl.BooleanExpression;

public enum SearchOfQueryDsl {

    ALL(0), TITLE(1), CONTENT(2);

    private final int searchValue;

    SearchOfQueryDsl(int searchValue) {
        this.searchValue = searchValue;
    }

    public int getSearchValue() {
        return searchValue;
    }
}
