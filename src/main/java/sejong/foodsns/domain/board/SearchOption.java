package sejong.foodsns.domain.board;

public enum SearchOption {

    ALL(0), TITLE(1), CONTENT(2);

    private final int searchValue;

    SearchOption(int searchValue) {
        this.searchValue = searchValue;
    }

    public int getSearchValue() {
        return searchValue;
    }
}
