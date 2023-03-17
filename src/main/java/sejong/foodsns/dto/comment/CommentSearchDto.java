package sejong.foodsns.dto.comment;

import lombok.*;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder
public class CommentSearchDto {

    private Long commentId;
}
