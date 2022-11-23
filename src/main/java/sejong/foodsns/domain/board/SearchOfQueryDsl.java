package sejong.foodsns.domain.board;

public enum SearchOfQueryDsl {

    ALL(0), TITLE(1), CONTENT(2);

    private int searchValue;
//
    SearchOfQueryDsl(int searchValue) {
        this.searchValue = searchValue;
    }

    public int getSearchValue() {
        return searchValue;
    }
}
