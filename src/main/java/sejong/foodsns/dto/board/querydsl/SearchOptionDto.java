package sejong.foodsns.dto.board.querydsl;

import lombok.*;
import sejong.foodsns.domain.board.SearchOption;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SearchOptionDto {

    private SearchOption searchOption;
    private String content;
}
